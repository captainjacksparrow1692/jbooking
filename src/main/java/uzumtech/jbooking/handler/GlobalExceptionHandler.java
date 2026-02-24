package uzumtech.jbooking.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import uzumtech.jbooking.constant.enums.Error;
import uzumtech.jbooking.constant.enums.ErrorType;
import uzumtech.jbooking.dto.ErrorDto;
import uzumtech.jbooking.exception.BusinessException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handleBusinessException(BusinessException ex) {
        log.error("Business Error: code={}, message={}", ex.getCode(), ex.getMessage());

        var errorBody = ErrorDto.builder()
                .code(ex.getCode())
                .errorType(ex.getErrorType())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(errorBody);
    }

    // 2. Обработка ошибок валидации @Valid (например, пустые поля в Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());

        List<String> details = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .toList();

        var errorBody = ErrorDto.builder()
                .code(Error.VALIDATION_ERROR_CODE.getCode()) // Предполагаем наличие такого кода в Enum
                .errorType(ErrorType.BUSINESS)
                .message("Validation failed")
                .validationErrors(details)
                .build();

        return ResponseEntity.badRequest().body(errorBody);
    }

    // 3. Обработка нечитаемого JSON (HttpMessageNotReadableException)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleJsonError(HttpMessageNotReadableException ex) {
        log.error("JSON Not Readable: {}", ex.getMessage());

        var errorBody = ErrorDto.builder()
                .code(Error.JSON_NOT_VALID_ERROR_CODE.getCode())
                .errorType(ErrorType.SYSTEM)
                .message(Error.JSON_NOT_VALID_ERROR_CODE.getMessage())
                .build();

        return ResponseEntity.badRequest().body(errorBody);
    }

    // 4. Обработка отсутствующих заголовков (MissingRequestHeaderException)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorDto> handleMissingHeaders(MissingRequestHeaderException ex) {
        var errorBody = ErrorDto.builder()
                .code(Error.MISSING_REQUEST_HEADER_ERROR_CODE.getCode())
                .errorType(ErrorType.SYSTEM)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorBody);
    }

    // 5. Обработка неправильных типов параметров в URL (MethodArgumentTypeMismatchException)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        var errorBody = ErrorDto.builder()
                .code(Error.INVALID_REQUEST_PARAM_ERROR_CODE.getCode())
                .errorType(ErrorType.SYSTEM)
                .message("Invalid parameter type: " + ex.getName())
                .build();
        return ResponseEntity.badRequest().body(errorBody);
    }

    // 6. Обработка несуществующих путей (NoHandlerFoundException)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDto> handleNoHandler(NoHandlerFoundException ex) {
        var errorBody = ErrorDto.builder()
                .code(Error.HANDLER_NOT_FOUND_ERROR_CODE.getCode())
                .errorType(ErrorType.SYSTEM)
                .message("Endpoint not found: " + ex.getRequestURL())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

    // 7. Финальный перехватчик всех остальных непредвиденных исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleAllExceptions(Exception ex) {
        log.error("Unexpected Error: ", ex);

        var errorBody = ErrorDto.builder()
                .code(Error.INTERNAL_SERVICE_ERROR_CODE.getCode())
                .errorType(ErrorType.SYSTEM)
                .message(Error.INTERNAL_SERVICE_ERROR_CODE.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }
}
