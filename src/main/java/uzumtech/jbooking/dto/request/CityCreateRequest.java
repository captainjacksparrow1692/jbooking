package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CityCreateRequest(
        @NotBlank(message = "Name of city")
        String name,
        String country
) {
}
