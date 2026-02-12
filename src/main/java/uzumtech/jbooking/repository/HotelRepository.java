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

    Page<Hotel> findByCityCountryIgnoreCaseAndCityNameIgnoreCase(String country, String cityName);

    Page<Hotel> findByAverageRatingGreaterThanEqualAndAccommodationType(Double rating, AccommodationType type);

    @Query("SELECT DISTINCT h FROM Hotel h JOIN h.rooms r WHERE h.city.name = :cityName " +
            "AND r.id NOT IN (SELECT b.room.id FROM Booking b " +
            "WHERE b.checkInDate < :checkout AND b.checkOutDate > :checkin " +
            "AND b.bookingStatus != uzumtech.jbooking.constant.enums.BookingStatus.CANCELLED)")
    Page<Hotel> findAvailableHotels(@Param("cityName") String cityName,
                                    @Param("checkin") LocalDate checkin,
                                    @Param("checkout") LocalDate checkout,
                                    Pageable pageable);
}