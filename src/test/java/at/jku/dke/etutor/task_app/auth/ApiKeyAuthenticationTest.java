package at.jku.dke.etutor.task_app.auth;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiKeyAuthenticationTest {

    @Test
    void getCredentials() {
        // Arrange
        final String key = "key";
        var apiKey = new ApiKeyConfiguration.ApiKey("name", key, List.of("CRUD"));

        // Act
        var auth = new ApiKeyAuthentication(apiKey);

        // Assert
        assertEquals(key, auth.getCredentials());
    }

    @Test
    void getPrincipal() {
        // Arrange
        final String key = "key";
        var apiKey = new ApiKeyConfiguration.ApiKey("name", key, List.of("CRUD"));

        // Act
        var auth = new ApiKeyAuthentication(apiKey);

        // Assert
        assertEquals(apiKey, auth.getPrincipal());
    }

    @Test
    void getAuthorities() {
        // Arrange
        final String key = "key";
        var apiKey = new ApiKeyConfiguration.ApiKey("name", key, List.of("CRUD"));

        // Act
        var auth = new ApiKeyAuthentication(apiKey);

        // Assert
        assertEquals(1, auth.getAuthorities().size());
    }

}
