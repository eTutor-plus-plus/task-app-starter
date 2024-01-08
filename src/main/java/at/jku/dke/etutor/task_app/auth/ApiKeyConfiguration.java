package at.jku.dke.etutor.task_app.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Loads the clients from the application-properties.
 */
@Validated
@ConfigurationProperties("clients")
public class ApiKeyConfiguration {

    @NotNull
    private List<ApiKey> apiKeys;

    /**
     * Creates a new instance of class {@link ApiKeyConfiguration}.
     */
    public ApiKeyConfiguration() {
        this.apiKeys = Collections.emptyList();
    }

    /**
     * Gets the API-keys.
     *
     * @return The available API keys.
     */
    public List<ApiKey> getApiKeys() {
        return apiKeys;
    }

    /**
     * Sets the API-keys.
     *
     * @param apiKeys The API-keys.
     */
    public void setApiKeys(List<ApiKey> apiKeys) {
        this.apiKeys = apiKeys;
    }

    /**
     * Returns the API-key with the specified key.
     *
     * @param key The API-key to find.
     * @return The {@link ApiKey} instance or an empty result if no key was found.
     */
    public Optional<ApiKey> getApiKey(String key) {
        if (key == null || key.isBlank())
            return Optional.empty();
        return this.apiKeys.stream().filter(x -> x.key().equals(key)).findFirst();
    }

    /**
     * Represents an API-key.
     *
     * @param name  The name of the API-key.
     * @param key   The API-key itself.
     * @param roles The roles of the API-key (specifies what the application is allowed to do).
     */
    public record ApiKey(@NotEmpty String name, @NotEmpty String key, @NotEmpty List<String> roles) {
    }

}
