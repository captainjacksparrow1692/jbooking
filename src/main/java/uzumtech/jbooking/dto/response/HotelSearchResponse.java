package uzumtech.jbooking.dto.response;

import uzumtech.jbooking.constant.enums.AccommodationType;

import java.math.BigDecimal;
import java.util.UUID;

public record HotelSearchResponse(
        UUID id,
        UUID cityId,
        String name,
        String country,
        String city,
        String address,
        AccommodationType accommodationType,
        Double averageRating,
        Long reviewsCount,
        BigDecimal minPricePerNight,
        String description,
        String brand,
        String amenities
) {}
