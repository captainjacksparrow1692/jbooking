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
import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.mapper.HotelMapper;
import uzumtech.jbooking.repository.HotelRepository;
import uzumtech.jbooking.service.HotelService;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelServiceImpl implements HotelService {

    HotelRepository hotelRepository;
    HotelMapper hotelMapper;

    @Override
    public Page<HotelSearchResponse> searchHotel(HotelSearchRequest request, Pageable pageable) {
        log.info("Simple hotel search initiated for cityId: {}", request.cityId());

        // 1. Упрощаем пагинацию (защита от слишком больших запросов)
        Pageable safePageable = getSafePageable(pageable);

        // 2. Вызываем упрощенный метод репозитория
        return hotelRepository.simpleSearch(
                        request.cityId(),
                        request.accommodationType(),
                        request.minRating(),
                        request.name(),
                        safePageable
                )
                .map(hotelMapper::toHotelSearchResponse);
    }

    private Pageable getSafePageable(Pageable pageable) {
        if (pageable == null || pageable.isUnpaged()) {
            return PageRequest.of(0, Constant.DEFAULT_PAGE_SIZE);
        }
        if (pageable.getPageSize() > Constant.MAX_PAGE_SIZE) {
            return PageRequest.of(
                    pageable.getPageNumber(),
                    Constant.MAX_PAGE_SIZE,
                    pageable.getSort()
            );
        }
        return pageable;
    }
}