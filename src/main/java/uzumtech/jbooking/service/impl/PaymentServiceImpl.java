package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.component.adapter.JBankAdapter;
import uzumtech.jbooking.component.adapter.JNotificationAdapter;
import uzumtech.jbooking.constant.enums.*;
import uzumtech.jbooking.constant.enums.Error;
import uzumtech.jbooking.dto.request.BankWebhookRequest;
import uzumtech.jbooking.dto.request.PaymentRequest;
import uzumtech.jbooking.dto.response.PaymentResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.Payment;
import uzumtech.jbooking.exception.BusinessException;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.PaymentRepository;
import uzumtech.jbooking.service.PaymentRefundValidator;
import uzumtech.jbooking.service.PaymentService;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

    PaymentRepository paymentRepository;
    BookingRepository bookingRepository;
    PaymentRefundValidator refundValidator;
    JBankAdapter jBankAdapter;
    JNotificationAdapter jNotificationAdapter;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        // 1. Находим бронирование
        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // 2. Отправляем hold в банк — только инициируем оплату
        log.info("Sending hold payment request to jBank, bookingId={}", request.bookingId());
        PaymentResponse bankResponse = jBankAdapter.holdPayment(request);

        // 3. Сохраняем платёж со статусом PENDING
        // Финальный статус придёт асинхронно через webhook от банка
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.amount());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId(bankResponse.transactionId()); // ID от банка
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentType(request.paymentType());
        paymentRepository.save(payment);

        // Статус букинга остаётся HOLD до подтверждения от банка через webhook
        log.info("Payment initiated, transactionId={}, awaiting bank webhook", bankResponse.transactionId());

        return new PaymentResponse(
                payment.getTransactionId(),
                payment.getPaymentStatus(),
                payment.getAmount(),
                "Payment initiated, awaiting bank confirmation"
        );
    }

    @Override
    @Transactional
    public void refund(UUID bookingId) {
        refundValidator.validateRefundAllowed(bookingId);

        Payment payment = paymentRepository.findByBookingIdAndPaymentStatus(bookingId, PaymentStatus.SUCCESS)
                .orElseThrow(() -> new ResourceNotFoundException("Success payment not found"));

        // Отправляем запрос на возврат в банк
        log.info("Sending refund request to jBank, transactionId={}", payment.getTransactionId());
        PaymentResponse bankResponse = jBankAdapter.refundPayment(payment.getTransactionId());

        if (bankResponse == null) {
            throw new BusinessException(
                    Error.REFUND_NOT_POSSIBLE_ERROR_CODE.getCode(),
                    "No response from bank",
                    HttpStatus.BAD_GATEWAY,
                    ErrorType.BUSINESS
            );
        }

        // Помечаем платёж как REFUNDED — финальное подтверждение придёт через webhook
        payment.setPaymentStatus(PaymentStatus.REFUNDED);

        log.info("Refund initiated, transactionId={}, awaiting bank webhook", payment.getTransactionId());
    }

    @Override
    @Transactional
    public void handleBankWebhook(BankWebhookRequest request) {
        Payment payment = paymentRepository.findByTransactionId(request.transactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        Booking booking = payment.getBooking();
        UUID userId = booking.getUser().getId();

        if (PaymentStatus.SUCCESS.equals(request.paymentStatus())) {

            if (PaymentStatus.PENDING.equals(payment.getPaymentStatus())) {
                // Банк подтвердил оплату
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                booking.setBookingStatus(BookingStatus.PAID);
                log.info("Payment confirmed by bank, transactionId={}, bookingId={}", request.transactionId(), booking.getId());

                jNotificationAdapter.sendPaymentSuccess(
                        userId,
                        "Payment of " + payment.getAmount() + " successful. Booking " + booking.getId() + " is confirmed."
                );

            } else if (PaymentStatus.REFUNDED.equals(payment.getPaymentStatus())) {
                // Банк подтвердил возврат
                booking.setBookingStatus(BookingStatus.CANCELLED);
                log.info("Refund confirmed by bank, transactionId={}, bookingId={}", request.transactionId(), booking.getId());

                jNotificationAdapter.sendBookingCancellation(
                        userId,
                        "Refund of " + payment.getAmount() + " confirmed. Booking " + booking.getId() + " has been cancelled."
                );
            }

        } else {
            // Банк отказал

            if (PaymentStatus.PENDING.equals(payment.getPaymentStatus())) {
                // Оплата не прошла — букинг остаётся HOLD
                payment.setPaymentStatus(PaymentStatus.FAILED);
                log.warn("Payment failed, transactionId={}, bookingId={}", request.transactionId(), booking.getId());

                jNotificationAdapter.sendBookingCancellation(
                        userId,
                        "Payment failed for booking " + booking.getId() + ". Please try again."
                );

            } else if (PaymentStatus.REFUNDED.equals(payment.getPaymentStatus())) {
                // Банк отказал в возврате — откатываем статус платежа
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                log.warn("Refund declined by bank, transactionId={}", request.transactionId());
            }
        }
    }
}