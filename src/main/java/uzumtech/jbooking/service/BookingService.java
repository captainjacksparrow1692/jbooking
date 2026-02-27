package uzumtech.jbooking.service;

import uzumtech.jbooking.dto.request.BookingCreateRequest;
import uzumtech.jbooking.dto.response.BookingResponse;

import java.util.UUID;

public interface BookingService {

    //создание брони
    BookingResponse create(BookingCreateRequest request);

    //только свою бронь
    BookingResponse getById(UUID userId, UUID bookingId);

    //отмена
    void cancelMyBooking(UUID userId, UUID bookingId);
}
