package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.entity.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByUserId(Long userId, Pageable pageable);

    Page<Booking> findByRoomId(Long roomId, Pageable pageable);

    Page<Booking> findByRoomAvailabilityStatus(Long roomAvailabilityStatus, Pageable pageable);

    Page<Booking> findByBookingStatus(Long bookingStatus, Pageable pageable);

    List<Booking> findByUserIdAndBookingStatus(Long userId, BookingStatus bookingStatus);

    boolean existsByUserIdAndRoomIdAndBookingStatus (Long userId, Long roomId, BookingStatus status);
}
