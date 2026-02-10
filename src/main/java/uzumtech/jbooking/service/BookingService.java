package uzumtech.jbooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.request.BookingStatusUpdateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;

public interface BookingService {

    // Создание бронирования (с проверкой дат и вместимости)
    BookingResponse create(BookingCreateRequest request);

    // Получение деталей конкретной брони
    BookingResponse getById(Long id);

    // отмена
    void cancel(Long id);

    // получение всех бронирований
    Page<BookingResponse> getAll(Pageable pageable);

    // Обновление статуса (CONFIRMED, CANCELLED и т.д.)
    void updateStatus(BookingStatusUpdateRequest request);

    // Пагинация для списка бронирований пользователя или отеля
    Page<BookingResponse> getPagedBookings(int page, int size);
}
