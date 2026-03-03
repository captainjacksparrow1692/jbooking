package uzumtech.jbooking.dto.request;

import uzumtech.jbooking.constant.enums.NotificationType;

import java.util.UUID;

public record NotificationRequest(
        UUID userId,
        NotificationType type,  // EMAIL
        String subject,
        String message
) {
}