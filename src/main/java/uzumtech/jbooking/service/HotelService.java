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

    Page<HotelResponse> getAll(Pageable pageable);

    //поиск по дате, гостям и условиям
    Page<HotelSearchResponse> searchHotel(HotelSearchRequest request, int page, int size);

    void delete(Long id);
}
