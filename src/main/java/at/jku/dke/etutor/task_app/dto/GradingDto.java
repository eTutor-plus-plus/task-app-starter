package at.jku.dke.etutor.task_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data transfer object for grading of a submission.
 *
 * @param maxPoints       The maximum reachable points.
 * @param points          The achieved points.
 * @param generalFeedback The general feedback (can be HTML).
 * @param criteria        The criteria containing the specific feedback.
 */
@Schema(description = "Grading and evaluation result of a submission")
public record GradingDto(
    @Schema(description = "The maximum reachable points.", example = "10") @NotNull @PositiveOrZero BigDecimal maxPoints,
    @Schema(description = "The achieved points.", example = "7.5") @NotNull BigDecimal points,
    @Schema(description = "The general feedback (can be HTML).", example = "Your solution is correct.", examples = {"Your solution is correct.", "Your solution is incorrect."}) String generalFeedback,
    @Schema(description = "The criteria containing the specific feedback.") @NotNull List<CriterionDto> criteria
) implements Serializable {
}
