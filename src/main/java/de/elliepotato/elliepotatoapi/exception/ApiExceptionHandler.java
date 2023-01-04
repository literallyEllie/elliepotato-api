package de.elliepotato.elliepotatoapi.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception handler for unhandled exceptions.
 * </p>
 * {@link ApiException} are handled with the default error handling.
 */
@ControllerAdvice
@RestController
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnhandledError(
            RuntimeException ex, WebRequest request
    ) {
        ApiError response = ApiError.UNHANDLED_ERROR;

        return handleExceptionInternal(
                ex,
                response.asProblemDetail(),
                new HttpHeaders(),
                response.getHttpStatus(),
                request
        );
    }

}