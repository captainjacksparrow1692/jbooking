package uzumtech.jbooking.dto.response;

import uzumtech.jbooking.constant.enums.AccommodationType;

import java.math.BigDecimal;

public record HotelSearchResponse(
        Long id,
        Long cityId,
        String name,
        String country,
        String city,
        String address,
        AccommodationType accommodationType,
        Double averageRating,
        Integer reviewsCount,
        BigDecimal minPricePerNight,
        String description,
        String brand,
        String amenities
) {}
