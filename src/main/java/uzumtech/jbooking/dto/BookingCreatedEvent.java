package uzumtech.jbooking.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookingCreatedEvent(
        UUID bookingId,
        UUID roomId,
        UUID userId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer guestsCount,
        LocalDateTime createdAt
) {}