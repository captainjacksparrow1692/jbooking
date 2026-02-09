package uzumtech.jbooking.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorDto(
        int code,
        String message,
        LocalDateTime timestamp,
        List<String> validationErrors
) {
}
