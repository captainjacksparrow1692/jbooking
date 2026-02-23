package uzumtech.jbooking.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.entity.City;
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.mapper.HotelMapper;
import uzumtech.jbooking.repository.CityRepository;
import uzumtech.jbooking.repository.HotelRepository;
import uzumtech.jbooking.service.HotelService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelServiceImpl implements HotelService {

    HotelRepository hotelRepository;
    CityRepository cityRepository;
    HotelMapper hotelMapper;

    @Override
    public List<HotelSearchResponse> searchHotel(HotelSearchRequest request) {

        cityRepository.findById(request.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));

        return hotelRepository.searchAvailableHotels(
                        request.cityId(),
                        request.checkIn(),
                        request.checkOut(),
                        request.minRating(),
                        request.accommodationType()
                )
                .stream()
                .map(hotelMapper::toHotelSearchResponse)
                .toList();
    }
}