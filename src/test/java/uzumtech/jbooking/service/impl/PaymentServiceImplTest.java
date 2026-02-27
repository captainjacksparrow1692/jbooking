package uzumtech.jbooking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uzumtech.jbooking.constant.Constant;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.constant.enums.PaymentStatus;
import uzumtech.jbooking.constant.enums.PaymentType;
import uzumtech.jbooking.dto.request.BankWebhookRequest;
import uzumtech.jbooking.dto.request.PaymentRequest;
import uzumtech.jbooking.dto.response.PaymentResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.Payment;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.PaymentRepository;
import uzumtech.jbooking.service.PaymentRefundValidator;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    PaymentRefundValidator refundValidator;

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Test
    void processPayment_shouldSavePaymentAndConfirmBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.HOLD);

        PaymentRequest request = new PaymentRequest(
                1L, BigDecimal.valueOf(500), PaymentType.PREPAYMENT,
                PaymentStatus.SUCCESS, "txn-001"
        );

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        PaymentResponse result = paymentService.processPayment(request);

        assertThat(result.transactionId()).isEqualTo("txn-001");
        assertThat(result.paymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(500));
        assertThat(result.message()).isEqualTo(Constant.PAYMENT_SUCCESS_MESSAGE);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.CONFIRMED);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void processPayment_shouldNotConfirmBookingWhenPaymentFailed() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.HOLD);

        PaymentRequest request = new PaymentRequest(
                1L, BigDecimal.valueOf(500), PaymentType.PREPAYMENT,
                PaymentStatus.FAILED, "txn-002"
        );

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        PaymentResponse result = paymentService.processPayment(request);

        assertThat(result.paymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.HOLD);
    }

    @Test
    void processPayment_shouldThrowWhenBookingNotFound() {
        PaymentRequest request = new PaymentRequest(
                999L, BigDecimal.valueOf(500), PaymentType.PREPAYMENT,
                PaymentStatus.SUCCESS, "txn-003"
        );

        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.processPayment(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Booking not found");
    }

    @Test
    void refund_shouldSetPaymentRefundedAndBookingCancelled() {
        Long bookingId = 1L;

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setBooking(booking);

        doNothing().when(refundValidator).validateRefundAllowed(bookingId);
        when(paymentRepository.findByBookingIdAndPaymentStatus(bookingId, PaymentStatus.SUCCESS))
                .thenReturn(Optional.of(payment));

        paymentService.refund(bookingId);

        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    void refund_shouldThrowWhenSuccessPaymentNotFound() {
        Long bookingId = 1L;

        doNothing().when(refundValidator).validateRefundAllowed(bookingId);
        when(paymentRepository.findByBookingIdAndPaymentStatus(bookingId, PaymentStatus.SUCCESS))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.refund(bookingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Success payment not found");
    }

    @Test
    void handleRefundWebhook_shouldRefundOnSuccess() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        Payment payment = new Payment();
        payment.setTransactionId("txn-001");
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setBooking(booking);

        BankWebhookRequest request = new BankWebhookRequest(
                "txn-001", PaymentStatus.SUCCESS, BigDecimal.valueOf(500)
        );

        when(paymentRepository.findByTransactionId("txn-001")).thenReturn(Optional.of(payment));

        paymentService.handleRefundWebhook(request);

        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    void handleRefundWebhook_shouldNotRefundOnFailedStatus() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        Payment payment = new Payment();
        payment.setTransactionId("txn-001");
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setBooking(booking);

        BankWebhookRequest request = new BankWebhookRequest(
                "txn-001", PaymentStatus.FAILED, BigDecimal.valueOf(500)
        );

        when(paymentRepository.findByTransactionId("txn-001")).thenReturn(Optional.of(payment));

        paymentService.handleRefundWebhook(request);

        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    void handleRefundWebhook_shouldThrowWhenPaymentNotFound() {
        BankWebhookRequest request = new BankWebhookRequest(
                "unknown-txn", PaymentStatus.SUCCESS, BigDecimal.valueOf(500)
        );

        when(paymentRepository.findByTransactionId("unknown-txn")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.handleRefundWebhook(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Payment not found");
    }
}
