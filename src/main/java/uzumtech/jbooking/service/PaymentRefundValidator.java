package uzumtech.jbooking.service;

import java.util.UUID;

/**
 * Валидация возврата выполняется вне транзакции записи,
 * чтобы не держать блокировки при проверках.
 */
public interface PaymentRefundValidator {

    void validateRefundAllowed(UUID bookingId);
}
