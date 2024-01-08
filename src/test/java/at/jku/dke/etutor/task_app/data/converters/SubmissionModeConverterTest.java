package at.jku.dke.etutor.task_app.data.converters;

import at.jku.dke.etutor.task_app.dto.SubmissionMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionModeConverterTest {

    //#region --- convertToDatabaseColumn ---
    @Test
    void testConvertToDatabaseColumn() {
        // Arrange
        var converter = new SubmissionModeConverter();
        var status = SubmissionMode.DIAGNOSE;

        // Act
        var result = converter.convertToDatabaseColumn(status);

        // Assert
        assertEquals(status.name().toLowerCase(), result);
    }

    @Test
    void testConvertToDatabaseColumnNullValue() {
        // Arrange
        var converter = new SubmissionModeConverter();

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
        var converter = new SubmissionModeConverter();
        var status = SubmissionMode.SUBMIT;

        // Act
        var result = converter.convertToEntityAttribute(status.name().toLowerCase());

        // Assert
        assertEquals(status, result);
    }

    @Test
    void testConvertToEntityAttributeNullValue() {
        // Arrange
        var converter = new SubmissionModeConverter();

        // Act
        var result = converter.convertToEntityAttribute(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testConvertToEntityAttributeInvalidValueThrowsException() {
        // Arrange
        var converter = new SubmissionModeConverter();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("invalid"));
    }
    //#endregion

}
