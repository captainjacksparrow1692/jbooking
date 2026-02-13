package uzumtech.jbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import uzumtech.jbooking.constant.enums.ErrorType;
import uzumtech.jbooking.constant.enums.Error;

public class HttpClientException extends BusinessException {

    public HttpClientException(String message, HttpStatusCode status) {
        super(
                Error.HTTP_CLIENT_ERROR_CODE.getCode(),
                message,
                HttpStatus.valueOf(status.value()),
                ErrorType.EXTERNAL
        );
    }
}