package uzumtech.jbooking.service;

import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelSearchResponse;

import java.util.List;

public interface HotelService {

    // поиск отелей со свободными номерами на даты
    List<HotelSearchResponse> searchHotel(HotelSearchRequest request);
}