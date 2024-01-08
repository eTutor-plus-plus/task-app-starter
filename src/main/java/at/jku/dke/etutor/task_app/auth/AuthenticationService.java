package at.jku.dke.etutor.task_app.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Provides a method for authenticating clients using API keys.
 */
@Service
public class AuthenticationService {

    private final ApiKeyConfiguration config;

    /**
     * Creates a new instance of class {@link AuthenticationService}.
     *
     * @param config The configuration containing the API keys.
     */
    public AuthenticationService(ApiKeyConfiguration config) {
        this.config = config;
    }

    /**
     * Returns the authentication for the specified API key.
     *
     * @param apiKey The API key.
     * @return The authentication. Either empty if {@code apiKey} was {@code null} or the authentication.
     * @throws BadCredentialsException If the {@code apiKey} is not {@code null}, but the API-Key is invalid.
     */
    public Optional<Authentication> authenticate(String apiKey) {
        if (apiKey == null)
            return Optional.empty();

        var key = this.config.getApiKey(apiKey)
            .orElseThrow(() -> new BadCredentialsException("Invalid API key"));
        return Optional.of(new ApiKeyAuthentication(key));
    }

    /**
     * Returns the authentication object for the specified request.
     * <p>
     * This method extracts the API key from the HTTP header {@link AuthConstants#AUTH_TOKEN_HEADER_NAME}.
     *
     * @param request The HTTP request to authenticate.
     * @return The authentication. Either empty if no header was found or the authentication.
     * @throws BadCredentialsException If the header was found, but the API-Key is invalid.
     */
    public Optional<Authentication> authenticate(HttpServletRequest request) {
        String apiKey = request.getHeader(AuthConstants.AUTH_TOKEN_HEADER_NAME);
        return this.authenticate(apiKey);
    }

}
