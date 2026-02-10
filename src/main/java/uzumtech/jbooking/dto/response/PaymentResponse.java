package uzumtech.jbooking.dto.response;

import uzumtech.jbooking.constant.enums.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponse (
        String transactionId,
        PaymentStatus paymentStatus,
        BigDecimal amount,
        String message // оплата произошла или отменена и тд
){
}
