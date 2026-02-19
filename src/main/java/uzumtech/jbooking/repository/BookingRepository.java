package uzumtech.jbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uzumtech.jbooking.entity.Booking;

import java.time.LocalDate;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Проверка доступности комнаты
    @Query("""
        SELECT CASE WHEN COUNT(b) = 0 THEN true ELSE false END
        FROM Booking b
        WHERE b.room.id = :roomId
        AND b.bookingStatus <> 'CANCELLED'
        AND (
            :checkIn < b.checkOutDate AND
            :checkOut > b.checkInDate
        )
    """)
    boolean isRoomAvailable(@Param("roomId") Long roomId,
                            @Param("checkIn") LocalDate checkIn,
                            @Param("checkOut") LocalDate checkOut);

    // Найти бронь пользователя
    Optional<Booking> findByIdAndUserId(Long id, Long userId);
}
