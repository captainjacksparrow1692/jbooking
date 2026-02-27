package uzumtech.jbooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.dto.request.RoomSearchRequest;
import uzumtech.jbooking.dto.response.RoomResponse;

import java.util.UUID;

public interface RoomService {
    RoomResponse getById(UUID id);

    // Поиск комнат по фильтрам пользователя (даты, питание и т.д.)
    Page<RoomResponse> searchRooms(RoomSearchRequest request, Pageable pageable);
}