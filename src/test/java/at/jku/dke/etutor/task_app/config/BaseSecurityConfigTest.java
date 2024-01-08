package at.jku.dke.etutor.task_app.config;

import at.jku.dke.etutor.task_app.auth.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.mockito.Mockito.mock;

class BaseSecurityConfigTest {

    @Test
    void filterChain() throws Exception {
        // Arrange
        var http = mock(HttpSecurity.class);
        var authenticationService = mock(AuthenticationService.class);

        // Act
        var config = new TestSecurityConfig();
        config.filterChain(http, authenticationService);

        // Assert
        Mockito.verify(http, Mockito.times(1)).csrf(Mockito.any());
        Mockito.verify(http, Mockito.times(1)).headers(Mockito.any());
        Mockito.verify(http, Mockito.times(1)).authorizeHttpRequests(Mockito.any());
        Mockito.verify(http, Mockito.times(1)).sessionManagement(Mockito.any());
        Mockito.verify(http, Mockito.times(1)).addFilterBefore(Mockito.any(), Mockito.eq(UsernamePasswordAuthenticationFilter.class));
        Mockito.verify(http, Mockito.times(1)).build();
    }


    private static class TestSecurityConfig extends BaseSecurityConfig {
    }
}
