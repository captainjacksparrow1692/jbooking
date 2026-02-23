package uzumtech.jbooking.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.service.HotelService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/hotels")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HotelController {

    HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelSearchResponse>> search(
            @Valid HotelSearchRequest request,
            Pageable pageable) {

        log.info("REST request to search hotels with criteria: {}", request);

        Page<HotelSearchResponse> results = hotelService.searchHotel(request, pageable);

        return ResponseEntity.ok(results);
    }
}