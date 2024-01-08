package at.jku.dke.etutor.task_app.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApiKeyConfiguration.class, properties = """
clients.api-keys[0].name=task-administration
clients.api-keys[0].key=task-admin-key
clients.api-keys[0].roles[0]=CRUD
clients.api-keys[0].roles[1]=SUBMIT

clients.api-keys[1].name=task-submission
clients.api-keys[1].key=task-submit-key
clients.api-keys[1].roles[0]=SUBMIT

clients.api-keys[2].name=task-check
clients.api-keys[2].key=task-check-key
clients.api-keys[2].roles[0]=READ_SUBMISSION
""")
@EnableConfigurationProperties
class ApiKeyConfigurationTest {

    @Autowired
    private ApiKeyConfiguration keyConfiguration;

    @Test
    void getApiKeys() {
        // Act
        var keys = keyConfiguration.getApiKeys();

        // Assert
        assertNotNull(keys);
        assertEquals(3, keys.size());
    }

    @Test
    void getApiKey() {
        // Act
        var key = keyConfiguration.getApiKey("task-admin-key");

        // Assert
        assertTrue(key.isPresent());
        assertEquals("task-admin-key", key.get().key());
        assertEquals("task-administration", key.get().name());
        assertTrue(key.get().roles().contains("CRUD"));
        assertTrue(key.get().roles().contains("SUBMIT"));
    }

    @Test
    void getApiKeyNotExisting() {
        // Act
        var key = keyConfiguration.getApiKey("not-existing-key");

        // Assert
        assertTrue(key.isEmpty());
    }

    @Test
    void getApiKeyNull() {
        // Act
        var key = keyConfiguration.getApiKey(null);

        // Assert
        assertTrue(key.isEmpty());
    }

    @Test
    void getApiKeyBlank() {
        // Act
        var key = keyConfiguration.getApiKey("  ");

        // Assert
        assertTrue(key.isEmpty());
    }
}
