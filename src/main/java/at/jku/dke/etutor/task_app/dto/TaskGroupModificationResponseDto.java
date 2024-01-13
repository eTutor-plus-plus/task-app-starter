package at.jku.dke.etutor.task_app.dto;

/**
 * Response data for task group creation/modification.
 *
 * @param descriptionDe The german description to set (might be {@code null}).
 * @param descriptionEn The english description to set (might be {@code null}).
 */
public record TaskGroupModificationResponseDto(
    String descriptionDe,
    String descriptionEn) {
}
