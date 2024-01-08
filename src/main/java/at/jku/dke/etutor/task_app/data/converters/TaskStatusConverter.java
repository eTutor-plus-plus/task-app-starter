package at.jku.dke.etutor.task_app.data.converters;

import at.jku.dke.etutor.task_app.dto.TaskStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

/**
 * Concerts the database {@code task_status} enum to the java {@link TaskStatus} enum and to/from string.
 */
@Converter
public class TaskStatusConverter implements AttributeConverter<TaskStatus, String> {
    /**
     * Creates a new instance of class {@linkplain TaskStatusConverter}.
     */
    public TaskStatusConverter() {
    }

    @Override
    public String convertToDatabaseColumn(TaskStatus taskStatus) {
        if (taskStatus == null)
            return null;
        return taskStatus.name().toLowerCase();
    }

    @Override
    public TaskStatus convertToEntityAttribute(String value) {
        if (value == null)
            return null;

        return Stream.of(TaskStatus.values())
            .filter(g -> g.name().toLowerCase().equals(value))
            .findAny().orElseThrow(IllegalArgumentException::new);
    }
}
