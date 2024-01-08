package at.jku.dke.etutor.task_app.data.entities;

import at.jku.dke.etutor.task_app.dto.TaskStatus;

import java.math.BigDecimal;

/**
 * Interface for task entity.
 *
 * @param <T> The type of the task group.
 */
public interface Task<T extends TaskGroup> {
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
     * Gets the max points.
     *
     * @return The max points.
     */
    BigDecimal getMaxPoints();

    /**
     * Sets the max points.
     *
     * @param maxPoints The max points.
     */
    void setMaxPoints(BigDecimal maxPoints);

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

    /**
     * Gets the task group.
     *
     * @return The task group.
     */
    T getTaskGroup();

    /**
     * Sets the task group.
     *
     * @param taskGroup The task group.
     */
    void setTaskGroup(T taskGroup);
}
