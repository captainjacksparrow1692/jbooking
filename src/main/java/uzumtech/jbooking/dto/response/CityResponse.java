package uzumtech.jbooking.dto.response;

public record CityResponse(
        Long cityId,
        String name,
        String country,
        String timezone
) {
}
