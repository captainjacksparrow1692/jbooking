package uzumtech.jbooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.dto.request.RoomCreateRequest;
import uzumtech.jbooking.dto.response.RoomResponse;

public interface RoomService {

    RoomResponse create(RoomCreateRequest request);

    RoomResponse getById(Long id);

    void delete(Long id);

    Page<RoomResponse> getRoomsByHotel(Long hotelId, Pageable pageable);
}
