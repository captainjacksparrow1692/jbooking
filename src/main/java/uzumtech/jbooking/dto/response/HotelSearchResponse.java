package uzumtech.jbooking.dto.response;

import uzumtech.jbooking.constant.enums.AccommodationType;
import uzumtech.jbooking.constant.enums.HotelBrand;

import java.math.BigDecimal;

public record HotelSearchResponse(
        Long id,
        String name,
        String country,
        String city,
        AccommodationType accommodationType,
        HotelBrand hotelBrand,
        Double averageRating,
        Integer reviewsCount,
        BigDecimal minPricePerNight,
        String description
) {
}
