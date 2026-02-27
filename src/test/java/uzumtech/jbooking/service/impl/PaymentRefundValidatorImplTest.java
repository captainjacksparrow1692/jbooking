package uzumtech.jbooking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uzumtech.jbooking.constant.enums.CancellationPolicyType;
import uzumtech.jbooking.constant.enums.PaymentStatus;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.entity.Payment;
import uzumtech.jbooking.entity.Room;
import uzumtech.jbooking.exception.BusinessException;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.PaymentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentRefundValidatorImplTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    PaymentRefundValidatorImpl validator;

    @Test
    void validateRefundAllowed_shouldPassForRefundableBooking() {
        Long bookingId = 1L;

        Room room = Room.builder()
                .cancellationPolicyType(CancellationPolicyType.FREE_CANCELLATION)
                .build();

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setRoom(room);

        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(paymentRepository.findByBookingIdAndPaymentStatus(bookingId, PaymentStatus.SUCCESS))
                .thenReturn(Optional.of(payment));

        assertThatCode(() -> validator.validateRefundAllowed(bookingId))
                .doesNotThrowAnyException();
    }

    @Test
    void validateRefundAllowed_shouldThrowForNonRefundableBooking() {
        Long bookingId = 1L;

        Room room = Room.builder()
                .cancellationPolicyType(CancellationPolicyType.NON_REFUNDABLE)
                .build();

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setRoom(room);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> validator.validateRefundAllowed(bookingId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("non-refundable");
    }

    @Test
    void validateRefundAllowed_shouldThrowWhenBookingNotFound() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validator.validateRefundAllowed(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Booking not found");
    }

    @Test
    void validateRefundAllowed_shouldThrowWhenNoSuccessPayment() {
        Long bookingId = 1L;

        Room room = Room.builder()
                .cancellationPolicyType(CancellationPolicyType.FREE_CANCELLATION)
                .build();

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setRoom(room);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(paymentRepository.findByBookingIdAndPaymentStatus(bookingId, PaymentStatus.SUCCESS))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> validator.validateRefundAllowed(bookingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Success payment not found");
    }
}
