package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uzumtech.jbooking.constant.enums.ServiceType;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "hotel_services")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Hotel hotel;

    @Enumerated(EnumType.STRING)
    ServiceType serviceType;

    BigDecimal price;
    boolean isAvailable;
}
