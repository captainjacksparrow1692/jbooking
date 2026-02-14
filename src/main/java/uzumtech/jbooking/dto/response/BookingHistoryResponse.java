package uzumtech.jbooking.dto.response;

import uzumtech.jbooking.constant.enums.HistoryActionType;

import java.time.LocalDateTime;

public record BookingHistoryResponse(
        HistoryActionType historyActionType,
        LocalDateTime actionTimestamp,
        String details
) {
}
