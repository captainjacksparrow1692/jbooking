package uzumtech.jbooking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.dto.request.CityCreateRequest;
import uzumtech.jbooking.dto.response.CityResponse;

public interface CityService {
    CityResponse create(CityCreateRequest request);
    CityResponse getById(Long id);
    Page<CityResponse> getAll(Pageable pageable);
}