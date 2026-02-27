package uzumtech.jbooking.dto.response;

import java.util.UUID;

public record CityResponse(
        UUID cityId,
        String name,
        String country,
        String timezone
) {
}
