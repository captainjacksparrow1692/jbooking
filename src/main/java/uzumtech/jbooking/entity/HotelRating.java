package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uzumtech.jbooking.constant.enums.RatingType;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "hotel_reviews")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long hotelId;
    Long userId;

    @ElementCollection
    @CollectionTable(name = "review_ratings", joinColumns = @JoinColumn(name = "review_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "score")
    Map<RatingType, Integer> score;

    String comment;
    LocalDateTime createdAt;
}
