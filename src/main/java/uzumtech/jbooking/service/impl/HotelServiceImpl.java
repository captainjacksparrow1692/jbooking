package uzumtech.jbooking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzumtech.jbooking.dto.request.HotelCreateRequest;
import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelResponse;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.entity.City;
import uzumtech.jbooking.entity.Hotel;
import uzumtech.jbooking.mapper.HotelMapper;
import uzumtech.jbooking.repository.CityRepository;
import uzumtech.jbooking.repository.HotelRepository;
import uzumtech.jbooking.service.HotelService;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelServiceImpl implements HotelService {

    HotelRepository hotelRepository;
    CityRepository cityRepository;
    HotelMapper hotelMapper;

    @Override
    @Transactional
    public HotelResponse createHotel(HotelCreateRequest request) {
        // Проверяем существование города
        City cityEntity = cityRepository.findById(request.cityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));

        Hotel hotel = hotelMapper.toHotel(request);
        hotel.setCity(cityEntity);

        return hotelMapper.toHotelResponse(hotelRepository.save(hotel));
    }

    @Override
    public Page<HotelSearchResponse> searchHotel(HotelSearchRequest request, Pageable pageable) {

        return hotelRepository.findAvailableHotels(
                request.city(),
                request.checkIn().toLocalDate(),
                request.checkOut().toLocalDate(),
                pageable)
                .map(hotelMapper::toHotelSearchResponse);
    }

    @Override
    public HotelResponse getById(Long id) {
        return hotelRepository.findById(id)
                .map(hotelMapper::toHotelResponse)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
    }

    @Override
    @Transactional
    public void updateRating(Long hotelId, Double newRating) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        hotel.setAverageRating(newRating);
        hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        hotelRepository.deleteById(id);
    }
}