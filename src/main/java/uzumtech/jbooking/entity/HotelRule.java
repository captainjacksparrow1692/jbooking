package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uzumtech.jbooking.constant.enums.BookingRuleType;

@Entity
@Getter
@Setter
@Table(name = "hotel_rules")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Hotel hotel;

    @Enumerated(EnumType.STRING)
    BookingRuleType  bookingRuleType;
    String ruleValue;
}
