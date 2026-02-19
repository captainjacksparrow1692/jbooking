package uzumtech.jbooking.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import uzumtech.jbooking.constant.enums.PaymentType;

import java.time.LocalDate;

public record BookingCreateRequest (
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
        PaymentType paymentType
){}