package uzumtech.jbooking.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzumtech.jbooking.dto.request.CitySearchRequest;
import uzumtech.jbooking.dto.response.CityResponse;
import uzumtech.jbooking.service.CityService;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CityController {

    CityService cityService;

    //api/v1/cities/search?name=Tashkent
    @GetMapping("/search")
    public ResponseEntity<Page<CityResponse>> search(
            @Valid @ModelAttribute CitySearchRequest request,
            Pageable pageable) {
        return ResponseEntity.ok(cityService.searchCities(request, pageable));
    }
}