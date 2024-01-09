package at.jku.dke.etutor.task_app;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.mock.env.MockEnvironment;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AppHelperTest {

    @Test
    void logApplicationStartupWithSslAndContext() {
        // Arrange
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "Test");
        env.setProperty("server.ssl.key-store", "something");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", "/test");
        env.setActiveProfiles("test");
        Logger logger = mock(Logger.class);

        // Assert
        AppHelper.logApplicationStartup(logger, env);

        // Assert
        verify(logger).info(anyString(), eq("Test"), eq("https"), eq("8080"), eq("/test"), eq("https"), anyString(), eq("8080"), eq("/test"), argThat(x -> {
            var arr = (String[]) x;
            return arr.length == 1 && arr[0].equals("test");
        }));
    }

    @Test
    void logApplicationStartupWithoutSslAndContext() {
        // Arrange
        MockEnvironment env = new MockEnvironment();
        env.setProperty("spring.application.name", "Test");
        env.setProperty("server.port", "8080");
        env.setProperty("server.servlet.context-path", " ");
        Logger logger = mock(Logger.class);

        // Assert
        AppHelper.logApplicationStartup(logger, env);

        // Assert
        verify(logger).info(anyString(), eq("Test"), eq("http"), eq("8080"), eq("/"), eq("http"), anyString(), eq("8080"), eq("/"), argThat(x -> {
            var arr = (String[]) x;
            return arr.length == 1 && arr[0].equals("default");
        }));
    }

}
