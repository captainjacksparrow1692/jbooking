package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uzumtech.jbooking.constant.Constant;
import uzumtech.jbooking.dto.request.CitySearchRequest;
import uzumtech.jbooking.dto.response.CityResponse;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.mapper.CityMapper;
import uzumtech.jbooking.repository.CityRepository;
import uzumtech.jbooking.service.CityService;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CityServiceImpl implements CityService {

    CityRepository cityRepository;
    CityMapper cityMapper;

    @Override
    public CityResponse getById(Long id) {
        return cityRepository.findById(id)
                .map(cityMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
    }

    @Override
    public Page<CityResponse> searchCities(CitySearchRequest request, Pageable pageable) {
        log.info("Searching cities by request: {}", request);

        Pageable safePageable = pageable;
        if (safePageable == null || safePageable.isUnpaged()) {
            safePageable = PageRequest.of(0, Constant.DEFAULT_PAGE_SIZE);
        } else if (safePageable.getPageSize() > Constant.MAX_PAGE_SIZE) {
            safePageable = PageRequest.of(
                    safePageable.getPageNumber(),
                    Constant.MAX_PAGE_SIZE,
                    safePageable.getSort()
            );
        }

        return cityRepository.searchByNameAndCountry(
                        request.name(),
                        request.country(),
                        safePageable
                )
                .map(cityMapper::toResponse);
    }
}
