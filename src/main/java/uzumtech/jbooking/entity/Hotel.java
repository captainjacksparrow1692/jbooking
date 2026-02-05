package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uzumtech.jbooking.constant.enums.*;

import java.util.Set;
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String address;
    String city;
    String description;

    @Enumerated(EnumType.STRING)
    AccommodationType accommodationType;

    @Enumerated(EnumType.STRING)
    HotelBrand brand;

    @ElementCollection(targetClass = Amenity.class)
    @CollectionTable(name = "hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "amenity")
    Set<Amenity> amenities;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    List<Room> rooms;
}