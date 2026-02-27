package uzumtech.jbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uzumtech.jbooking.entity.Booking;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    // Проверка доступности комнаты
    @Query("""
    SELECT CASE WHEN COUNT(b) = 0 THEN true ELSE false END
    FROM Booking b
    WHERE b.room.id = :roomId
      AND b.bookingStatus <> uzumtech.jbooking.constant.enums.BookingStatus.CANCELLED
      AND (
          :checkIn < b.checkOutDate AND
          :checkOut > b.checkInDate
      )
      AND (
          b.bookingStatus <> uzumtech.jbooking.constant.enums.BookingStatus.HOLD
          OR b.holdUntil IS NULL
          OR b.holdUntil > CURRENT_TIMESTAMP
      )
    """)
    boolean isRoomAvailable(@Param("roomId") UUID roomId,
                            @Param("checkIn") LocalDate checkIn,
                            @Param("checkOut") LocalDate checkOut);

    // Найти бронь пользователя
    Optional<Booking> findByIdAndUserId(UUID id, UUID userId);
}
