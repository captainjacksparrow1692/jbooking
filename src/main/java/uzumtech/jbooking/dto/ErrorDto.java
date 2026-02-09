package uzumtech.jbooking.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErrorDto(
        int code,
        String message,
        LocalDateTime timestamp,
        List<String> validationErrors
) {
}
