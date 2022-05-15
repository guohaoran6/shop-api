package io.recruitment.assessment.api.exception;

public class UnauthorizedErrorException extends RuntimeException {
    public UnauthorizedErrorException() {
    }

    public UnauthorizedErrorException(String message) {
        super(message);
    }

    public UnauthorizedErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedErrorException(Throwable cause) {
        super(cause);
    }

    public UnauthorizedErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
