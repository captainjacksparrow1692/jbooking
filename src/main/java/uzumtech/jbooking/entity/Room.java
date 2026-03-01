package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uzumtech.jbooking.constant.enums.BoardBasis;
import uzumtech.jbooking.constant.enums.CancellationPolicyType;
import uzumtech.jbooking.constant.enums.RoomAvailabilityStatus;
import uzumtech.jbooking.constant.enums.RoomType;

import java.math.BigDecimal;
import java.util.UUID;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String roomNumber;
    BigDecimal price;
    Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    Hotel hotel;

    @Enumerated(EnumType.STRING)
    BoardBasis boardBasis;

    @Enumerated(EnumType.STRING)
    RoomAvailabilityStatus roomAvailabilityStatus;

    @Enumerated(EnumType.STRING)
    CancellationPolicyType cancellationPolicyType;

    @Enumerated(EnumType.STRING)
    RoomType roomType;
}
