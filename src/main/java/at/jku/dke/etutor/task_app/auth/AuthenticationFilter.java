package at.jku.dke.etutor.task_app.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * Filter that authenticates the client using an API-key (if present).
 */
public class AuthenticationFilter extends GenericFilterBean {

    private final AuthenticationService authenticationService;

    /**
     * Creates a new instance of class {@link AuthenticationFilter}.
     *
     * @param authenticationService The authentication service.
     */
    public AuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Extracts the API-key from the HTTP header (if present).
     *
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response to for further processing.
     * @throws IOException      If an I/O error occurs during this filter's processing of the request.
     * @throws ServletException If the processing fails for any other reason.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            Optional<Authentication> auth = this.authenticationService.authenticate((HttpServletRequest) request);
            auth.ifPresent(a -> SecurityContextHolder.getContext().setAuthentication(a));
            chain.doFilter(request, response);
        } catch (BadCredentialsException ex) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

            var problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
            problem.setDetail(ex.getMessage());

            PrintWriter writer = httpResponse.getWriter();
            Jackson2ObjectMapperBuilder.json()
                .build()
                .writeValue(writer, problem);
            writer.flush();
            writer.close();
        }
    }

}
