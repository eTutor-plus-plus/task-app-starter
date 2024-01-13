package at.jku.dke.etutor.task_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * Data transfer object for submitting a submission.
 *
 * @param userId        The user identifier (can be used e.g. for plagiarism check).
 * @param assignmentId  The assignment identifier (can be used e.g. for plagiarism check).
 * @param taskId        The task identifier.
 * @param language      The language of the submitters' user interface (either "de" or "en").
 * @param mode          The submission mode.
 * @param feedbackLevel The feedback level.
 * @param submission    The submission.
 * @param <T>           The type of the submission.
 */
@Schema(description = "Data for a submission that should be evaluated and graded")
public record SubmitSubmissionDto<T>(
    @Schema(description = "The user identifier (can be used e.g. for plagiarism check).", example = "12212345", examples = {"12212345", "student1"}) @Size(max = 255) String userId,
    @Schema(description = "The assignment identifier (can be used e.g. for plagiarism check).", example = "moodle_quiz_3", examples = {"moodle_quiz_3", "1"}) @Size(max = 255) String assignmentId,
    @Schema(description = "The task identifier.", example = "8911") @NotNull Long taskId,
    @Schema(description = "The user interface language.") @NotNull @Size(min = 2, max = 2) @Pattern(regexp = "de|en") String language,
    @Schema(description = "The submission mode.") @NotNull SubmissionMode mode,
    @Schema(description = "The feedback level.") @NotNull @Min(0) @Max(3) Integer feedbackLevel,
    @Schema(description = "The submission data.") @NotNull @Valid T submission
) implements Serializable {
}
