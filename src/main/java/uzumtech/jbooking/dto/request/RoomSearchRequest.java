package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uzumtech.jbooking.constant.enums.BoardBasis;
import uzumtech.jbooking.constant.enums.CancellationPolicyType;

import java.time.LocalDate;
import java.util.UUID;

public record RoomSearchRequest(

        @NotNull(message = "Hotel ID is required")
        UUID hotelId,

        @NotNull @FutureOrPresent
        LocalDate checkIn,

        @NotNull @Future
        LocalDate checkOut,

        BoardBasis boardBasis,
        CancellationPolicyType cancellationPolicyType,

        @NotNull @Positive(message = "Guests count must be positive")
        Integer guestsCount
) {

}
