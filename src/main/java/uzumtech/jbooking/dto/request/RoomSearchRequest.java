package uzumtech.jbooking.dto.request;

import uzumtech.jbooking.constant.enums.BoardBasis;
import uzumtech.jbooking.constant.enums.CancellationPolicyType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RoomSearchRequest(
        Long hotelId,
        LocalDateTime checkIn,
        LocalDateTime checkOut,
        Integer guestsCount,
        BoardBasis boardBasis,
        CancellationPolicyType cancellationPolicyType
) {

}
