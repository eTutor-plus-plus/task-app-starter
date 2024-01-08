package at.jku.dke.etutor.task_app.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.properties.javabean.JavaBeanGetter;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.context.request.WebRequest;

import java.lang.annotation.Annotation;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ProblemDetailsExceptionHandlerTest {

    @Test
    void handleBadCredentialsException() {
        // Arrange
        var request = mock(WebRequest.class);
        var ex = new BadCredentialsException("Bad credentials");

        // Act
        var result = new ProblemDetailsExceptionHandler().handleBadCredentialsException(ex, request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        var problem = (ProblemDetail) result.getBody();
        assertNotNull(problem);
        assertEquals("Bad credentials", problem.getDetail());
        assertEquals("http://etutor.dke.uni-linz.ac.at/errors/bad-credentials", problem.getType().toString());
    }

    @Test
    void handleAccessDeniedException() {
        // Arrange
        var request = mock(WebRequest.class);
        var ex = new AccessDeniedException("Access denied");

        // Act
        var result = new ProblemDetailsExceptionHandler().handleAccessDeniedException(ex, request);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        var problem = (ProblemDetail) result.getBody();
        assertNotNull(problem);
        assertEquals("Access denied", problem.getDetail());
        assertEquals("http://etutor.dke.uni-linz.ac.at/errors/access-denied", problem.getType().toString());
    }

    @Test
    void handleEntityNotFoundException() {
        // Arrange
        var request = mock(WebRequest.class);
        var ex = new EntityNotFoundException("Entity not found");

        // Act
        var result = new ProblemDetailsExceptionHandler().handleEntityNotFoundException(ex, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        var problem = (ProblemDetail) result.getBody();
        assertNotNull(problem);
        assertEquals("Entity not found", problem.getDetail());
        assertEquals("http://etutor.dke.uni-linz.ac.at/errors/not-found", problem.getType().toString());
    }

    @Test
    void handleConstraintViolationException() {
        // Arrange
        var request = mock(WebRequest.class);
        var ex = new ConstraintViolationException("Constraint violation", Set.of(
            ConstraintViolationImpl.forBeanValidation("It is invalid.", null, null, "It is invalid.",
                Locale.class, Locale.GERMAN, null, Locale.GERMAN, PathImpl.createPathFromString("country"),
                new ConstraintDescriptorImpl<>(ConstraintHelper.forAllBuiltinConstraints(), mock(JavaBeanGetter.class),
                    new ConstraintAnnotationDescriptor<NotNull>(new NotNull() {

                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return NotNull.class;
                        }

                        @Override
                        public String message() {
                            return "test";
                        }

                        @Override
                        public Class<?>[] groups() {
                            return new Class[0];
                        }

                        @Override
                        public Class<? extends Payload>[] payload() {
                            //noinspection unchecked
                            return new Class[0];
                        }
                    }), ConstraintLocation.ConstraintLocationKind.METHOD), new Object()),
            ConstraintViolationImpl.forBeanValidation("Error", null, null, "Error",
                Locale.class, Locale.GERMAN, null, Locale.GERMAN, PathImpl.createPathFromString("country"), null, new Object())));

        // Act
        var result = new ProblemDetailsExceptionHandler().handleConstraintViolationException(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var problem = (ProblemDetail) result.getBody();
        assertNotNull(problem);
        assertEquals("Constraint violation", problem.getDetail());
        assertEquals("http://etutor.dke.uni-linz.ac.at/errors/validation-error", problem.getType().toString());
        assertNotNull(problem.getProperties());
        assertNotNull(problem.getProperties().get("violations"));
    }

    @Test
    void handleDbConstraintViolationException() {
        // Arrange
        var request = mock(WebRequest.class);
        var ex = new org.hibernate.exception.ConstraintViolationException("Constraint violation", new SQLException("error", "ERR", 9), "constraint_fk");

        // Act
        var result = new ProblemDetailsExceptionHandler().handleConstraintViolationException(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var problem = (ProblemDetail) result.getBody();
        assertNotNull(problem);
        assertEquals("Constraint violation", problem.getDetail());
        assertEquals("http://etutor.dke.uni-linz.ac.at/errors/validation-error/db-constraint", problem.getType().toString());
        assertNotNull(problem.getProperties());
        assertEquals("constraint_fk", problem.getProperties().get("constraint"));
        assertEquals(9, problem.getProperties().get("errorCode"));
    }

    @Test
    void handleDuplicateKeyException() {
        // Arrange
        var request = mock(WebRequest.class);
        var ex = new DuplicateKeyException("Duplicate key", new SQLException("error", "ERR", 9));

        // Act
        var result = new ProblemDetailsExceptionHandler().handleDuplicateKeyException(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var problem = (ProblemDetail) result.getBody();
        assertNotNull(problem);
        assertEquals("Duplicate key", problem.getDetail());
        assertEquals("http://etutor.dke.uni-linz.ac.at/errors/validation-error/duplicate-key", problem.getType().toString());
    }

    @Test
    void handleDataIntegrityViolationExceptionWithCause() {
        // Arrange
        var request = mock(WebRequest.class);
        var ex = new org.springframework.dao.DataIntegrityViolationException("Data integrity violation", new SQLException("error", "ERR", 9));

        // Act
        var result = new ProblemDetailsExceptionHandler().handleDataIntegrityViolationException(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var problem = (ProblemDetail) result.getBody();
        assertNotNull(problem);
        assertEquals("Data integrity violation", problem.getDetail());
        assertEquals("http://etutor.dke.uni-linz.ac.at/errors/validation-error/data-integrity", problem.getType().toString());
    }

    @Test
    void handleDataIntegrityViolationExceptionWithoutCause() {
        // Arrange
        var request = mock(WebRequest.class);
        var ex = new org.springframework.dao.DataIntegrityViolationException("Data integrity violation");

        // Act
        var result = new ProblemDetailsExceptionHandler().handleDataIntegrityViolationException(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        var problem = (ProblemDetail) result.getBody();
        assertNotNull(problem);
        assertEquals("Data integrity violation", problem.getDetail());
        assertEquals("http://etutor.dke.uni-linz.ac.at/errors/validation-error/data-integrity", problem.getType().toString());
    }
}
