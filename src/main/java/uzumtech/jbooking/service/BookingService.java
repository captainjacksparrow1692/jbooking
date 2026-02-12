package uzumtech.jbooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.request.BookingStatusUpdateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;

public interface BookingService {

    // Создание бронирования (с проверкой дат и вместимости)
    BookingResponse create(BookingCreateRequest request);

    BookingResponse getById(Long id);

    // Обновление статуса
    void updateStatus(BookingStatusUpdateRequest request);

    void cancel(Long id);

    Page<BookingResponse> getAll(Pageable pageable);

    Page<BookingResponse> getByUserId(Long userId, Pageable pageable);
}
