package at.jku.dke.etutor.task_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Data transfer object for creating and updating tasks.
 *
 * @param taskGroupId    The task group id.
 * @param maxPoints      The maximum achievable points.
 * @param taskType       The task type.
 * @param status         The status.
 * @param additionalData The task type specific data.
 * @param <T>            The type of the additional data.
 */
@Schema(description = "Data for creating and updating tasks")
public record ModifyTaskDto<T>(
    @Schema(description = "The task group identifier.", example = "445") Long taskGroupId,
    @Schema(description = "The maximum achievable points.", example = "10") @NotNull @PositiveOrZero BigDecimal maxPoints,
    @Schema(description = "The task type. If a task app supports more than one task type, this can be used to distinguish between types.", example = "sql", examples = {"sql", "datalog"}) @NotEmpty @Size(max = 100) String taskType,
    @Schema(description = "The task status.") @NotNull TaskStatus status,
    @Schema(description = "The task type specific data.") @Valid @NotNull T additionalData
) implements Serializable {
}
