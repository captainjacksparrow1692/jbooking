package uzumtech.jbooking.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.constant.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingResponse(
        Long bookingId,

        String hotelName,
        String hotelAddress,

        Long roomId,
        String roomNumber,
        String roomType,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate checkIn,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate checkOut,

        BigDecimal totalPrice,

        BookingStatus bookingStatus,
        PaymentType paymentType,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        // Время, до которого держится бронь (если статус HOLD)
        LocalDateTime holdUntil
) {
}