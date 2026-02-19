package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uzumtech.jbooking.dto.response.CityResponse;
import uzumtech.jbooking.entity.City;

@Mapper(componentModel = "spring")
public interface CityMapper {

    // Из Entity в Response (для клиента/фронтенда)
    @Mapping(target = "cityId", source = "id")
    CityResponse toResponse(City city);
}