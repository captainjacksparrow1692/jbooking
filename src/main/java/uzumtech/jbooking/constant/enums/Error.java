package uzumtech.jbooking.constant.enums;

import lombok.Getter;

@Getter
public enum Error {
    // --- СИСТЕМНЫЕ ОШИБКИ (10xxx) ---
    INTERNAL_SERVICE_ERROR_CODE(10001, "System not available"),
    EXTERNAL_SERVICE_FAILED_ERROR_CODE(10002, "External service not available"),
    HANDLER_NOT_FOUND_ERROR_CODE(10003, "Handler not found"),
    JSON_NOT_VALID_ERROR_CODE(10004, "Json not valid"),
    VALIDATION_ERROR_CODE(10005, "Validation error"),
    INVALID_REQUEST_PARAM_ERROR_CODE(10006, "Invalid request param"),
    INTERNAL_TIMEOUT_ERROR_CODE(10007, "Internal timeout"),
    METHOD_NOT_SUPPORTED_ERROR_CODE(10008, "Method not supported"),
    MISSING_REQUEST_HEADER_ERROR_CODE(10009, "Missing request header"),
    HTTP_SERVICE_ERROR_CODE(10010, "Service error code"),
    HTTP_CLIENT_ERROR_CODE(10011, "Client error code"),

    // --- ОШИБКИ ПОИСКА (РЕСУРСЫ) (20xxx) ---
    CITY_NOT_FOUND_ERROR_CODE(20001, "City not found"),
    HOTEL_NOT_FOUND_ERROR_CODE(20002, "Hotel not found"),
    ROOM_NOT_FOUND_ERROR_CODE(20003, "Room not found"),
    BOOKING_NOT_FOUND_ERROR_CODE(20004, "Booking record not found"),
    USER_NOT_FOUND_ERROR_CODE(20005, "User not found"),

    // --- БИЗНЕС-ЛОГИКА БРОНИРОВАНИЯ (30xxx) ---
    ROOM_ALREADY_BOOKED_ERROR_CODE(30001, "Room is already occupied for these dates"),
    INVALID_BOOKING_DATES_ERROR_CODE(30002, "Check-in createdAt must be before check-out createdAt"),
    PAST_DATE_BOOKING_ERROR_CODE(30003, "Cannot book a room for past dates"),
    GUEST_COUNT_EXCEEDED_ERROR_CODE(30004, "Guest count exceeds room capacity"),
    BOOKING_ALREADY_CANCELLED_ERROR_CODE(30005, "This booking is already cancelled"),
    BOOKING_HOLD_EXPIRED_ERROR_CODE(30006, "Booking hold time has expired"),
    CANCELLATION_NOT_ALLOWED_ERROR_CODE(30007, "Cancellation is not allowed according to the hotel policy"),
    INVALID_BOOKING_STATUS_ERROR_CODE(30008, "Booking status does not allow this operation"),

    // --- ОШИБКИ ОПЛАТЫ (40xxx) ---
    PAYMENT_AMOUNT_MISMATCH_ERROR_CODE(40001, "Paid amount does not match booking price"),
    PAYMENT_REJECTED_ERROR_CODE(40002, "Payment was rejected by bank"),
    INSUFFICIENT_FUNDS_ERROR_CODE(40003, "Insufficient funds on card"),
    REFUND_NOT_POSSIBLE_ERROR_CODE(40004, "Refund is not possible for this booking status"),
    DUPLICATE_PAYMENT_ERROR_CODE(40005, "Payment for this booking already processed");

    final int code;
    final String message;

    Error(int code, String message) {
        this.code = code;
        this.message = message;
    }
}