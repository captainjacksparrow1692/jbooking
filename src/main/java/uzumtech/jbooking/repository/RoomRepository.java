package uzumtech.jbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzumtech.jbooking.entity.Room;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // Пагинация важна для пользователя, чтобы не грузить 100+ номеров сразу
    Page<Room> findByHotelId(Long hotelId, Pageable pageable);
}