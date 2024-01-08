package at.jku.dke.etutor.task_app.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * API-key authentication token.
 */
public class ApiKeyAuthentication extends AbstractAuthenticationToken {
    private final ApiKeyConfiguration.ApiKey apiKey;

    /**
     * Creates a new instance of class {@link ApiKeyAuthentication}.
     * <p>
     * Sets the role ({@link ApiKeyConfiguration.ApiKey#roles()}) of the API key as authority.
     *
     * @param apiKey The API-key for which to create the authentication token.
     */
    public ApiKeyAuthentication(ApiKeyConfiguration.ApiKey apiKey) {
        super(AuthorityUtils.createAuthorityList(apiKey.roles()));
        this.apiKey = apiKey;
        this.setAuthenticated(true);
    }

    /**
     * The credentials that prove the principal is correct.
     *
     * @return The {@link ApiKeyConfiguration.ApiKey#key()}.
     */
    @Override
    public Object getCredentials() {
        return this.apiKey.key();
    }

    /**
     * The identity of the principal being authenticated.
     *
     * @return The {@link ApiKeyConfiguration.ApiKey}.
     */
    @Override
    public Object getPrincipal() {
        return this.apiKey;
    }
}
