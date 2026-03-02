package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uzumtech.jbooking.constant.enums.PaymentStatus;

import java.math.BigDecimal;

public record BankWebhookRequest (
        @NotNull
        String transactionId,

        @NotNull
        PaymentStatus paymentStatus,

        @NotNull
        @Positive
        BigDecimal amount
){
}