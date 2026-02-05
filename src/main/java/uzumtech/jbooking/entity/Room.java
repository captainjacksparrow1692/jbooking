package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uzumtech.jbooking.constant.enums.BoardBasis;
import uzumtech.jbooking.constant.enums.CancellationPoliceType;
import uzumtech.jbooking.constant.enums.RoomAvailabilityStatus;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String roomNumber;
    BigDecimal price;
    Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    Hotel hotel;

    @Enumerated(EnumType.STRING)
    BoardBasis boardBasis;

    @Enumerated(EnumType.STRING)
    CancellationPoliceType  cancellationPoliceType;

    @Enumerated(EnumType.STRING)
    RoomAvailabilityStatus  roomAvailabilityStatus;
}
