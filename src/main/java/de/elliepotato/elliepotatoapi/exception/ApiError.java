package de.elliepotato.elliepotatoapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.Nullable;

/**
 * Definition of all handled errors on the API service.s
 */
public enum ApiError {

    // GENERIC
    UNHANDLED_ERROR(1, "Unhandled Api Error", HttpStatus.INTERNAL_SERVER_ERROR),

    // AUTH
    USERNAME_TAKEN(100, "Username Taken"),
    NO_SUCH_USER(101, "No such username"),
    BAD_LOGIN_CREDENTIALS(102, "Bad login credentials", HttpStatus.UNAUTHORIZED),
    ACCOUNT_DISABLED(103, "Account Disabled", HttpStatus.FORBIDDEN),
    ACCOUNT_LOCKED(104, "Account Locked", HttpStatus.FORBIDDEN),

    // Plugins
    NO_SUCH_PLUGIN(301, "No such plugin", HttpStatus.NOT_FOUND),
    PLUGIN_ALREADY_EXISTS(302, "Plugin already exists by that id"),
    PLUGIN_REQUEST_INVALID(303, "Invalid plugin");

    private final int errorCode;
    private final String message;
    private final String redirection;
    private final HttpStatus status;

    ApiError(int errorCode, String message, String redirection, HttpStatus status) {
        this.errorCode = errorCode;
        this.message = message;
        this.redirection = redirection;
        this.status = status;
    }

    ApiError(int errorCode, String message) {
        this(errorCode, message, (String) null);
    }

    ApiError(int errorCode, String message, String redirection) {
        this(errorCode, message, redirection, HttpStatus.BAD_REQUEST);
    }

    ApiError(int errorCode, String message, HttpStatus status) {
        this(errorCode, message, null, status);
    }

    /**
     * @return The API error code.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @return Information message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return Optional redirection message if a request is sent to the wrong plcae.
     */
    @Nullable
    public String getRedirection() {
        return redirection;
    }

    /**
     * @return The http status to return for this error.
     */
    public HttpStatus getHttpStatus() {
        return status;
    }

    /**
     * @return The error as the {@link ProblemDetail}
     */
    public ProblemDetail asProblemDetail() {
        return ProblemDetail.forStatusAndDetail(status, message);
    }

    /**
     * @return Create the error into an {@link ApiException} to throw.
     */
    public ApiException createException() {
        return new ApiException(this);
    }

    /**
     * Create the error in an {@link ApiException} to throw with additional properties.
     *
     * @param key Property key.
     * @param value Property Value
     * @param properties Subsequent keys and values in, key, value, key order.
     * @return The exception.
     */
    public ApiException createExceptionWithProperties(String key, Object value, Object... properties) {
        ApiException apiException = new ApiException(this);
        apiException.getBody().setProperty(key, value);

        if (properties.length == 0) {
            return apiException;
        }

        String subsequentKey = null;
        for (Object property : properties) {
            if (subsequentKey != null) {
                apiException.getBody().setProperty(key, property);
                subsequentKey = null;
            } else {
                subsequentKey = property.toString();
            }
        }

        return apiException;
    }


}
