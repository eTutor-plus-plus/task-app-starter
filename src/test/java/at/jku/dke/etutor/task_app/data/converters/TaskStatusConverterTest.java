package at.jku.dke.etutor.task_app.data.converters;

import at.jku.dke.etutor.task_app.dto.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatusConverterTest {

    //#region --- convertToDatabaseColumn ---
    @Test
    void testConvertToDatabaseColumn() {
        // Arrange
        var converter = new TaskStatusConverter();
        var status = TaskStatus.READY_FOR_APPROVAL;

        // Act
        var result = converter.convertToDatabaseColumn(status);

        // Assert
        assertEquals(status.name().toLowerCase(), result);
    }

    @Test
    void testConvertToDatabaseColumnNullValue() {
        // Arrange
        var converter = new TaskStatusConverter();

        // Act
        var result = converter.convertToDatabaseColumn(null);

        // Assert
        assertNull(result);
    }
    //#endregion

    //#region --- convertToEntityAttribute ---
    @Test
    void testConvertToEntityAttribute() {
        // Arrange
        var converter = new TaskStatusConverter();
        var status = TaskStatus.READY_FOR_APPROVAL;

        // Act
        var result = converter.convertToEntityAttribute(status.name().toLowerCase());

        // Assert
        assertEquals(status, result);
    }

    @Test
    void testConvertToEntityAttributeNullValue() {
        // Arrange
        var converter = new TaskStatusConverter();

        // Act
        var result = converter.convertToEntityAttribute(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testConvertToEntityAttributeInvalidValueThrowsException() {
        // Arrange
        var converter = new TaskStatusConverter();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("invalid"));
    }
    //#endregion

}
