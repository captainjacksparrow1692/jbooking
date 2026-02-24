package uzumtech.jbooking.service;

/**
 * Валидация возврата выполняется вне транзакции записи,
 * чтобы не держать блокировки при проверках.
 */
public interface PaymentRefundValidator {

    void validateRefundAllowed(Long bookingId);
}
