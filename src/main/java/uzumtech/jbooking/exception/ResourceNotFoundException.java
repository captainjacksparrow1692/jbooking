package uzumtech.jbooking.exception;

import org.springframework.http.HttpStatus;
import uzumtech.jbooking.constant.enums.ErrorType;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(20000, message, HttpStatus.NOT_FOUND, ErrorType.BUSINESS);
    }
}
