package de.elliepotato.elliepotatoapi.endpoint.auth.response;

/**
 * Successful response of authentication.
 */
public class AuthenticationResponse {
    private final String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
