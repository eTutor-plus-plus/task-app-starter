package at.jku.dke.etutor.task_app.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ApiKeyConfiguration.class, AuthenticationService.class}, properties = """
clients.api-keys[0].name=task-administration
clients.api-keys[0].key=task-admin-key
clients.api-keys[0].roles[0]=CRUD
""")
@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void authenticate() {
        // Arrange
        var key = "task-admin-key";

        // Act
        var result = authenticationService.authenticate(key);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(key, result.get().getCredentials());
    }

    @Test
    void authenticateNullReturnsEmpty() {
        // Arrange
        String key = null;

        // Act
        var result = authenticationService.authenticate(key);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void authenticateInvalidThrowsException() {
        // Arrange
        var key = "invalid-key";

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(key));
    }

    @Test
    void authenticateRequest() {
        // Arrange
        var request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(AuthConstants.AUTH_TOKEN_HEADER_NAME)).thenReturn("task-admin-key");

        // Act
        var result = authenticationService.authenticate(request);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("task-admin-key", result.get().getCredentials());
    }

    @Test
    void authenticateRequestNullReturnsEmpty() {
        // Arrange
        var request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(AuthConstants.AUTH_TOKEN_HEADER_NAME)).thenReturn(null);

        // Act
        var result = authenticationService.authenticate(request);

        // Assert
        assertTrue(result.isEmpty());
    }
}
