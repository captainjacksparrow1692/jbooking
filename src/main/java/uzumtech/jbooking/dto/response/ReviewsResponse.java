package uzumtech.jbooking.dto.response;

import uzumtech.jbooking.constant.enums.RatingType;

import java.time.LocalDateTime;
import java.util.Map;

public record ReviewsResponse(
        Long id,
        String userName,
        String comment,
        Map<RatingType, Integer> score,
        Double averageScore,
        LocalDateTime createdAt
){
}
