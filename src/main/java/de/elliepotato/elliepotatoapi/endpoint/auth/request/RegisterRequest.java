

package de.elliepotato.elliepotatoapi.endpoint.auth.request;

/**
 * Request to register with the service.
 * </p>
 * This would allow access to private endpoints.
 */
public class RegisterRequest {
    private String username;
    private String password;

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RegisterRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
