package uzumtech.jbooking.dto.response;

import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.constant.enums.PaymentType;
import uzumtech.jbooking.dto.GuestDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(
        Long bookingId,
        Long roomId,
        String hotelName,
        LocalDate checkIn,
        LocalDate checkOut,
        BigDecimal totalPrice,
        BookingStatus bookingStatus,
        PaymentType paymentType,
        List<GuestDto> guests,
        LocalDateTime createdAt
) {
}
