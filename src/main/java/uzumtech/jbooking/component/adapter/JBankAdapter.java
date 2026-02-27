package uzumtech.jbooking.component.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uzumtech.jbooking.dto.request.PaymentRequest;
import uzumtech.jbooking.dto.response.PaymentResponse;

@Slf4j
@Component
public class JBankAdapter {

    private final RestClient restClient;

    public JBankAdapter(@Qualifier("jbankClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public PaymentResponse holdPayment(PaymentRequest request) {
        log.info("Sending hold payment request to jBank, bookingId={}", request.bookingId());

        return restClient
                .post()
                .uri("/api/v1/payments/hold")
                .body(request)
                .retrieve()
                .body(PaymentResponse.class);
    }

    public PaymentResponse confirmPayment(String transactionId) {
        log.info("Confirming payment in jBank, transactionId={}", transactionId);

        return restClient
                .post()
                .uri("/api/v1/payments/{transactionId}/confirm", transactionId)
                .retrieve()
                .body(PaymentResponse.class);
    }

    public PaymentResponse refundPayment(String transactionId) {
        log.info("Sending refund request to jBank, transactionId={}", transactionId);

        return restClient
                .post()
                .uri("/api/v1/payments/{transactionId}/refund", transactionId)
                .retrieve()
                .body(PaymentResponse.class);
    }
}