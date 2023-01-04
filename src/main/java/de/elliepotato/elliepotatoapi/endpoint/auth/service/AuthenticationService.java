package de.elliepotato.elliepotatoapi.endpoint.auth.service;

import de.elliepotato.elliepotatoapi.endpoint.auth.repository.UserRepository;
import de.elliepotato.elliepotatoapi.endpoint.auth.request.AuthenticationRequest;
import de.elliepotato.elliepotatoapi.endpoint.auth.response.AuthenticationResponse;
import de.elliepotato.elliepotatoapi.endpoint.auth.request.RegisterRequest;
import de.elliepotato.elliepotatoapi.endpoint.auth.user.User;
import de.elliepotato.elliepotatoapi.exception.ApiError;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for registering and authenticating users.
 */
@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository repository, PasswordEncoder passwordEncoder,
            JwtService jwtService, AuthenticationManager authenticationManager

    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Register a user from a request and return a JWT response.
     * </p>
     * If the user exists, {@link ApiError#USERNAME_TAKEN} will be thrown.
     *
     * @param request Request to auth.
     * @return An authentication response containing the JWT
     */
    public AuthenticationResponse register(RegisterRequest request) {
        // Check doesn't exist already
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw ApiError.USERNAME_TAKEN.createException();
        }

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword())
        );
        repository.save(user);

        return new AuthenticationResponse(
                jwtService.generateToken(user)
        );
    }

    /**
     * Process an authentication request and return a JWT.
     * </p>
     * If the account is disabled, will throw {@link ApiError#ACCOUNT_DISABLED}
     * If the account is locked, will throw {@link ApiError#ACCOUNT_LOCKED}
     * If bad credentials are provided, will throw {@link ApiError#BAD_LOGIN_CREDENTIALS}
     *
     * @param request Request to get a JWT.
     * @return a JWT response.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()
                    )
            );

            // todo prevent dupe tokens

            System.out.println(authenticate.isAuthenticated());
        } catch (DisabledException e) {
            throw ApiError.ACCOUNT_DISABLED.createException();
        } catch (LockedException e) {
            throw ApiError.ACCOUNT_LOCKED.createException();
        } catch (BadCredentialsException e) {
            throw ApiError.BAD_LOGIN_CREDENTIALS.createException();
        }

        // ok authenticated
        User user = repository.findByUsername(request.getUsername())
                .orElseThrow(ApiError.BAD_LOGIN_CREDENTIALS::createException);

        // Hello
        user.setLastLogin(System.currentTimeMillis());
        repository.save(user);

        return new AuthenticationResponse(
                jwtService.generateToken(user)
        );
    }

}
