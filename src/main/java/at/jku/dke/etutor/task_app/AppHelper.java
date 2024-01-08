package at.jku.dke.etutor.task_app;

import org.slf4j.Logger;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * Contains some helper methods.
 */
@SuppressWarnings("unused")
public final class AppHelper {
    private AppHelper() {
    }

    /**
     * Logs the URLs under which the application is reachable.
     *
     * @param logger The logger.
     * @param env    The application environment.
     */
    public static void logApplicationStartup(Logger logger, Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(x -> !x.isBlank())
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            logger.warn("The host name could not be determined, using `localhost` as fallback");
        }
        var profiles = env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles();

        logger.info("""

                ----------------------------------------------------------
                \tApplication '{}' is running! Access URLs:
                \tLocal: \t\t{}://localhost:{}{}
                \tExternal: \t{}://{}:{}{}
                \tProfile(s): \t{}
                ----------------------------------------------------------""",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            profiles
        );
    }
}
