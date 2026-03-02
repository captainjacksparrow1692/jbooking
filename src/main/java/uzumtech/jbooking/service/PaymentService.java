package uzumtech.jbooking.service;

import uzumtech.jbooking.dto.request.BankWebhookRequest;
import uzumtech.jbooking.dto.request.PaymentRequest;
import uzumtech.jbooking.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest request);

    void refund(UUID bookingId);

    void handleBankWebhook(BankWebhookRequest request);
}
