package uzumtech.jbooking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uzumtech.jbooking.constant.enums.PenaltyType;

@Entity
@Getter
@Setter
@Table(name = "cancellation_policy")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancellationPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    PenaltyType penaltyType;

    Double penaltyValue;
    Integer daysBeforeCancel; //за сколько дней можно отметить без штрафа
}
