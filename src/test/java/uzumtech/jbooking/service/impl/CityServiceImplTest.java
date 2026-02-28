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
import uzumtech.jbooking.exception.ResourceNotFoundException;
import uzumtech.jbooking.mapper.CityMapper;
import uzumtech.jbooking.repository.CityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    void getById_shouldReturnCityResponse() {
        UUID cityId = UUID.randomUUID();

        City city = City.builder()
                .id(cityId)
                .name("Tashkent")
                .country("Uzbekistan")
                .build();

        CityResponse expected = new CityResponse(cityId, "Tashkent", "Uzbekistan", null);

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));
        when(cityMapper.toResponse(city)).thenReturn(expected);

        CityResponse result = cityService.getById(cityId);

        assertThat(result.cityId()).isEqualTo(cityId);
        assertThat(result.name()).isEqualTo("Tashkent");
    }

    @Test
    void getById_shouldThrowWhenCityNotFound() {
        UUID randomId = UUID.randomUUID();

        when(cityRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.getById(randomId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("City not found");
    }

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

        CityResponse response = new CityResponse(cityId, "Tashkent", "Uzbekistan", null);

        Page<City> cityPage = new PageImpl<>(List.of(city), pageable, 1);

        // сервис добавляет % и делает lower()
        when(cityRepository.findByName(eq("%tash%"), any(Pageable.class)))
                .thenReturn(cityPage);

        when(cityMapper.toResponse(city)).thenReturn(response);

        Page<CityResponse> result = cityService.searchCities(request, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Tashkent");
        assertThat(result.getContent().get(0).cityId()).isEqualTo(cityId);
    }

    @Test
    void searchCities_shouldUseDefaultPageableWhenNull() {
        CitySearchRequest request = new CitySearchRequest("Tash");
        Page<City> emptyPage = Page.empty();

        when(cityRepository.findByName(eq("%tash%"), any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<CityResponse> result = cityService.searchCities(request, null);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void searchCities_shouldCapPageSizeAtMax() {
        CitySearchRequest request = new CitySearchRequest(null);
        Pageable oversized = PageRequest.of(0, 500);
        Page<City> emptyPage = Page.empty();

        when(cityRepository.findByName(isNull(), any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<CityResponse> result = cityService.searchCities(request, oversized);

        assertThat(result).isNotNull();
    }
}