package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uzumtech.jbooking.constant.enums.AccommodationType;
import uzumtech.jbooking.entity.Hotel;

import java.time.LocalDate;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Получить все отели
    Page<Hotel> findAll(Pageable pageable);

    // Поиск по стране и городу с учетом регистра
    Page<Hotel> findByCountryIgnoreCaseAndCityIgnoreCase(String country, String city);

    // Фильтрация по звездам (рейтингу) и типу жилья
    Page<Hotel> findByAverageRatingGreaterThanEqualAndAccommodationType(Double rating, AccommodationType type);

    // Поиск отелей, у которых есть свободные комнаты на выбранные даты
    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.rooms r WHERE h.city = :city " +
            "AND r.id NOT IN (SELECT b.room.id FROM Booking b " +
            "WHERE b.checkInDate < :checkout AND b.checkOutDate > :checkin AND b.bookingStatus != 'CANCELLED')")
    Page<Hotel> findAvailableHotels(@Param("city") String city,
                                    @Param("checkin") LocalDate checkin,
                                    @Param("checkout") LocalDate checkout);

}
