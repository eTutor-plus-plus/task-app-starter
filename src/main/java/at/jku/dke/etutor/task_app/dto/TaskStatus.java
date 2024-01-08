package at.jku.dke.etutor.task_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Provides the task (group) statuses.
 */
public enum TaskStatus {
    /**
     * Task (group) is in draft-mode and must not be used in production.
     */
    @Schema(description = "Task (group) is in draft-mode and must not be used in production.")
    DRAFT,

    /**
     * Task (group) is ready for approval and must not be used in production.
     */
    @Schema(description = "Task (group) is ready for approval and must not be used in production.")
    READY_FOR_APPROVAL,

    /**
     * Task (group) is approved and can be used in production.
     */
    @Schema(description = "Task (group) is approved and can be used in production.")
    APPROVED
}
