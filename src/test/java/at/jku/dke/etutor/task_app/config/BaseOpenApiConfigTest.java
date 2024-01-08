package at.jku.dke.etutor.task_app.config;

import at.jku.dke.etutor.task_app.auth.AuthConstants;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseOpenApiConfigTest {

    @Test
    void customOpenApi() {
        // Arrange
        final String title = "Test Title";
        final String description = "Test Description";
        final String version = "Test Version";

        // Act
        var config = new TestOpenApiConfig(title, description, version);
        var result = config.customOpenApi();

        // Assert
        var info = result.getInfo();
        assertEquals(title, info.getTitle());
        assertEquals(description, info.getDescription());
        assertEquals(version, info.getVersion());

        assertEquals(1, result.getComponents().getSecuritySchemes().size());
        result.getComponents().getSecuritySchemes().forEach((key, value) -> {
            assertEquals(AuthConstants.AUTH_TOKEN_HEADER_NAME, value.getName());
            assertEquals(SecurityScheme.In.HEADER, value.getIn());
            assertEquals(SecurityScheme.Type.APIKEY, value.getType());
        });
    }

    private static class TestOpenApiConfig extends BaseOpenApiConfig {
        protected TestOpenApiConfig(String title, String description, String version) {
            super(title, description, version);
        }
    }
}
