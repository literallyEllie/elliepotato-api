package de.elliepotato.elliepotatoapi.exception;

import org.springframework.web.ErrorResponseException;

/**
 * Handled response exception for {@link ApiError}
 */
public class ApiException extends ErrorResponseException {

    private static final String PROP_ERROR_CODE = "apiError";

    private final ApiError error;

    public ApiException(ApiError error) {
        super(
                error.getHttpStatus(),
                error.asProblemDetail(),
                null,
                String.valueOf(error.getErrorCode()),
                new Object[0]
        );
        getBody().setProperty(
                PROP_ERROR_CODE, error.getErrorCode()
        );
        this.error = error;
    }

    /**
     * @return The error that happened.
     */
    public ApiError getError() {
        return error;
    }

}
