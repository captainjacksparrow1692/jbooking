package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import uzumtech.jbooking.constant.enums.BookingStatus;

public record BookingStatusUpdateRequest(

        @NotNull(message = "Booking Id is required")
        Long bookingId,

        @NotNull(message = "New status is required")
        BookingStatus bookingStatus,

        String reason
) {
}
