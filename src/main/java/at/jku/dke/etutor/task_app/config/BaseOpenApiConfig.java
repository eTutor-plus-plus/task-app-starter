package at.jku.dke.etutor.task_app.config;

import at.jku.dke.etutor.task_app.auth.AuthConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;

/**
 * Base class for OpenAPI configuration.
 * <p>
 * Override this class and add the {@link org.springframework.context.annotation.Configuration} annotation to the subclass.
 */
public abstract class BaseOpenApiConfig {

    private final String title;
    private final String description;
    private final String version;

    /**
     * Creates a new instance of class {@link BaseOpenApiConfig}.
     *
     * @param title       The title of the API documentation.
     * @param description The description of the API.
     * @param version     The version of the API.
     */
    protected BaseOpenApiConfig(String title, String description, String version) {
        this.title = title;
        this.description = description;
        this.version = version;
    }

    /**
     * Provides the Open API information.
     *
     * @return The Open API information.
     */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
            .info(openApiInfo())
            .components(new Components()
                .addSecuritySchemes("api-key", new SecurityScheme()
                    .name(AuthConstants.AUTH_TOKEN_HEADER_NAME)
                    .in(SecurityScheme.In.HEADER)
                    .type(SecurityScheme.Type.APIKEY)
                    .description("The authentication token.")));
    }

    /**
     * Provides the OpenAPI information.
     *
     * @return The OpenAPI information.
     */
    protected Info openApiInfo() {
        Info info = new Info();
        info.setTitle(this.title);
        info.setDescription(this.description);
        info.setVersion(this.version);
        return info;
    }

}
