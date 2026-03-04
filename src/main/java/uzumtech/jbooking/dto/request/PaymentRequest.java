package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import uzumtech.jbooking.constant.enums.PaymentStatus;
import uzumtech.jbooking.constant.enums.PaymentType;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        @NotNull(message = "Booking ID cannot be null")
        UUID bookingId,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Payment type is required")
        PaymentType paymentType, // PREPAYMENT, PARTIAL, PAY_AT_HOTEL

        String transactionId
) {
}