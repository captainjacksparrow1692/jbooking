package uzumtech.jbooking.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import uzumtech.jbooking.constant.enums.RatingType;

import java.util.Map;

public record ReviewCreateRequest(

        Long hotelId,
        Long userId,
        String comment,
        Map<RatingType, @Min(1) @Max(5) Integer> score
) {
}
