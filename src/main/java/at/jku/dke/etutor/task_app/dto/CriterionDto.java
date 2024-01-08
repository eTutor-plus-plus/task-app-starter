package at.jku.dke.etutor.task_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents a grading criterion result.
 *
 * @param name     The name of the criterion.
 * @param points   The achieved points (just for information, not used for total grading, might be {@code null}).
 * @param passed   Whether the criterion was passed (might influence display, not used for anything else).
 * @param feedback The feedback for the criterion (can be HTML).
 */
@Schema(description = "Criterion of a grading result")
public record CriterionDto(
    @Schema(examples = {"Syntax", "Correct Order"}, example = "Syntax", description = "The name of the criterion.") @NotEmpty String name,
    @Schema(description = "The achieved points (just for information, not used for total grading, might be <code>null</code>).", example = "1.5") BigDecimal points,
    @Schema(description = "Whether the criterion was passed (might influence display, not used for anything else).") @NotNull boolean passed,
    @Schema(description = "The feedback for the criterion (can be HTML).", example = "<p>Your solution has syntax errors.</p>", examples = {"<p>Your solution produces following output: 12334</p>", "1 column is missing"}) @NotNull String feedback
) implements Serializable {
}
