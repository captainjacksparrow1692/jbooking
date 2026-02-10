package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uzumtech.jbooking.dto.request.HotelCreateRequest;
import uzumtech.jbooking.dto.request.HotelSearchRequest;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.entity.Hotel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {

    @Mapping(target = "city", ignore = true)
    Hotel toHotel(HotelCreateRequest request);

    @Mapping(target = "city", source = "city.name")
    @Mapping(target = "country", source = "city.country")
    @Mapping(target = "averageRating", source = "averageRating")
    HotelSearchResponse toHotelSearchResponse(HotelSearchRequest request);

}
