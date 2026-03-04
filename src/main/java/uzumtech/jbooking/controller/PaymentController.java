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

    // инициируем оплату → jbooking холдирует в банке → ответ PENDING
    // финальный статус придёт через webhook
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> process(@RequestBody @Valid PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    // инициируем возврат → банк обрабатывает → подтверждение через webhook
    @PostMapping("/refund/{bookingId}")
    public ResponseEntity<Void> refund(@PathVariable @NotNull UUID bookingId) {
        paymentService.refund(bookingId);
        return ResponseEntity.noContent().build();
    }

    // единый webhook от банка — обрабатывает и оплату и возврат
    @PostMapping("/webhook/bank")
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
            @RequestBody @Valid BankWebhookRequest request) {

        if (!Constant.BANK_WEBHOOK_SECRET.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        paymentService.handleBankWebhook(request);
        return ResponseEntity.ok().build();
    }
}