package uzumtech.jbooking.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.service.HotelService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/hotels")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HotelController {

    HotelService hotelService;

    @PostMapping("/search")
    public ResponseEntity<List<HotelSearchResponse>> search(
            @Valid @RequestBody HotelSearchRequest request) {

        log.info("REST request to search hotels for city: {}", request.cityId());

        List<HotelSearchResponse> results = hotelService.searchHotel(request);

        return ResponseEntity.ok(results);
    }
}