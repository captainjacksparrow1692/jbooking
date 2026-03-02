package uzumtech.jbooking.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record BookingCreateRequest (
        @NotNull(message = "User ID is required")
        UUID userId,

        @NotNull(message = "Room ID is required")
        UUID roomId,

        @NotNull @FutureOrPresent
        @JsonProperty("checkInDate")
        LocalDate checkInDate,

        @NotNull @Future
        @JsonProperty("checkOutDate")
        LocalDate checkOutDate,

        @NotNull @Positive
        Integer guestsCount
){}