package at.jku.dke.etutor.task_app.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a grading result.
 *
 * @param submissionId The submission identifier.
 * @param grading      The grading result.
 */
public record GradingResultDto(UUID submissionId, @NotNull GradingDto grading) implements Serializable {
}
