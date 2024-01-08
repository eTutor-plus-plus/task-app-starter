package at.jku.dke.etutor.task_app.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

    private AuthenticationService authenticationService;
    private AuthenticationFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        this.authenticationService = Mockito.mock(AuthenticationService.class);
        this.filter = new AuthenticationFilter(this.authenticationService);
        this.request = Mockito.mock(HttpServletRequest.class);
        this.response = Mockito.mock(HttpServletResponse.class);
        this.chain = Mockito.mock(FilterChain.class);
    }

    @Test
    void doFilter() throws ServletException, IOException {
        // Arrange
        var apiKey = new ApiKeyConfiguration.ApiKey("task-admin", "task-admin-key", List.of("CRUD"));
        Mockito.when(authenticationService.authenticate(request)).thenReturn(Optional.of(new ApiKeyAuthentication(apiKey)));

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        Mockito.verify(chain).doFilter(request, response);
        assertEquals(apiKey.key(), SecurityContextHolder.getContext().getAuthentication().getCredentials());
    }

    @Test
    void doFilterNullHeader() throws ServletException, IOException {
        // Arrange
        Mockito.when(authenticationService.authenticate(request)).thenReturn(Optional.empty());

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        Mockito.verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInvalidHeader() throws ServletException, IOException {
        // Arrange
        Mockito.when(authenticationService.authenticate(request)).thenThrow(new BadCredentialsException("Invalid API key"));
        Mockito.when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));

        // Act
        filter.doFilter(request, response, chain);

        // Assert
        Mockito.verifyNoInteractions(chain);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
