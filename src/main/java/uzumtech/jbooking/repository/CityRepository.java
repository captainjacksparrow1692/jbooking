package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uzumtech.jbooking.entity.City;

import java.util.UUID;


public interface CityRepository extends JpaRepository<City, UUID> {

    @Query("""
        SELECT c
        FROM City c
        WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:country IS NULL OR LOWER(c.country) LIKE LOWER(CONCAT('%', :country, '%')))
        """)
    Page<City> searchByNameAndCountry(
            @Param("name") String name,
            @Param("country") String country,
            Pageable pageable
    );
}