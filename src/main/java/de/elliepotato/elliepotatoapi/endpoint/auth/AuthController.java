package de.elliepotato.elliepotatoapi.endpoint.auth;

import de.elliepotato.elliepotatoapi.endpoint.auth.request.AuthenticationRequest;
import de.elliepotato.elliepotatoapi.endpoint.auth.response.AuthenticationResponse;
import de.elliepotato.elliepotatoapi.endpoint.auth.request.RegisterRequest;
import de.elliepotato.elliepotatoapi.endpoint.auth.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling auth requests.
 */
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthenticationService service;

    public AuthController(AuthenticationService service) {
        this.service = service;
    }

    // private
    @PostMapping("/register")
    public AuthenticationResponse register(
            @RequestBody RegisterRequest request
    ) {
        return service.register(request);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return service.authenticate(request);
    }

}
