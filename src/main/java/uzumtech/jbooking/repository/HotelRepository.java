package uzumtech.jbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uzumtech.jbooking.constant.enums.AccommodationType;
import uzumtech.jbooking.entity.Hotel;

import java.time.LocalDate;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    //поиск по городу и датам
    @Query("""
        SELECT DISTINCT h
        FROM Hotel h
            JOIN h.rooms r
        WHERE h.city.id = :cityId
          AND (:accommodationType IS NULL OR h.accommodationType = :accommodationType)
          AND (:minRating IS NULL OR h.averageRating >= :minRating)
          AND NOT EXISTS (
              SELECT b
              FROM Booking b
              WHERE b.room = r
                AND b.bookingStatus <> uzumtech.jbooking.constant.enums.BookingStatus.CANCELLED
                AND :checkIn < b.checkOutDate
                AND :checkOut > b.checkInDate
          )
        """)
    List<Hotel> searchAvailableHotels(
            @Param("cityId") Long cityId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("minRating") Double minRating,
            @Param("accommodationType") AccommodationType accommodationType
    );
}