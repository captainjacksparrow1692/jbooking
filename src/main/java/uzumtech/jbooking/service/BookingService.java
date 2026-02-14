package uzumtech.jbooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.request.BookingStatusUpdateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;

public interface BookingService {

    //создание брони и запись в букингхистори
    BookingResponse create(BookingCreateRequest request);

    //возврат букингреспонса
    BookingResponse getById(Long id);

    // Каждое обновление статуса теперь создает новую запись в историю
    void updateStatus(BookingStatusUpdateRequest request);

    //отмена
    void cancel(Long id);

    Page<BookingResponse> getAll(Pageable pageable);

    Page<BookingResponse> getByUserId(Long userId, Pageable pageable);
}
