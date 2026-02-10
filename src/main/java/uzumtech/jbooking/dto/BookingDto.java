package uzumtech.jbooking.dto;

import lombok.Builder;

@Builder
public record BookingDto(
        String key, String correlationId, String message
){
}
