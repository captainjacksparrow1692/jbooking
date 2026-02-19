package uzumtech.jbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uzumtech.jbooking.entity.Hotel;

import java.time.LocalDate;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Поиск всех отелей в конкретном городе
    List<Hotel> findByCityId(Long cityId, LocalDate checkIn,  LocalDate checkOut);
}