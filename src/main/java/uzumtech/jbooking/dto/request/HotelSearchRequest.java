package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import uzumtech.jbooking.constant.enums.AccommodationType;

import java.util.UUID;

public record HotelSearchRequest(
        @NotNull(message = "City ID is required")
        UUID cityId,

        String name,

        Double minRating,

        AccommodationType accommodationType
) {}