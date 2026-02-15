package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.dto.request.CityCreateRequest;
import uzumtech.jbooking.dto.response.CityResponse;
import uzumtech.jbooking.entity.City;
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
    @Transactional
    public CityResponse create(CityCreateRequest request) {
        City city = cityMapper.toEntity(request);
        return cityMapper.toResponse(cityRepository.save(city));
    }

    @Override
    public CityResponse getById(Long id) {
        return  cityRepository.findById(id)
                .map(cityMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
    }

    @Override
    public Page<CityResponse> getAll(Pageable pageable) {
        return cityRepository.findAll(pageable).map(cityMapper::toResponse);
    }
}
