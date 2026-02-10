package uzumtech.jbooking.dto.response;

import uzumtech.jbooking.constant.enums.AccommodationType;
import uzumtech.jbooking.constant.enums.HotelBrand;
import uzumtech.jbooking.constant.enums.RatingType;

import java.util.List;
import java.util.Map;

public record HotelResponse(
        Long id,
        String name,
        String description,
        String address,
        String cityName,
        Double averageRating,
        Map<RatingType, Integer> detailedRatings, // LOCATION: 4, CLEANLINESS: 5
        List<String> amenities,
        List<RoomResponse> availableRooms // Список доступных номеров
){
}
