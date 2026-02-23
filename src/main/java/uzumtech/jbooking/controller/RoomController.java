package uzumtech.jbooking.controller;

import jakarta.validation.Valid;
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

    //инфа комнаты что там и как
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getById(@PathVariable Long id) {
        log.info("REST request to get room: {}", id);
        return ResponseEntity.ok(roomService.getById(id));
    }

    //поиск комнаты
    @PostMapping("/search")
    public ResponseEntity<Page<RoomResponse>> searchRooms(
            @Valid @RequestBody RoomSearchRequest request,
            Pageable pageable) {
        log.info("REST request to search rooms with criteria: {}", request);
        return ResponseEntity.ok(roomService.searchRooms(request, pageable));
    }
}