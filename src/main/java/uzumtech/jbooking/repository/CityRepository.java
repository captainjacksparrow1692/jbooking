package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uzumtech.jbooking.entity.City;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    //поиск по названию
    List<City> findByNameContainsIgnoreCase(String name);

    //получить все города определенной страны
    List<City> findByCountryIgnoreCase(String country);

    //получить список уникальных стран
    @Query("SELECT DISTINCT c.country FROM City c")
    Page<City> findAllCountries(Pageable pageable);

    //поиск городов, где есть хотя бы 1 отель
    @Query("SELECT DISTINCT h.city FROM Hotel h")
    Page<String> findCitiesWithHotels(Pageable pageable);
}
