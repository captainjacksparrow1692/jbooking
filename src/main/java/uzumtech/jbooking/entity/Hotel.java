package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uzumtech.jbooking.constant.enums.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotels")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String name;
    String address;
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    City city;

    String brand;

    @Column(name = "average_rating")
    Double averageRating;

    @Column(name = "reviews_count")
    Long reviewsCount;

    String amenities;

    @Enumerated(EnumType.STRING)
    AccommodationType accommodationType;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    List<Room> rooms;
}