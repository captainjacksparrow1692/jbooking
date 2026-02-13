package uzumtech.jbooking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.constant.enums.PaymentStatus;
import uzumtech.jbooking.dto.request.PaymentRequest;
import uzumtech.jbooking.dto.response.PaymentResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.Payment;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.PaymentRepository;
import uzumtech.jbooking.service.PaymentService;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

    PaymentRepository paymentRepository;
    BookingRepository bookingRepository;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request){
        log.info("Processing Payment: {}", request.bookingId());

        //проверка существования брони
        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: " + request.bookingId() + " not found"));

        //запись в системе о платиже
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.amount());
        payment.setPaymentStatus(request.paymentStatus()); //получаем статус от банка
        payment.setTransactionId(request.transactionId());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentType(request.paymentType());

        paymentRepository.save(payment);

        //если оплата успешна подтверждение
        if (PaymentStatus.SUCCESS.equals(request.paymentStatus())) {
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
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
        log.info("Refunding booking with id: {}", bookingId);

        Payment payment = paymentRepository.findByBookingIdAndPaymentStatus(bookingId, PaymentStatus.SUCCESS)
                .orElseThrow(() -> new EntityNotFoundException("Payment with id: " + bookingId + " not found"));

        payment.setPaymentStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: " + bookingId + " not found"));
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        log.info("Refund processed for booking {}", bookingId);
    }

}
