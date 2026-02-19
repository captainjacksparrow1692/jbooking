package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import uzumtech.jbooking.service.PaymentService;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

    PaymentRepository paymentRepository;
    BookingRepository bookingRepository;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
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

        return new PaymentResponse(payment.getTransactionId(), payment.getPaymentStatus(), payment.getAmount(), "OK");
    }

    @Override
    @Transactional
    public void refund(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getRoom().getCancellationPolicyType() == CancellationPolicyType.NON_REFUNDABLE) {
            throw new BusinessException(
                    Error.CANCELLATION_NOT_ALLOWED_ERROR_CODE.getCode(),
                    "This booking is non-refundable",
                    HttpStatus.BAD_REQUEST,
                    ErrorType.BUSINESS
            );
        }

        Payment payment = paymentRepository.findByBookingIdAndPaymentStatus(bookingId, PaymentStatus.SUCCESS)
                .orElseThrow(() -> new ResourceNotFoundException("Success payment not found"));

        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        booking.setBookingStatus(BookingStatus.CANCELLED);
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