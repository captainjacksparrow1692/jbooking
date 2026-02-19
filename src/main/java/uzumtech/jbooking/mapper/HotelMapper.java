package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.entity.Hotel;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "city", source = "city.name") // если в Response есть поле String city
    @Mapping(target = "country", source = "city.country")
    HotelSearchResponse toHotelSearchResponse(Hotel hotel);
}