package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uzumtech.jbooking.dto.request.HotelCreateRequest;
import uzumtech.jbooking.dto.response.HotelResponse;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.entity.Hotel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {

    @Mapping(target = "city", ignore = true) // Город ищется в БД по ID в сервисе
    Hotel toHotel(HotelCreateRequest request);

    @Mapping(target = "city", source = "city.name")
    @Mapping(target = "country", source = "city.country")
    HotelSearchResponse toHotelSearchResponse(Hotel hotel);

    @Mapping(target = "cityName", source = "city.name")
    @Mapping(target = "availableRooms", source = "rooms")
    HotelResponse toHotelResponse(Hotel hotel);
}