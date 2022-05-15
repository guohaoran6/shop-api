package io.recruitment.assessment.api.exception;

import io.recruitment.assessment.api.dto.ApiResponseEntity;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    public ApiExceptionHandler() {
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(
            code = HttpStatus.INTERNAL_SERVER_ERROR
    )
    public ApiResponseEntity internalErrorHandler(Exception exception, ServletWebRequest request) {
        log.error("{}", ExceptionUtils.getStackTrace(exception));
        return new ApiResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage(), request);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(
            code = HttpStatus.NOT_FOUND
    )
    public ApiResponseEntity notFoundHandler(Exception exception, ServletWebRequest request) {
        log.warn("{}", exception.getMessage());
        return new ApiResponseEntity(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getMessage(), request);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(
            code = HttpStatus.BAD_REQUEST
    )
    public ApiResponseEntity illegalArgumentException(Exception exception, ServletWebRequest request) {
        log.warn("{}", exception.getMessage());
        return new ApiResponseEntity(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), exception.getMessage(), request);
    }

    @ExceptionHandler({UnauthorizedErrorException.class})
    @ResponseStatus(
            code = HttpStatus.UNAUTHORIZED
    )
    public ApiResponseEntity unauthorizedErrorException(Exception exception, ServletWebRequest request) {
        log.warn("{}", exception.getMessage());
        return new ApiResponseEntity(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), exception.getMessage(), request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("{}", exception.getMessage());
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", exception, 0);
        }

        ServletWebRequest servletWebRequest = (ServletWebRequest)request;
        ApiResponseEntity apiResponseEntity = new ApiResponseEntity(status.value(), status.getReasonPhrase(), exception.getMessage(), servletWebRequest);
        return new ResponseEntity(apiResponseEntity, headers, status);
    }
}
