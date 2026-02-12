package uzumtech.jbooking.dto.request;

import uzumtech.jbooking.constant.enums.BoardBasis;
import uzumtech.jbooking.constant.enums.CancellationPolicyType;

import java.time.LocalDate;

public record RoomSearchRequest(
        Long hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        Integer guestsCount,
        BoardBasis boardBasis,
        CancellationPolicyType cancellationPolicyType
) {

}
