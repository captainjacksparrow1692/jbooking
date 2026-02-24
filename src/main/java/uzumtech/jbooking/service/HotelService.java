package uzumtech.jbooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelSearchResponse;

public interface HotelService {

    // поиск отелей
    Page<HotelSearchResponse> searchHotel(HotelSearchRequest request, Pageable pageable);
}