package at.jku.dke.etutor.task_app.dto;

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
    String descriptionDe,
    String descriptionEn,
    Short difficulty,
    BigDecimal maxPoints) {

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
