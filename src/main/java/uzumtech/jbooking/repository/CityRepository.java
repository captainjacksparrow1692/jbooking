package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzumtech.jbooking.entity.City;


public interface CityRepository extends JpaRepository<City, Long> {
    // Поиск городов по названию (для автокомплита поиска)
    Page<City> findByNameContainingIgnoreCase(String name,  Pageable pageable);
}