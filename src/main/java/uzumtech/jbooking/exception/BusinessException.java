package uzumtech.jbooking.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import uzumtech.jbooking.constant.enums.ErrorType;

@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusinessException extends RuntimeException {
    int code;
    String message;
    HttpStatus status;
    ErrorType errorType;

    public BusinessException(int code, String message, HttpStatus status, ErrorType errorType) {
        super(message);
        this.code = code;
        this.message = message;
        this.status = status;
        this.errorType = errorType;
    }
}
