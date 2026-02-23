package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import uzumtech.jbooking.constant.enums.PaymentStatus;
import uzumtech.jbooking.constant.enums.PaymentType;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull(message = "Booking ID cannot be null")
        Long bookingId,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Payment type is required")
        PaymentType paymentType, // PREPAYMENT, PARTIAL, PAY_AT_HOTEL

        @NotNull(message = "Payment status is required")
        PaymentStatus paymentStatus, // PENDING, SUCCESS, FAILED

        String transactionId // ID от платежного шлюза
) {
}