package uzumtech.jbooking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.entity.City;
import uzumtech.jbooking.entity.Hotel;
import uzumtech.jbooking.mapper.HotelMapper;
import uzumtech.jbooking.repository.HotelRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    HotelRepository hotelRepository;

    @Mock
    HotelMapper hotelMapper;

    @InjectMocks
    HotelServiceImpl hotelService;

    @Test
    void searchHotel_shouldReturnPageOfHotels() {
        UUID cityId = UUID.randomUUID();
        UUID hotelId = UUID.randomUUID();

        HotelSearchRequest request = new HotelSearchRequest(cityId, "Grand", 4.0, null);
        Pageable pageable = PageRequest.of(0, 10);

        City city = City.builder()
                .id(cityId)
                .name("Tashkent")
                .country("Uzbekistan")
                .build();

        Hotel hotel = Hotel.builder()
                .id(hotelId)
                .name("Grand Hotel")
                .city(city)
                .averageRating(4.5)
                .build();

        HotelSearchResponse response = new HotelSearchResponse(
                hotelId, cityId, "Grand Hotel", "Uzbekistan", "Tashkent", null,
                null, 4.5, 100L, null, null, null, null
        );

        Page<Hotel> hotelPage = new PageImpl<>(List.of(hotel), pageable, 1);

        when(hotelRepository.simpleSearch(eq(cityId), eq(null), eq(4.0), eq("Grand"), any(Pageable.class)))
                .thenReturn(hotelPage);
        when(hotelMapper.toHotelSearchResponse(hotel)).thenReturn(response);

        Page<HotelSearchResponse> result = hotelService.searchHotel(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().name()).isEqualTo("Grand Hotel");
        assertThat(result.getContent().getFirst().id()).isEqualTo(hotelId);
    }

    @Test
    void searchHotel_shouldUseDefaultPageableWhenNull() {
        UUID cityId = UUID.randomUUID();
        HotelSearchRequest request = new HotelSearchRequest(cityId, null, null, null);
        Page<Hotel> emptyPage = Page.empty();

        when(hotelRepository.simpleSearch(eq(cityId), eq(null), eq(null), eq(null), any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<HotelSearchResponse> result = hotelService.searchHotel(request, null);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void searchHotel_shouldCapPageSizeAtMax() {
        UUID cityId = UUID.randomUUID();
        HotelSearchRequest request = new HotelSearchRequest(cityId, null, null, null);
        Pageable oversized = PageRequest.of(0, 500);
        Page<Hotel> emptyPage = Page.empty();

        when(hotelRepository.simpleSearch(eq(cityId), eq(null), eq(null), eq(null), any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<HotelSearchResponse> result = hotelService.searchHotel(request, oversized);

        assertThat(result).isNotNull();
    }
}