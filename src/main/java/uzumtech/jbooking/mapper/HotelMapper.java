package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.entity.Hotel;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "city", source = "hotel.city.name")
    @Mapping(target = "country", source = "hotel.city.country")
    @Mapping(target = "minPricePerNight", ignore = true)
    HotelSearchResponse toHotelSearchResponse(Hotel hotel);
}