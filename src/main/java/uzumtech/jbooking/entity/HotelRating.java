package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import uzumtech.jbooking.constant.enums.RatingType;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotel_reviews")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @ElementCollection
    @CollectionTable(name = "review_ratings", joinColumns = @JoinColumn(name = "review_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "score")
    Map<RatingType, Integer> scores;

    @Column(length = 1000)
    String comment;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;
}