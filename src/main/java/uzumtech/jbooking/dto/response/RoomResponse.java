package uzumtech.jbooking.dto.response;

import uzumtech.jbooking.constant.enums.BoardBasis;
import uzumtech.jbooking.constant.enums.CancellationPoliceType;
import uzumtech.jbooking.constant.enums.RoomAvailabilityStatus;

import java.math.BigDecimal;

public record RoomResponse(
        Long id,
        String roomNumber,
        BigDecimal pricePerNight,
        Integer capacity,
        BoardBasis boardBasis,
        CancellationPoliceType cancellationPoliceType,
        RoomAvailabilityStatus roomAvailabilityStatus,
        String description
){
}
