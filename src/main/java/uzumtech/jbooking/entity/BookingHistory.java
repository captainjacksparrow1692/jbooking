package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.constant.enums.HistoryActionType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking_history")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long bookingId;

    @Enumerated(EnumType.STRING)
    HistoryActionType historyActionType;

    LocalDateTime actionTimestamp;
    String details;
}
