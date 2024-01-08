package at.jku.dke.etutor.task_app.data.entities;

import at.jku.dke.etutor.task_app.dto.TaskStatus;

/**
 * Interface for task group entity.
 */
public interface TaskGroup {
    /**
     * Gets the id.
     *
     * @return The id.
     */
    Long getId();

    /**
     * Sets the id.
     *
     * @param id The id.
     */
    void setId(Long id);

    /**
     * Gets the status.
     *
     * @return The status.
     */
    TaskStatus getStatus();

    /**
     * Sets the status.
     *
     * @param status The status.
     */
    void setStatus(TaskStatus status);
}
