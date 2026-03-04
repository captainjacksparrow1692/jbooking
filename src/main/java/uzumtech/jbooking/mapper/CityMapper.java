package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import uzumtech.jbooking.dto.response.CityResponse;
import uzumtech.jbooking.entity.City;

@Mapper(componentModel = "spring")
public interface CityMapper {

    // Из Entity в Response
    CityResponse toResponse(City city);
}