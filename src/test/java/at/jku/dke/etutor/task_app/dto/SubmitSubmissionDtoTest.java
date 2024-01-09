package at.jku.dke.etutor.task_app.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubmitSubmissionDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        //noinspection resource
        this.validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();
    }

    @Test
    void testUserIdSizeValidation() {
        // Arrange
        var dto = new SubmitSubmissionDto<>("a".repeat(256), null, 1L, "de", SubmissionMode.SUBMIT, 1, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testAssignmentIdSizeValidation() {
        // Arrange
        var dto = new SubmitSubmissionDto<>(null, "a".repeat(256), 1L, "de", SubmissionMode.SUBMIT, 1, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testTaskIdNullValidation() {
        // Arrange
        var dto = new SubmitSubmissionDto<>(null, null, null, "de", SubmissionMode.SUBMIT, 1, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testLanguageNullValidation() {
        // Arrange
        var dto = new SubmitSubmissionDto<>(null, null, 1L, null, SubmissionMode.SUBMIT, 1, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testLanguagePatternValidation() {
        // Arrange
        var dto = new SubmitSubmissionDto<>(null, null, 1L, "es", SubmissionMode.SUBMIT, 1, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testSubmissionModeNullValidation() {
        // Arrange
        var dto = new SubmitSubmissionDto<>(null, null, 1L, "de", null, 1, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testFeedbackLevelNullValidation() {
        // Arrange
        var dto = new SubmitSubmissionDto<>(null, null, 1L, "de", SubmissionMode.SUBMIT, null, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testFeedbackLevelMinValidation() {
        // Arrange
        var dto = new SubmitSubmissionDto<>(null, null, 1L, "de", SubmissionMode.SUBMIT, -1, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testFeedbackLevelMaxValidation() {
        // Arrange
        var dto = new SubmitSubmissionDto<>(null, null, 1L, "de", SubmissionMode.SUBMIT, 4, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testSubmissionNullValidation() {
        // Arrange
        var dto = new SubmitSubmissionDto<>(null, null, 1L, "de", SubmissionMode.SUBMIT, 3, null);

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testSubmissionInvalidValidation() {
        // Arrange
        var dto = new SubmitSubmissionDto<>(null, null, 1L, "de", SubmissionMode.SUBMIT, 3, new TestData(null));

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    private record TestData(@NotNull String data) {
    }
}
