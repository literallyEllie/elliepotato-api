package de.elliepotato.elliepotatoapi.endpoint.auth.request;

/**
 * Request to authenticate.
 * </p>
 * This is sent on registration and also on login.
 */
public class AuthenticationRequest {
    private String username;
    private String password;

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthenticationRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
