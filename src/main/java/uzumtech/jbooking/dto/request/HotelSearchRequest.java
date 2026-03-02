package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import uzumtech.jbooking.constant.enums.AccommodationType;

public record HotelSearchRequest(
        @NotBlank(message = "City name is required")
        String cityName,

        String name,

        Double minRating,

        AccommodationType accommodationType
) {}