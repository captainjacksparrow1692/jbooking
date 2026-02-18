package uzumtech.jbooking.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzumtech.jbooking.dto.request.CityCreateRequest;
import uzumtech.jbooking.dto.response.CityResponse;
import uzumtech.jbooking.service.CityService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cities")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CityController {

    CityService  cityService;

    @PostMapping
    public ResponseEntity<CityResponse> createCity(@Valid @RequestBody CityCreateRequest cityCreateRequest) {
        log.info("REST  request to create a city : {}", cityCreateRequest);
        CityResponse response = cityService.create(cityCreateRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getById(@PathVariable Long id){
        log.info("REST request to get City : {}", id);
        return ResponseEntity.ok(cityService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CityResponse>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST  request to get all cities : {}", pageable);
        return ResponseEntity.ok(cityService.getAll(pageable));
    }
}
