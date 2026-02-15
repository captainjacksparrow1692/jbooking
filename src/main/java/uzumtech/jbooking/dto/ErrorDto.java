package uzumtech.jbooking.dto;

import lombok.Builder;
import uzumtech.jbooking.constant.enums.ErrorType;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErrorDto(
        int code,
        ErrorType errorType,
        String message,
        LocalDateTime timestamp,
        List<String> validationErrors
) {
}
