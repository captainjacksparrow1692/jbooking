package uzumtech.jbooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.dto.request.RoomSearchRequest;
import uzumtech.jbooking.dto.response.RoomResponse;

public interface RoomService {
    RoomResponse getById(Long id);

    // Поиск комнат по фильтрам пользователя (даты, питание и т.д.)
    Page<RoomResponse> searchRooms(RoomSearchRequest request, Pageable pageable);
}