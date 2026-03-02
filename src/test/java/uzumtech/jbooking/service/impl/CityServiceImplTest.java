package uzumtech.jbooking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import uzumtech.jbooking.dto.request.CitySearchRequest;
import uzumtech.jbooking.dto.response.CityResponse;
import uzumtech.jbooking.entity.City;
import uzumtech.jbooking.mapper.CityMapper;
import uzumtech.jbooking.repository.CityRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {

    @Mock
    CityRepository cityRepository;

    @Mock
    CityMapper cityMapper;

    @InjectMocks
    CityServiceImpl cityService;

    @Test
    void searchCities_shouldReturnPageOfCities() {
        UUID cityId = UUID.randomUUID();
        CitySearchRequest request = new CitySearchRequest("Tash");
        Pageable pageable = PageRequest.of(0, 10);

        City city = City.builder()
                .id(cityId)
                .name("Tashkent")
                .country("Uzbekistan")
                .build();

        CityResponse response = new CityResponse("Tashkent", "Uzbekistan", null, city.getTimezone());

        Page<City> cityPage = new PageImpl<>(List.of(city), pageable, 1);

        when(cityRepository.findByName(eq("%tash%"), any(Pageable.class))).thenReturn(cityPage);
        when(cityMapper.toResponse(city)).thenReturn(response);

        Page<CityResponse> result = cityService.searchCities(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Tashkent");
        assertThat(result.getContent().get(0).country()).isEqualTo("Uzbekistan");
    }

    @Test
    void searchCities_shouldUseDefaultPageableWhenNull() {
        CitySearchRequest request = new CitySearchRequest("Tash");
        Page<City> emptyPage = Page.empty();

        when(cityRepository.findByName(eq("%tash%"), any(Pageable.class))).thenReturn(emptyPage);

        Page<CityResponse> result = cityService.searchCities(request, null);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void searchCities_shouldReturnAllCitiesWhenNameIsNull() {
        CitySearchRequest request = new CitySearchRequest(null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<City> emptyPage = Page.empty();

        when(cityRepository.findByName(eq("%"), any(Pageable.class))).thenReturn(emptyPage);

        Page<CityResponse> result = cityService.searchCities(request, pageable);

        assertThat(result).isNotNull();
    }

    @Test
    void searchCities_shouldReturnAllCitiesWhenNameIsBlank() {
        CitySearchRequest request = new CitySearchRequest("   ");
        Pageable pageable = PageRequest.of(0, 10);
        Page<City> emptyPage = Page.empty();

        when(cityRepository.findByName(eq("%"), any(Pageable.class))).thenReturn(emptyPage);

        Page<CityResponse> result = cityService.searchCities(request, pageable);

        assertThat(result).isNotNull();
    }

    @Test
    void searchCities_shouldCapPageSizeAtMax() {
        CitySearchRequest request = new CitySearchRequest("Tash");
        Pageable oversized = PageRequest.of(0, 100);
        Page<City> emptyPage = Page.empty();

        when(cityRepository.findByName(eq("%tash%"), any(Pageable.class))).thenReturn(emptyPage);

        Page<CityResponse> result = cityService.searchCities(request, oversized);

        assertThat(result).isNotNull();
    }
}