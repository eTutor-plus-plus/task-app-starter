package at.jku.dke.etutor.task_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Response data for task creation/modification.
 *
 * @param descriptionDe The german description to set (might be {@code null}).
 * @param descriptionEn The english description to set (might be {@code null}).
 * @param difficulty    The difficulty to set (might be {@code null}).
 * @param maxPoints     The maximum points to set (might be {@code null}).
 */
public record TaskModificationResponseDto(
    @Schema(description = "The german description.", example = "Geben Sie eine Zahl ein.") String descriptionDe,
    @Schema(description = "The english description.", example = "Enter a number.") String descriptionEn,
    @Schema(description = "The difficulty to set.", example = "2", examples = {"2", "0"}) @Min(0) @Max(3) Short difficulty,
    @Schema(description = "The maximum points to set.", example = "25.75", examples = {"25.75", "100"}) @Positive BigDecimal maxPoints) {

    /**
     * Creates a new instance of class {@link TaskGroupModificationResponseDto}.
     *
     * @param descriptionDe The german description.
     * @param descriptionEn The english description.
     */
    public TaskModificationResponseDto(String descriptionDe, String descriptionEn) {
        this(descriptionDe, descriptionEn, null, null);
    }

    /**
     * Creates a new instance of class {@link TaskModificationResponseDto}.
     *
     * @param difficulty The difficulty.
     */
    public TaskModificationResponseDto(short difficulty) {
        this(null, null, difficulty, null);
    }

    /**
     * Creates a new instance of class {@link TaskModificationResponseDto}.
     *
     * @param maxPoints The maximum points.
     */
    public TaskModificationResponseDto(BigDecimal maxPoints) {
        this(null, null, null, maxPoints);
    }
}
