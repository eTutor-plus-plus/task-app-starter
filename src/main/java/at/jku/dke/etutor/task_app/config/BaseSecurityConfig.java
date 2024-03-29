package at.jku.dke.etutor.task_app.config;

import at.jku.dke.etutor.task_app.auth.ApiKeyConfiguration;
import at.jku.dke.etutor.task_app.auth.AuthConstants;
import at.jku.dke.etutor.task_app.auth.AuthenticationFilter;
import at.jku.dke.etutor.task_app.auth.AuthenticationService;
import at.jku.dke.etutor.task_app.controllers.ProblemDetailsExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

/**
 * Base class for security configuration.
 * <p>
 * Override this class and add the {@link org.springframework.context.annotation.Configuration} annotation to the subclass.
 * <p>
 * This class configures the following:
 * <ul>
 *     <li>CSRF protection is disabled</li>
 *     <li>Secure HTTP headers are configured (Referrer Policy, Frame Options, XSS Protection)</li>
 *     <li>HTTP request authorizations are configured (all /api/ endpoints must be authenticated, actuator endpoints health/* are permitted by all, info needs to be authenticated and all other actuator endpoints need CRUD authority).</li>
 *     <li>Session management is configured to stateless</li>
 *     <li>An {@link AuthenticationFilter} is added to the filter chain</li>
 *     <li>Additional configuration can be done by overriding the {@link #additionalHttpRequestAuthorizationCustomization(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry)} method</li>
 *     <li>Additional configuration can be done by overriding the {@link #additionalFilterChainCustomization(HttpSecurity)} method</li>
 * </ul>
 */
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(value = {ApiKeyConfiguration.class})
@ComponentScan(basePackageClasses = {AuthenticationService.class, ProblemDetailsExceptionHandler.class})
public abstract class BaseSecurityConfig {

    /**
     * Creates a new instance of class {@link BaseSecurityConfig}.
     */
    protected BaseSecurityConfig() {
    }

    /**
     * Configures the applications' security filter chain.
     *
     * @param http                  The HTTP security configuration.
     * @param authenticationService The authentication service.
     * @return The security filter chain.
     * @throws Exception If the configuration fails.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationService authenticationService) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.headers(conf -> {
            conf.referrerPolicy(c -> c.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER));
            conf.frameOptions(HeadersConfigurer.FrameOptionsConfig::deny);
            conf.xssProtection(c -> c.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK));
        });

        http.authorizeHttpRequests(reg -> {
            reg.requestMatchers(HttpMethod.OPTIONS).permitAll();

            additionalHttpRequestAuthorizationCustomization(reg);

            reg.requestMatchers("/actuator/health").permitAll();
            reg.requestMatchers("/actuator/health/liveness").permitAll();
            reg.requestMatchers("/actuator/health/readiness").permitAll();
            reg.requestMatchers("/actuator/info").authenticated();
            reg.requestMatchers("/actuator/**").hasAuthority(AuthConstants.CRUD);

            reg.requestMatchers("/api/**").authenticated();

            reg.anyRequest().permitAll();
        });

        http.sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new AuthenticationFilter(authenticationService), UsernamePasswordAuthenticationFilter.class);

        this.additionalFilterChainCustomization(http);

        return http.build();
    }

    /**
     * Override this method to configure additional HTTP request authorizations.
     *
     * @param registry The request matcher registry.
     */
    protected void additionalHttpRequestAuthorizationCustomization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
    }

    /**
     * Override this method to customize the security filter chain.
     *
     * @param http The HTTP security configuration.
     * @throws Exception If the customization fails.
     */
    protected void additionalFilterChainCustomization(HttpSecurity http) throws Exception {
    }
}
