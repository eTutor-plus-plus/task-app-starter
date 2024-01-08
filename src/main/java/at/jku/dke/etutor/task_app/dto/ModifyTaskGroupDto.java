package at.jku.dke.etutor.task_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * Data transfer object for creating and updating task groups.
 *
 * @param taskGroupType  The task group type.
 * @param status         The status.
 * @param additionalData The task group type specific data.
 * @param <T>            The type of the additional data.
 */
@Schema(description = "Data for creating and updating task groups")
public record ModifyTaskGroupDto<T>(
    @Schema(description = "The task group type. If a task app supports more than one task group type, this can be used to distinguish between types.", example = "sql", examples = {"sql", "datalog"}) @NotEmpty @Size(max = 100) String taskGroupType,
    @Schema(description = "The task group status.") @NotNull TaskStatus status,
    @Schema(description = "The task group type specific data.") @NotNull T additionalData
) implements Serializable {
}
