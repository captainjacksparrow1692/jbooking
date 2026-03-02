package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uzumtech.jbooking.constant.enums.BoardBasis;
import uzumtech.jbooking.constant.enums.CancellationPolicyType;
import uzumtech.jbooking.entity.Room;

import java.time.LocalDate;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    @Query("""
        SELECT r
        FROM Room r
        WHERE r.hotel.id = :hotelId
          AND (:guestsCount IS NULL OR r.capacity >= :guestsCount)
          AND (:boardBasis IS NULL OR r.boardBasis = :boardBasis)
          AND (:cancellationPolicyType IS NULL OR r.cancellationPolicyType = :cancellationPolicyType)
          AND NOT EXISTS (
              SELECT b
              FROM Booking b
              WHERE b.room = r
                AND b.bookingStatus <> uzumtech.jbooking.constant.enums.BookingStatus.CANCELLED
                AND :checkIn < b.checkOutDate
                AND :checkOut > b.checkInDate
          )
        """)
    Page<Room> searchRooms(
            @Param("hotelId") UUID hotelId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("guestsCount") Integer guestsCount,
            @Param("boardBasis") BoardBasis boardBasis,
            @Param("cancellationPolicyType") CancellationPolicyType cancellationPolicyType,
            Pageable pageable
    );
}