package uzumtech.jbooking.dto.request;

import java.math.BigDecimal;

public record BankWebhookRequest (
        String transactionId, // Уникальный ID транзакции в банке
        String paymentStatus, // Статус от банка
        BigDecimal amount, // Сумма транзакции
        String type // Тип события
){
}
