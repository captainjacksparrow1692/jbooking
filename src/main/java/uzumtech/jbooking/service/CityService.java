package uzumtech.jbooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.dto.request.CitySearchRequest;
import uzumtech.jbooking.dto.response.CityResponse;


public interface CityService {

    Page<CityResponse> searchCities(CitySearchRequest request, Pageable pageable);
}