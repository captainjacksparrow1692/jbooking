package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uzumtech.jbooking.entity.City;

import java.util.UUID;


public interface CityRepository extends JpaRepository<City, UUID> {

    @Query("SELECT c FROM City c WHERE LOWER(c.name) LIKE :name")
    Page<City> findByName(@Param("name") String name, Pageable pageable);
}