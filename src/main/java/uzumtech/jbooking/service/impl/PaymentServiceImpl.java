package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.constant.Constant;
import uzumtech.jbooking.constant.enums.*;
import uzumtech.jbooking.dto.request.BankWebhookRequest;
import uzumtech.jbooking.dto.request.PaymentRequest;
import uzumtech.jbooking.dto.response.PaymentResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.Payment;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.PaymentRepository;
import uzumtech.jbooking.service.PaymentService;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

    PaymentRepository paymentRepository;
    BookingRepository bookingRepository;
    uzumtech.jbooking.service.PaymentRefundValidator refundValidator;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        // TODO: Интеграция с платёжным шлюзом (Stripe, PayPal, etc.):
        // 1. Вызов API банка/платёжной системы
        // 2. Обработка 3DS, верификации
        // 3. Отмена/rollback при ошибке оплаты
        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.amount());
        payment.setPaymentStatus(request.paymentStatus());
        payment.setTransactionId(request.transactionId());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentType(request.paymentType());

        paymentRepository.save(payment);

        if (PaymentStatus.SUCCESS.equals(request.paymentStatus())) {
            booking.setBookingStatus(BookingStatus.CONFIRMED);
        }

        return new PaymentResponse(payment.getTransactionId(), payment.getPaymentStatus(), payment.getAmount(), Constant.PAYMENT_SUCCESS_MESSAGE);
    }

    @Override
    @Transactional
    public void refund(UUID bookingId) {
        refundValidator.validateRefundAllowed(bookingId);

        Payment payment = paymentRepository.findByBookingIdAndPaymentStatus(bookingId, PaymentStatus.SUCCESS)
                .orElseThrow(() -> new ResourceNotFoundException("Success payment not found"));

        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        payment.getBooking().setBookingStatus(BookingStatus.CANCELLED);
    }

    @Override
    @Transactional
    public void handleRefundWebhook(BankWebhookRequest request) {
        Payment payment = paymentRepository.findByTransactionId(request.transactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (PaymentStatus.SUCCESS.equals(request.paymentStatus())) {
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
            payment.getBooking().setBookingStatus(BookingStatus.CANCELLED);
        }
    }
}