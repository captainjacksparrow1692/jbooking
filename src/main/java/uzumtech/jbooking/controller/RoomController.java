package uzumtech.jbooking.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzumtech.jbooking.dto.request.RoomSearchRequest;
import uzumtech.jbooking.dto.response.RoomResponse;
import uzumtech.jbooking.service.RoomService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomController {

    RoomService roomService;

    //получаем список комнат определенного отеля
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<Page<RoomResponse>> getRoomsByHotel(
            @PathVariable Long hotelId,
            Pageable pageable) {
        log.info("REST request to get rooms for hotel: {}", hotelId);
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId, pageable));
    }

    //инфа комнаты что там и как
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getById(@PathVariable Long id) {
        log.info("REST request to get room: {}", id);
        return ResponseEntity.ok(roomService.getById(id));
    }

    //поиск комнаты
    @PostMapping("/search")
    public ResponseEntity<Page<RoomResponse>> searchRooms(
            @RequestBody RoomSearchRequest request,
            Pageable pageable) {
        log.info("REST request to search rooms with criteria: {}", request);
        return ResponseEntity.ok(roomService.searchRooms(request, pageable));
    }
}