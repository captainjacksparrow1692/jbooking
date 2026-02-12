package uzumtech.jbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uzumtech.jbooking.constant.enums.RatingType;
import uzumtech.jbooking.dto.response.ReviewsResponse;
import uzumtech.jbooking.entity.HotelReview;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "averageScore", source = "scores", qualifiedByName = "calculateAverage")
    @Mapping(target = "userName", constant = "Guest")
    @Mapping(target = "score", source = "scores")
    ReviewsResponse toResponse(HotelReview hotelReview);

    @Named("calculateAverage")
    default Double calculateAverage(Map<RatingType, Integer> scores) {
        if (scores == null || scores.isEmpty()) return 0.0;
        return scores.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
}