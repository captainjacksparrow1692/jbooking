package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uzumtech.jbooking.constant.enums.AccommodationType;
import uzumtech.jbooking.entity.Hotel;

import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {

    @Query("""
            SELECT h
            FROM Hotel h
            WHERE LOWER(h.city.name) = LOWER(:cityName)
              AND (:accommodationType IS NULL OR h.accommodationType = :accommodationType)
              AND (:minRating IS NULL OR h.averageRating >= :minRating)
              AND (:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    Page<Hotel> simpleSearch(
            @Param("cityName") String cityName,
            @Param("accommodationType") AccommodationType accommodationType,
            @Param("minRating") Double minRating,
            @Param("name") String name,
            Pageable pageable
    );
}