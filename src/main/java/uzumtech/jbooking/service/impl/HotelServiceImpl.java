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
import uzumtech.jbooking.constant.enums.Error;
import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.exception.BookingValidationException;
import uzumtech.jbooking.exception.ResourceNotFoundException;
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
    public Page<HotelSearchResponse> searchHotel(HotelSearchRequest request, Pageable pageable) {

        cityRepository.findById(request.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));

        if (request.checkIn() != null && request.checkOut() != null
                && !request.checkIn().isBefore(request.checkOut())) {
            throw new BookingValidationException(
                    Error.INVALID_BOOKING_DATES_ERROR_CODE.getCode(),
                    Error.INVALID_BOOKING_DATES_ERROR_CODE.getMessage()
            );
        }

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

        return hotelRepository.searchAvailableHotels(
                        request.cityId(),
                        request.checkIn(),
                        request.checkOut(),
                        request.minRating(),
                        request.accommodationType(),
                        safePageable
                )
                .map(hotelMapper::toHotelSearchResponse);
    }
}