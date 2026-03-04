package uzumtech.jbooking.component.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import uzumtech.jbooking.constant.enums.NotificationType;
import uzumtech.jbooking.dto.request.NotificationRequest;

import java.util.UUID;

@Slf4j
@Component
public class JNotificationAdapter {

    private final RestClient restClient;

    public JNotificationAdapter(@Qualifier("jnotificationClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public void sendBookingConfirmation(UUID userId, String message) {
        send(new NotificationRequest(userId, NotificationType.EMAIL, "Booking Confirmed", message));
    }

    public void sendPaymentSuccess(UUID userId, String message) {
        send(new NotificationRequest(userId, NotificationType.EMAIL, "Payment Successful", message));
    }

    public void sendBookingCancellation(UUID userId, String message) {
        send(new NotificationRequest(userId, NotificationType.EMAIL, "Booking Cancelled", message));
    }

    private void send(NotificationRequest request) {
        log.info("Sending {} notification to userId={}", request.type(), request.userId());

        try {
            restClient
                    .post()
                    .uri("/api/v1/notifications/send")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Notification sent successfully to userId={}", request.userId());
        } catch (Exception e) {
            log.error("Failed to send {} notification to userId={}: {}",
                    request.type(), request.userId(), e.getMessage());
        }
    }
}