package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.NotNull;
import uzumtech.jbooking.constant.enums.PaymentType;
import uzumtech.jbooking.dto.GuestDto;

import java.time.LocalDateTime;
import java.util.List;

public record BookingCreateRequest (

        @NotNull
        Long userId,

        @NotNull
        Long roomId,

        LocalDateTime checkIn,
        LocalDateTime checkOut,

        PaymentType paymentType,

        List<GuestDto> guests

){
}