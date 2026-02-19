package uzumtech.jbooking.service;

import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;

public interface BookingService {

    //создание брони и запись в букингхистори
    BookingResponse create(BookingCreateRequest request);

    //только свою бронь
    BookingResponse getById(Long userId, Long bookingId);

    //отмена
    void cancelMyBooking(Long userId, Long bookingId);
}
