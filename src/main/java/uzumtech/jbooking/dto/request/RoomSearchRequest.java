package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uzumtech.jbooking.constant.enums.BoardBasis;
import uzumtech.jbooking.constant.enums.CancellationPolicyType;

import java.time.LocalDateTime;

public record RoomSearchRequest(

        @NotNull(message = "Hotel ID is required")
        Long hotelId,

        @NotNull @FutureOrPresent
        LocalDateTime checkIn,

        @NotNull @Future
        LocalDateTime checkOut,

        BoardBasis boardBasis,
        CancellationPolicyType cancellationPolicyType,

        @NotNull @Positive(message = "Guests count must be positive")
        Integer guestsCount
) {

}
