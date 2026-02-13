package uzumtech.jbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import uzumtech.jbooking.constant.enums.ErrorType;

public class InternalServerException extends  BusinessException {
    public InternalServerException(String message, HttpStatusCode status) {
        super(
                10001,
                message,
                HttpStatus.valueOf(status.value()),
                ErrorType.SYSTEM
        );
    }
}
