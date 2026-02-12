package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uzumtech.jbooking.constant.enums.RoomAvailabilityStatus;
import uzumtech.jbooking.entity.Room;

import java.time.LocalDate;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findByHotelIdAndCapacityGreaterThanEqualAndRoomAvailabilityStatus(
            Long hotelId, Integer capacity, RoomAvailabilityStatus status, Pageable pageable);

    @Query("SELECT COUNT(b) = 0 FROM Booking b WHERE b.room.id = :roomId " +
            "AND b.checkInDate < :checkout AND b.checkOutDate > :checkin " +
            "AND b.bookingStatus != uzumtech.jbooking.constant.enums.BookingStatus.CANCELLED")
    boolean isRoomAvailable(@Param("roomId") Long roomId,
                            @Param("checkin") LocalDate checkin,
                            @Param("checkout") LocalDate checkout);
}