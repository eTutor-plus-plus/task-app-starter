package at.jku.dke.etutor.task_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object for a persisted submission.
 *
 * @param id               The submission identifier.
 * @param userId           The user identifier.
 * @param assignmentId     The assignment identifier.
 * @param taskId           The task identifier.
 * @param submissionTime   The submission time.
 * @param language         The language of the submitters' user interface (either "de" or "en").
 * @param feedbackLevel    The feedback level.
 * @param submission       The submission.
 * @param mode             The submission mode.
 * @param evaluationResult The result of the evaluation.
 * @param <T>              The type of the submission.
 */
@Schema(description = "Submitted, evaluated, graded and persisted submission")
public record SubmissionDto<T>(
    @Schema(description = "The submissions' unique identifier.") @NotNull UUID id,
    @Schema(description = "The submission user identifier.", example = "12212345", examples = {"12212345", "student1"}) @Size(max = 255) String userId,
    @Schema(description = "The assignment identifier.", example = "moodle_quiz_3", examples = {"moodle_quiz_3", "1"}) @Size(max = 255) String assignmentId,
    @Schema(description = "The task identifier.", example = "7789") @NotNull long taskId,
    @Schema(description = "The submission timestamp.") @NotNull @PastOrPresent Instant submissionTime,
    @Schema(description = "The user interface language.") @NotNull @Size(min = 2, max = 2) @Pattern(regexp = "de|en") String language,
    @Schema(description = "The requested feedback level.") @NotNull @Min(0) @Max(3) int feedbackLevel,
    @Schema(description = "The submission mode.") @NotNull SubmissionMode mode,
    @Schema(description = "The submission data.") @NotNull T submission,
    @Schema(description = "The evaluation and grading result.") GradingDto evaluationResult) implements Serializable {
}
