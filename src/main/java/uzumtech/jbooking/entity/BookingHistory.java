package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.*;
import uzumtech.jbooking.constant.enums.BookingStatus;
import uzumtech.jbooking.constant.enums.HistoryActionType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "booking_history")
public class BookingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    Booking booking;

    @Enumerated(EnumType.STRING)
    HistoryActionType historyActionType;

    @Enumerated(EnumType.STRING)
    BookingStatus bookingStatus;

    LocalDateTime actionTimestamp;
    String details;
}