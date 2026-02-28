package uzumtech.jbooking.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import uzumtech.jbooking.constant.enums.Error;
import uzumtech.jbooking.constant.enums.ErrorType;
import uzumtech.jbooking.dto.ErrorDto;
import uzumtech.jbooking.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. BusinessException и все её наследники (ResourceNotFoundException, BookingValidationException)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handleBusinessException(BusinessException ex) {
        log.error("Business Error: code={}, message={}", ex.getCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorDto.builder()
                        .code(ex.getCode())
                        .errorType(ex.getErrorType())
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 2. Ошибки валидации @Valid
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

        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder()
                        .code(Error.VALIDATION_ERROR_CODE.getCode())
                        .errorType(ErrorType.BUSINESS)
                        .message("Validation failed")
                        .timestamp(LocalDateTime.now())
                        .validationErrors(details)
                        .build());
    }

    // 3. Отсутствует обязательный query-параметр (?userId=...)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDto> handleMissingParam(MissingServletRequestParameterException ex) {
        log.error("Missing request parameter: {}", ex.getParameterName());
        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder()
                        .code(Error.INVALID_REQUEST_PARAM_ERROR_CODE.getCode())
                        .errorType(ErrorType.BUSINESS)
                        .message("Required parameter '" + ex.getParameterName() + "' is missing")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 4. Неверный тип параметра (передали строку вместо UUID и т.д.)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Type mismatch for parameter: {}", ex.getName());
        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder()
                        .code(Error.INVALID_REQUEST_PARAM_ERROR_CODE.getCode())
                        .errorType(ErrorType.BUSINESS)
                        .message("Invalid value for parameter '" + ex.getName() + "'")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 5. Невалидный JSON в теле запроса
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleJsonError(HttpMessageNotReadableException ex) {
        log.error("JSON Not Readable: {}", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder()
                        .code(Error.JSON_NOT_VALID_ERROR_CODE.getCode())
                        .errorType(ErrorType.BUSINESS)
                        .message(Error.JSON_NOT_VALID_ERROR_CODE.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 6. Отсутствует обязательный заголовок
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorDto> handleMissingHeaders(MissingRequestHeaderException ex) {
        return ResponseEntity
                .badRequest()
                .body(ErrorDto.builder()
                        .code(Error.MISSING_REQUEST_HEADER_ERROR_CODE.getCode())
                        .errorType(ErrorType.BUSINESS)
                        .message("Required header '" + ex.getHeaderName() + "' is missing")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 7. Неверный HTTP метод (POST вместо GET и т.д.)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDto> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.error("Method not supported: {}", ex.getMethod());
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorDto.builder()
                        .code(Error.METHOD_NOT_SUPPORTED_ERROR_CODE.getCode())
                        .errorType(ErrorType.BUSINESS)
                        .message("HTTP method '" + ex.getMethod() + "' is not supported for this endpoint")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 8. Несуществующий путь (Spring MVC)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDto> handleNoHandler(NoHandlerFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorDto.builder()
                        .code(Error.HANDLER_NOT_FOUND_ERROR_CODE.getCode())
                        .errorType(ErrorType.BUSINESS)
                        .message("Endpoint not found: " + ex.getRequestURL())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 9. Несуществующий статический ресурс (Spring Boot 3+)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDto> handleNoResource(NoResourceFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorDto.builder()
                        .code(Error.HANDLER_NOT_FOUND_ERROR_CODE.getCode())
                        .errorType(ErrorType.BUSINESS)
                        .message("Endpoint not found: " + ex.getResourcePath())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // 10. Все остальные непредвиденные исключения
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleAllExceptions(Exception ex) {
        log.error("Unexpected Error: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDto.builder()
                        .code(Error.INTERNAL_SERVICE_ERROR_CODE.getCode())
                        .errorType(ErrorType.SYSTEM)
                        .message(Error.INTERNAL_SERVICE_ERROR_CODE.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}