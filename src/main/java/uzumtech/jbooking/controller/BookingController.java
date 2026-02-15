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
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.request.BookingStatusUpdateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;
import uzumtech.jbooking.service.BookingService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/bookings")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookingController {

    BookingService bookingService;

    //создание брони
    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingCreateRequest request) {
        log.info("REST request to create booking: {}", request);
        BookingResponse response = bookingService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //получить бронь по айди
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long id) {
        log.info("REST request to get booking by id: {}", id);
        return ResponseEntity.ok(bookingService.getById(id));
    }

    //изменение статуса
    @PatchMapping("/status")
    public ResponseEntity<Void> updateStatus(@Valid @RequestBody BookingStatusUpdateRequest request) {
        log.info("REST request to update booking status: {}", request);
        bookingService.updateStatus(request);
        return ResponseEntity.noContent().build();
    }

    //отмен брони
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancel(@PathVariable Long id) {
        log.info("REST request to cancel booking by id: {}", id);
        bookingService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    //получение всех броней
    @GetMapping
    public ResponseEntity<Page<BookingResponse>> getAll(
            @PageableDefault(size = 20)Pageable pageable) {
        log.info("REST request to get all bookings with pagination");
        return ResponseEntity.ok(bookingService.getAll(pageable));
    }

    //бронь конкретного пользователя
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BookingResponse>> getByUserId(
            @PathVariable Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("REST request to get bookings for user: {}", userId);
        return ResponseEntity.ok(bookingService.getByUserId(userId, pageable));
    }
}
