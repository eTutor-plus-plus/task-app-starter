package at.jku.dke.etutor.task_app.data.converters;

import at.jku.dke.etutor.task_app.dto.SubmissionMode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

/**
 * Concerts the database {@code submission_mode} enum to the java {@link SubmissionMode} enum and to/from string.
 */
@Converter
public class SubmissionModeConverter implements AttributeConverter<SubmissionMode, String> {
    /**
     * Creates a new instance of class {@linkplain SubmissionModeConverter}.
     */
    public SubmissionModeConverter() {
    }

    @Override
    public String convertToDatabaseColumn(SubmissionMode submissionMode) {
        if (submissionMode == null)
            return null;
        return submissionMode.name().toLowerCase();
    }

    @Override
    public SubmissionMode convertToEntityAttribute(String value) {
        if (value == null)
            return null;

        return Stream.of(SubmissionMode.values())
            .filter(g -> g.name().toLowerCase().equals(value))
            .findAny().orElseThrow(IllegalArgumentException::new);
    }
}
