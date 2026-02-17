package uzumtech.jbooking.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import uzumtech.jbooking.constant.enums.PaymentType;
import uzumtech.jbooking.dto.GuestDto;

import java.time.LocalDate;
import java.util.List;

public record BookingCreateRequest (
        @NotNull
        Long userId,

        @NotNull
        Long roomId,

        @JsonProperty("checkInDate")
        LocalDate checkInDate,

        @JsonProperty("checkOutDate")
        LocalDate checkOutDate,

        PaymentType paymentType,

        List<GuestDto> guests
){}