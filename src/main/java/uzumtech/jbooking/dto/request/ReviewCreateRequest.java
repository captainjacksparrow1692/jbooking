package uzumtech.jbooking.dto.request;

import uzumtech.jbooking.constant.enums.RatingType;

import java.util.Map;

public record ReviewCreateRequest(

        Long hotelId,
        Long userId,
        String comment,
        Map<RatingType, Integer> score
) {
}
