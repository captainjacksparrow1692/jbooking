package uzumtech.jbooking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.service.BookingService;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/bookings")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookingController {

    BookingService bookingService;

    // создание брони
    @PostMapping
    public ResponseEntity<BookingResponse> create(
            @Valid @RequestBody BookingCreateRequest request) {

        log.info("REST request to create booking: {}", request);

        BookingResponse response = bookingService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // получить свою бронь
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getById(
            @RequestParam @NotNull UUID userId,
            @PathVariable @NotNull UUID bookingId) {

        log.info("REST request to get booking {} for user {}", bookingId, userId);

        return ResponseEntity.ok(
                bookingService.getById(userId, bookingId)
        );
    }

    // отменить свою бронь
    @DeleteMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelMyBooking(
            @RequestParam @NotNull UUID userId,
            @PathVariable @NotNull UUID bookingId) {

        log.info("REST request to cancel booking {} for user {}", bookingId, userId);

        bookingService.cancelMyBooking(userId, bookingId);

        return ResponseEntity.noContent().build();
    }
}
