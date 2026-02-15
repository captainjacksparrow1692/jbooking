package uzumtech.jbooking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.constant.enums.*;
import uzumtech.jbooking.constant.enums.Error;
import uzumtech.jbooking.dto.request.BankWebhookRequest;
import uzumtech.jbooking.dto.request.PaymentRequest;
import uzumtech.jbooking.dto.response.PaymentResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.BookingHistory;
import uzumtech.jbooking.entity.Payment;
import uzumtech.jbooking.exception.BusinessException;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.repository.BookingHistoryRepository;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.PaymentRepository;
import uzumtech.jbooking.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

    PaymentRepository paymentRepository;
    BookingRepository bookingRepository;
    BookingHistoryRepository bookingHistoryRepository;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request){
        log.info("Processing Payment: {}", request.bookingId());

        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: " + request.bookingId() + " not found"));

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

            logAction(booking, HistoryActionType.PAYMENT, "Payment received successfully. Amount: " + payment.getAmount());

            log.info("Booking with id: {} has been confirmed", booking.getId());
        }

        return new PaymentResponse(
                payment.getTransactionId(),
                payment.getPaymentStatus(),
                payment.getAmount(),
                "Payment status is " + payment.getPaymentStatus()
        );
    }

    @Override
    public PaymentStatus checkExternalStatus(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .map(Payment::getPaymentStatus)
                .orElseThrow(() -> new EntityNotFoundException("Payment with id: " + transactionId + " not found"));
    }

    @Override
    @Transactional
    public void refund(Long bookingId){
        log.info("Checking cancellation policy for bookingId: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        CancellationPolicyType policy = booking.getRoom().getCancellationPolicyType();

        if (policy == CancellationPolicyType.NON_REFUNDABLE) {
            throw new BusinessException(
                    Error.CANCELLATION_NOT_ALLOWED_ERROR_CODE.getCode(),
                    "This booking is non-refundable",
                    HttpStatus.BAD_REQUEST,
                    ErrorType.BUSINESS
            );
        }

        Payment payment = paymentRepository.findByBookingIdAndPaymentStatus(bookingId, PaymentStatus.SUCCESS)
                .orElseThrow(() -> new ResourceNotFoundException("No successful payment found to refund"));

        BigDecimal amountToRefund = payment.getAmount();
        if (policy == CancellationPolicyType.PARTIAL_REFUND) {
            amountToRefund = amountToRefund.multiply(BigDecimal.valueOf(0.5));
        }

        // bankClient.refund(payment.getTransactionId(), amountToRefund);

        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        booking.setBookingStatus(BookingStatus.CANCELLED);

        logAction(booking, HistoryActionType.REFUND, "Refund initiated. Amount: " + amountToRefund);

        log.info("Refund successful for bookingId: {}", bookingId);
    }

    @Override
    @Transactional
    public void handleRefundWebhook(BankWebhookRequest request) {
        Payment payment = paymentRepository.findByTransactionId(request.transactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if ("SUCCESS".equals(request.paymentStatus())) {
            payment.setPaymentStatus(PaymentStatus.REFUNDED);

            Booking booking = payment.getBooking();
            booking.setBookingStatus(BookingStatus.CANCELLED);

            logAction(booking, HistoryActionType.REFUND, "Refund confirmed by bank via webhook.");

            log.info("Refund confirmed for booking ID: {}", booking.getId());
        }
    }

    //сохраняем запись в историю бронирования
    private void logAction(Booking booking, HistoryActionType historyActionType, String details) {
        BookingHistory history = new BookingHistory();
        history.setBooking(booking);
        history.setHistoryActionType(historyActionType);
        history.setBookingStatus(booking.getBookingStatus());
        history.setActionTimestamp(LocalDateTime.now());
        history.setDetails(details);

        bookingHistoryRepository.save(history);
    }
}