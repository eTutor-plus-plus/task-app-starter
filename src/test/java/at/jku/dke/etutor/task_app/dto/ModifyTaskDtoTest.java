package at.jku.dke.etutor.task_app.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModifyTaskDtoTest {
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
    void testTaskGroupIdNullValidation() {
        // Arrange
        var dto = new ModifyTaskDto<>(null, BigDecimal.TEN, "test", TaskStatus.DRAFT, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testMaxPointsNullValidation() {
        // Arrange
        var dto = new ModifyTaskDto<>(1L, null, "test", TaskStatus.DRAFT, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testMaxPointsPositiveValidation() {
        // Arrange
        var dto = new ModifyTaskDto<>(1L, BigDecimal.ONE.negate(), "test", TaskStatus.DRAFT, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testTaskTypeNullValidation() {
        // Arrange
        var dto = new ModifyTaskDto<>(1L, BigDecimal.ZERO, null, TaskStatus.DRAFT, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testTaskTypeEmptyValidation() {
        // Arrange
        var dto = new ModifyTaskDto<>(1L, BigDecimal.ZERO, "", TaskStatus.DRAFT, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testTaskTypeSizeValidation() {
        // Arrange
        var dto = new ModifyTaskDto<>(1L, BigDecimal.ZERO, "a".repeat(101), TaskStatus.DRAFT, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testTaskStatusNullValidation() {
        // Arrange
        var dto = new ModifyTaskDto<>(1L, BigDecimal.ZERO, "test", null, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testAdditionalDataNullValidation() {
        // Arrange
        var dto = new ModifyTaskDto<>(1L, BigDecimal.ZERO, "test", TaskStatus.DRAFT, null);

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testAdditionalDataValidValidation() {
        // Arrange
        var dto = new ModifyTaskDto<>(1L, BigDecimal.ZERO, "test", TaskStatus.DRAFT, new TestData(null));

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    private record TestData(@NotNull String data){}
}
