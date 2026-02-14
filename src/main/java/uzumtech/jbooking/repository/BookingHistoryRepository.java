package uzumtech.jbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uzumtech.jbooking.entity.BookingHistory;

import java.util.List;

public interface BookingHistoryRepository extends JpaRepository<BookingHistory, Long> {

    // Метод, чтобы найти всю историю по конкретной брони (от новых к старым)
    List<BookingHistory> findAllByBookingIdOrderByActionTimestampDesc(Long bookingId);
}
