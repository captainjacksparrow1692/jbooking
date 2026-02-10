package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uzumtech.jbooking.dto.request.CityCreateRequest;
import uzumtech.jbooking.dto.response.CityResponse;
import uzumtech.jbooking.entity.City;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @Mapping(target = "id", ignore = true)
    City toEntity(CityCreateRequest request);

    CityResponse toResponse(City city);
}
