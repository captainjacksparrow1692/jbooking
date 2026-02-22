package uzumtech.jbooking.dto.request;

import uzumtech.jbooking.constant.enums.AccommodationType;

import java.time.LocalDate;

public record HotelSearchRequest(
        String country,
        Long cityId,

        LocalDate checkIn,
        LocalDate checkOut,

        Double minRating,
        AccommodationType accommodationType,
        Integer guestsCount,

) {}