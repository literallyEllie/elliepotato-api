package de.elliepotato.elliepotatoapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

/**
 * Wrapped error response, currently unused.
 */
public class ErrorResponse {

    private final int errorCode;
    private final String message;
    private final String redirection;
    private final HttpStatus httpStatus;

    public ErrorResponse(int errorCode, String message, String redirection, HttpStatus status) {
        this.errorCode = errorCode;
        this.message = message;
        this.redirection = redirection;
        this.httpStatus = status;
    }

    public ErrorResponse(int errorCode, String message) {
        this(errorCode, message, null, HttpStatus.BAD_REQUEST);
    }

    public ErrorResponse(ResponseStatusException exception) {
        this(exception.getStatusCode().value(), exception.getReason());
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Nullable
    public String getRedirection() {
        return redirection;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
