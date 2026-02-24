package uzumtech.jbooking.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingCreateRequest (
        @NotNull
        Long  bookingId,

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Room ID is required")
        Long roomId,

        @NotNull @FutureOrPresent
        @JsonProperty("checkInDate")
        LocalDate checkInDate,

        @NotNull @Future
        @JsonProperty("checkOutDate")
        LocalDate checkOutDate,

        @NotNull
        Integer guestsCount,

        @NotNull
        LocalDateTime createdAt
){}