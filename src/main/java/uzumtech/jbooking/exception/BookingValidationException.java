package uzumtech.jbooking.exception;

import org.springframework.http.HttpStatus;
import uzumtech.jbooking.constant.enums.ErrorType;

public class BookingValidationException extends BusinessException {
    public BookingValidationException(int code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST, ErrorType.BUSINESS);
    }
}
