package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uzumtech.jbooking.entity.City;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByNameContainsIgnoreCase(String name);

    List<City> findByCountryIgnoreCase(String country);

    @Query("SELECT DISTINCT c.country FROM City c")
    Page<String> findAllCountries(Pageable pageable);

    @Query("SELECT DISTINCT h.city FROM Hotel h")
    Page<City> findCitiesWithHotels(Pageable pageable);
}