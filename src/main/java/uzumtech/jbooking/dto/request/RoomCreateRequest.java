package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uzumtech.jbooking.constant.enums.*;

import java.math.BigDecimal;

public record RoomCreateRequest(

        @NotNull(message = "Hotel ID is required")
        Long hotelId,

        @NotBlank(message = "Room number is required")
        String roomNumber,

        @Positive(message = "Price must be positive")
        BigDecimal price,

        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacity,

        @NotNull(message = "Board basis is required")
        BoardBasis boardBasis, // RO, BB, HB, FB, AI

        @NotNull(message = "Cancellation policy is required")
        CancellationPoliceType cancellationPolicy, // FREE_CANCELLATION, NON_REFUNDABLE

        @NotNull(message = "Initial status is required")
        RoomAvailabilityStatus availabilityStatus, // Обычно AVAILABLE при создании
        String description
) {
}
