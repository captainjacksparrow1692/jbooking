package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uzumtech.jbooking.constant.enums.PaymentStatus;
import uzumtech.jbooking.constant.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "payments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long bookingId;
    BigDecimal amount;


    @Enumerated(EnumType.STRING)
    PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    LocalDateTime paymentDate;
}
