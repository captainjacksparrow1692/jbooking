package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import uzumtech.jbooking.constant.enums.AccommodationType;

public record HotelSearchRequest(
        @NotNull(message = "City ID is required")
        Long cityId,

        String name,

        Double minRating,

        AccommodationType accommodationType
) {}