package at.jku.dke.etutor.task_app.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModifyTaskGroupDtoTest {
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
    void testTaskGroupTypeNullValidation() {
        // Arrange
        var dto = new ModifyTaskGroupDto<>(null, TaskStatus.DRAFT, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testTaskGroupTypeEmptyValidation() {
        // Arrange
        var dto = new ModifyTaskGroupDto<>("", TaskStatus.DRAFT, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testTaskGroupTypeSizeValidation() {
        // Arrange
        var dto = new ModifyTaskGroupDto<>("a".repeat(101), TaskStatus.DRAFT, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testTaskStatusNullValidation() {
        // Arrange
        var dto = new ModifyTaskGroupDto<>("a".repeat(100), null, "");

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testAdditionalDataNullValidation() {
        // Arrange
        var dto = new ModifyTaskGroupDto<>("test", TaskStatus.DRAFT, null);

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    @Test
    void testAdditionalDataValidValidation() {
        // Arrange
        var dto = new ModifyTaskGroupDto<>("test", TaskStatus.DRAFT, new TestData(null));

        // Act
        var violations = this.validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
    }

    private record TestData(@NotNull String data) {
    }
}
