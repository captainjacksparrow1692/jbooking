package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingCreateRequest (

        @NotNull
        Long userId,

        @NotNull
        Long roomId,

        LocalDate checkIn,
        LocalDate checkOut,

        @Min(1)
        Integer guestCount
){
}