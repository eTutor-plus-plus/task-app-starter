package at.jku.dke.etutor.task_app.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskModificationResponseDtoTest {
    @Test
    void testConstructor1() {
        // Arrange
        String descriptionDe = "Geben Sie eine Zahl ein.";
        String descriptionEn = "Enter a number.";

        // Act
        TaskModificationResponseDto dto = new TaskModificationResponseDto(descriptionDe, descriptionEn);

        // Assert
        assertEquals(dto.descriptionDe(), descriptionDe);
        assertEquals(dto.descriptionEn(), descriptionEn);
        assertNull(dto.difficulty());
        assertNull(dto.maxPoints());
    }

    @Test
    void testConstructor2() {
        // Arrange
        short difficulty = 2;

        // Act
        TaskModificationResponseDto dto = new TaskModificationResponseDto(difficulty);

        // Assert
        assertEquals(dto.difficulty(), difficulty);
        assertNull(dto.maxPoints());
        assertNull(dto.descriptionDe());
        assertNull(dto.descriptionEn());
    }

    @Test
    void testConstructor3() {
        // Arrange
        BigDecimal maxPoints = BigDecimal.TWO;

        // Act
        TaskModificationResponseDto dto = new TaskModificationResponseDto(maxPoints);

        // Assert
        assertEquals(dto.maxPoints(), maxPoints);
        assertNull(dto.difficulty());
        assertNull(dto.descriptionDe());
        assertNull(dto.descriptionEn());
    }
}
