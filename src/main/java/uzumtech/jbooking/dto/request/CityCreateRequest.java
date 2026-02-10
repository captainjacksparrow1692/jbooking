package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CityCreateRequest(
        @NotBlank(message = "City name is required")
        String name,

        @NotBlank(message = "Country name is required")
        String country,

        @Size(max = 1000, message = "Description is too long")
        String description,

        String timezone
) {
}
