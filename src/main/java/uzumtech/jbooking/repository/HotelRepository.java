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

    /**
     * Поиск отелей с доступными номерами по городу и датам.
     * Возвращает отели, у которых есть хотя бы один номер с достаточной вместимостью,
     * без пересекающихся бронирований на указанные даты.
     * Ограничения для продакшена:
     * - DISTINCT может быть дорогим при большом числе номеров; при росте нагрузки рассмотреть
     *   подзапрос или разделение на два запроса (сначала ID отелей, затем загрузка сущностей).
     * - При высокой нагрузке добавить индексы на (room_id, check_in_date, check_out_date) в bookings,
     *   на city_id в hotels, на hotel_id в rooms.
     */
    @Query("""
        SELECT DISTINCT h
        FROM Hotel h
            JOIN h.rooms r
        WHERE h.city.id = :cityId
          AND r.capacity >= :guestsCount
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
    Page<Hotel> searchAvailableHotels(
            @Param("cityId") Long cityId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("guestsCount") Integer guestsCount,
            @Param("minRating") Double minRating,
            @Param("accommodationType") AccommodationType accommodationType,
            Pageable pageable
    );
}