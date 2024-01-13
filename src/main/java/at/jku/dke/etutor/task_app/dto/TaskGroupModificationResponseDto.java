package at.jku.dke.etutor.task_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response data for task group creation/modification.
 *
 * @param descriptionDe The german description to set (might be {@code null}).
 * @param descriptionEn The english description to set (might be {@code null}).
 */
public record TaskGroupModificationResponseDto(
    @Schema(description = "The german description.", example = "Gegeben ist ein konzeptuelles Modell.") String descriptionDe,
    @Schema(description = "The english description.", example = "Given is a conceptual model.") String descriptionEn) {
}
