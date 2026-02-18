package uzumtech.jbooking.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzumtech.jbooking.dto.request.HotelCreateRequest;
import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelResponse;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.service.HotelService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/hotels")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HotelController {

    HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelResponse> create(@Valid @RequestBody HotelCreateRequest request) {
        log.info("REST request to create hotel: {}", request);
        return new ResponseEntity<>(hotelService.createHotel(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getById(@PathVariable Long id) {
        log.info("REST request to get hotel by id: {}", id);
        return ResponseEntity.ok(hotelService.getById(id));
    }

    // Поиск отелей со свободными номерами (POST, так как параметров поиска много)
    @PostMapping("/search")
    public ResponseEntity<Page<HotelSearchResponse>> search(
            @Valid @RequestBody HotelSearchRequest request,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("REST request to search available hotels: {}", request);
        return ResponseEntity.ok(hotelService.searchHotel(request, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> update(@PathVariable Long id, @RequestParam Double rating) {
        log.info("REST request to update hotel by id: {}", id);
        hotelService.updateRating(id, rating);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HotelResponse> delete(@PathVariable Long id) {
        log.info("REST request to delete hotel by id: {}", id);
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
