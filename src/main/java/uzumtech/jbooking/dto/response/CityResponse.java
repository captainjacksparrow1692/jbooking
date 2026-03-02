package uzumtech.jbooking.dto.response;

public record CityResponse(
        String name,
        String country,
        String description,
        String timezone
) {
}
