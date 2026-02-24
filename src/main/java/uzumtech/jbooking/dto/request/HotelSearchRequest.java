package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uzumtech.jbooking.constant.enums.AccommodationType;

import java.time.LocalDate;

public record HotelSearchRequest(
        String country,

        @NotNull(message = "City ID is required")
        Long cityId,

        @NotNull @FutureOrPresent
        LocalDate checkIn,

        @NotNull @Future
        LocalDate checkOut,

        Double minRating,
        AccommodationType accommodationType,

        @NotNull @Positive(message = "Guests count must be positive")
        Integer guestsCount
) {}