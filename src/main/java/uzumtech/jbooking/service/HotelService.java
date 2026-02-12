package uzumtech.jbooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.dto.request.HotelCreateRequest;
import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelResponse;
import uzumtech.jbooking.dto.response.HotelSearchResponse;

public interface HotelService {
    HotelResponse createHotel(HotelCreateRequest request);

    HotelResponse getById(Long id);

    // поиск отелей со свободными номерами на даты
    Page<HotelSearchResponse> searchHotel(HotelSearchRequest request, Pageable pageable);

    void updateRating(Long hotelId, Double newRating); // Вызывается из ReviewService

    void delete(Long id);
}