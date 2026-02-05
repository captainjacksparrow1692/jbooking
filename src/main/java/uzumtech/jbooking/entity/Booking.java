package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uzumtech.jbooking.constant.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    Room room;

    @Column(name = "user_id")
    Long userId;

    LocalDate checkInDate;
    LocalDate checkOutDate;

    @Enumerated(EnumType.STRING)
    BookingStatus bookingStatus;

    LocalDateTime createAt;
}
