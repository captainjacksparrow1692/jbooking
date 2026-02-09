package uzumtech.jbooking.dto.request;

import uzumtech.jbooking.constant.enums.AccommodationType;
import uzumtech.jbooking.constant.enums.HotelBrand;

import java.time.LocalDateTime;

public record HotelSearchRequest(

        String country,
        String city,

        LocalDateTime checkIn,
        LocalDateTime checkOut,

        Double minRating,
        AccommodationType accommodationType,
        HotelBrand hotelBrand
) {
}