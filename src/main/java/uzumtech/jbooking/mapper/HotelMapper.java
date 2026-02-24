package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uzumtech.jbooking.dto.response.HotelSearchResponse;
import uzumtech.jbooking.entity.Hotel;
import uzumtech.jbooking.entity.Room;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "city", source = "city.name")
    @Mapping(target = "country", source = "city.country")
    // Добавляем вычисление минимальной цены
    @Mapping(target = "minPricePerNight", source = "rooms", qualifiedByName = "calculateMinPrice")
    HotelSearchResponse toHotelSearchResponse(Hotel hotel);

    @Named("calculateMinPrice")
    default BigDecimal calculateMinPrice(List<Room> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            return null;
        }
        return rooms.stream()
                .map(Room::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(null);
    }
}