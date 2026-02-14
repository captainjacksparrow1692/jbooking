package uzumtech.jbooking.service;

import uzumtech.jbooking.constant.enums.PaymentStatus;
import uzumtech.jbooking.dto.request.BankWebhookRequest;
import uzumtech.jbooking.dto.request.PaymentRequest;
import uzumtech.jbooking.dto.response.PaymentResponse;

public interface PaymentService {

    // Обработка транзакции
    PaymentResponse processPayment(PaymentRequest request);

    // Проверка статуса во внешней системе
    PaymentStatus checkExternalStatus(String transactionId);

    // Возврат средств при отмене
    void refund(Long bookingId);

    //метод обработки
    void handleRefundWebhook(BankWebhookRequest request);
}
