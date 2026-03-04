package uzumtech.jbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.constant.enums.CancellationPolicyType;
import uzumtech.jbooking.constant.enums.Error;
import uzumtech.jbooking.constant.enums.ErrorType;
import uzumtech.jbooking.entity.Booking;
import uzumtech.jbooking.exception.BusinessException;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.repository.BookingRepository;
import uzumtech.jbooking.repository.PaymentRepository;
import uzumtech.jbooking.constant.enums.PaymentStatus;

import org.springframework.http.HttpStatus;
import uzumtech.jbooking.service.PaymentRefundValidator;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentRefundValidatorImpl implements PaymentRefundValidator {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void validateRefundAllowed(UUID bookingId) {
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

        if (paymentRepository.findByBookingIdAndPaymentStatus(bookingId, PaymentStatus.SUCCESS).isEmpty()) {
            throw new ResourceNotFoundException("Success payment not found");
        }
    }
}
