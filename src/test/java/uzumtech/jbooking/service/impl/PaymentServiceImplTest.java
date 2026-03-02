package uzumtech.jbooking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uzumtech.jbooking.component.adapter.JBankAdapter;
import uzumtech.jbooking.component.adapter.JNotificationAdapter;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.constant.enums.PaymentStatus;
import uzumtech.jbooking.constant.enums.PaymentType;
import uzumtech.jbooking.dto.request.BankWebhookRequest;
import uzumtech.jbooking.dto.request.PaymentRequest;
import uzumtech.jbooking.dto.response.PaymentResponse;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.Payment;
import uzumtech.jbooking.entity.User;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.PaymentRepository;
import uzumtech.jbooking.service.PaymentRefundValidator;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

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

    @Mock
    JBankAdapter jBankAdapter;

    @Mock
    JNotificationAdapter jNotificationAdapter;

    @InjectMocks
    PaymentServiceImpl paymentService;


    private Booking bookingWithUser(BookingStatus status) {
        User user = new User();
        user.setId(UUID.randomUUID());

        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setUser(user);
        booking.setBookingStatus(status);
        return booking;
    }


    @Test
    void processPayment_shouldSavePaymentWithPendingAndReturnPending() {
        Booking booking = bookingWithUser(BookingStatus.HOLD);

        PaymentRequest request = new PaymentRequest(
                booking.getId(), BigDecimal.valueOf(500), PaymentType.PREPAYMENT,
                PaymentStatus.PENDING, null
        );

        PaymentResponse bankResponse = new PaymentResponse(
                "TXN-BANK-001", PaymentStatus.SUCCESS, BigDecimal.valueOf(500), "Hold placed"
        );

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(jBankAdapter.holdPayment(request)).thenReturn(bankResponse);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        PaymentResponse result = paymentService.processPayment(request);

        // processPayment всегда возвращает PENDING — финальный статус придёт через webhook
        assertThat(result.transactionId()).isEqualTo("TXN-BANK-001");
        assertThat(result.paymentStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(500));

        // букинг остаётся HOLD до webhook
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.HOLD);

        verify(jBankAdapter).holdPayment(request);
        verify(paymentRepository).save(any(Payment.class));
        verifyNoInteractions(jNotificationAdapter);
    }

    @Test
    void processPayment_shouldThrowWhenBookingNotFound() {
        UUID randomId = UUID.randomUUID();
        PaymentRequest request = new PaymentRequest(
                randomId, BigDecimal.valueOf(500), PaymentType.PREPAYMENT,
                PaymentStatus.PENDING, null
        );

        when(bookingRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.processPayment(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Booking not found");

        verifyNoInteractions(jBankAdapter);
    }


    @Test
    void refund_shouldInitiateRefundInBankAndSetRefundedStatus() {
        Booking booking = bookingWithUser(BookingStatus.PAID);

        Payment payment = new Payment();
        payment.setTransactionId("TXN-BANK-001");
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setBooking(booking);

        PaymentResponse bankResponse = new PaymentResponse(
                "TXN-BANK-001", PaymentStatus.REFUNDED, BigDecimal.valueOf(500), "Refunded"
        );

        doNothing().when(refundValidator).validateRefundAllowed(booking.getId());
        when(paymentRepository.findByBookingIdAndPaymentStatus(booking.getId(), PaymentStatus.SUCCESS))
                .thenReturn(Optional.of(payment));
        when(jBankAdapter.refundPayment("TXN-BANK-001")).thenReturn(bankResponse);

        paymentService.refund(booking.getId());

        // Статус платежа помечается REFUNDED — букинг изменится через webhook
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.PAID);

        verify(jBankAdapter).refundPayment("TXN-BANK-001");
        verifyNoInteractions(jNotificationAdapter);
    }

    @Test
    void refund_shouldThrowWhenSuccessPaymentNotFound() {
        UUID bookingId = UUID.randomUUID();

        doNothing().when(refundValidator).validateRefundAllowed(bookingId);
        when(paymentRepository.findByBookingIdAndPaymentStatus(bookingId, PaymentStatus.SUCCESS))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.refund(bookingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Success payment not found");

        verifyNoInteractions(jBankAdapter);
    }


    @Test
    void handleBankWebhook_shouldConfirmPaymentAndSetPaidWhenPendingAndSuccess() {
        Booking booking = bookingWithUser(BookingStatus.HOLD);

        Payment payment = new Payment();
        payment.setTransactionId("TXN-BANK-001");
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setAmount(BigDecimal.valueOf(500));
        payment.setBooking(booking);

        BankWebhookRequest request = new BankWebhookRequest(
                "TXN-BANK-001", PaymentStatus.SUCCESS, BigDecimal.valueOf(500)
        );

        when(paymentRepository.findByTransactionId("TXN-BANK-001")).thenReturn(Optional.of(payment));

        paymentService.handleBankWebhook(request);

        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.PAID);
        verify(jNotificationAdapter).sendPaymentSuccess(any(), any());
    }

    @Test
    void handleBankWebhook_shouldFailPaymentAndKeepHoldWhenPendingAndFailed() {
        Booking booking = bookingWithUser(BookingStatus.HOLD);

        Payment payment = new Payment();
        payment.setTransactionId("TXN-BANK-002");
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setAmount(BigDecimal.valueOf(500));
        payment.setBooking(booking);

        BankWebhookRequest request = new BankWebhookRequest(
                "TXN-BANK-002", PaymentStatus.FAILED, BigDecimal.valueOf(500)
        );

        when(paymentRepository.findByTransactionId("TXN-BANK-002")).thenReturn(Optional.of(payment));

        paymentService.handleBankWebhook(request);

        // Оплата не прошла — букинг остаётся HOLD
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.HOLD);
        verify(jNotificationAdapter).sendBookingCancellation(any(), any());
    }


    @Test
    void handleBankWebhook_shouldCancelBookingWhenRefundedAndSuccess() {
        Booking booking = bookingWithUser(BookingStatus.PAID);

        Payment payment = new Payment();
        payment.setTransactionId("TXN-BANK-003");
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        payment.setAmount(BigDecimal.valueOf(500));
        payment.setBooking(booking);

        BankWebhookRequest request = new BankWebhookRequest(
                "TXN-BANK-003", PaymentStatus.SUCCESS, BigDecimal.valueOf(500)
        );

        when(paymentRepository.findByTransactionId("TXN-BANK-003")).thenReturn(Optional.of(payment));

        paymentService.handleBankWebhook(request);

        // Банк подтвердил возврат — букинг отменяется
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.CANCELLED);
        verify(jNotificationAdapter).sendBookingCancellation(any(), any());
    }

    @Test
    void handleBankWebhook_shouldRollbackToSuccessWhenRefundDeclined() {
        Booking booking = bookingWithUser(BookingStatus.PAID);

        Payment payment = new Payment();
        payment.setTransactionId("TXN-BANK-004");
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        payment.setAmount(BigDecimal.valueOf(500));
        payment.setBooking(booking);

        BankWebhookRequest request = new BankWebhookRequest(
                "TXN-BANK-004", PaymentStatus.FAILED, BigDecimal.valueOf(500)
        );

        when(paymentRepository.findByTransactionId("TXN-BANK-004")).thenReturn(Optional.of(payment));

        paymentService.handleBankWebhook(request);

        // Банк отказал в возврате — платёж откатывается в SUCCESS
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.PAID);
        verifyNoInteractions(jNotificationAdapter);
    }

    @Test
    void handleBankWebhook_shouldThrowWhenPaymentNotFound() {
        BankWebhookRequest request = new BankWebhookRequest(
                "unknown-txn", PaymentStatus.SUCCESS, BigDecimal.valueOf(500)
        );

        when(paymentRepository.findByTransactionId("unknown-txn")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.handleBankWebhook(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Payment not found");
    }
}