package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.entity.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByUserId(Long userId, Pageable pageable);

    @Query("""
        SELECT COUNT(b) = 0 
        FROM Booking b 
        WHERE b.room.id = :roomId 
        AND b.bookingStatus IN (uzumtech.jbooking.constant.enums.BookingStatus.CONFIRMED, 
                         uzumtech.jbooking.constant.enums.BookingStatus.HOLD)
        AND NOT (b.checkOutDate <= :checkIn OR b.checkInDate >= :checkOut)
    """)
    boolean isRoomAvailable(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    // Метод для автоматической отмены просроченных холдов (для планировщика)
    @Query("SELECT b FROM Booking b WHERE b.bookingStatus = 'HOLD' AND b.holdUntil < CURRENT_TIMESTAMP")
    List<Booking> findAllExpiredHolds();

    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
}