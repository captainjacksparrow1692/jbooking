package uzumtech.jbooking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uzumtech.jbooking.constant.Constant;
import uzumtech.jbooking.dto.request.BankWebhookRequest;
import uzumtech.jbooking.dto.request.PaymentRequest;
import uzumtech.jbooking.dto.response.PaymentResponse;
import uzumtech.jbooking.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    //процесс оплаты и тд
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> process(@RequestBody @Valid PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    //возврат денег
    @PostMapping("/refund/{bookingId}")
    public ResponseEntity<Void> refund(@PathVariable @NotNull @Positive UUID bookingId) {
        paymentService.refund(bookingId);
        return ResponseEntity.noContent().build();
    }

    //вебхук от банка
    @PostMapping("/webhook/bank")
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
            @RequestBody @Valid BankWebhookRequest request) {

        // Минимальная защита: проверка секретного ключа
        if (!Constant.BANK_WEBHOOK_SECRET.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        paymentService.handleRefundWebhook(request);
        return ResponseEntity.ok().build();
    }
}