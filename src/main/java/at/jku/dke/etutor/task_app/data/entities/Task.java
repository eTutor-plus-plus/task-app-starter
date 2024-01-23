package at.jku.dke.etutor.task_app.data.entities;

import at.jku.dke.etutor.task_app.dto.TaskStatus;

import java.math.BigDecimal;

/**
 * Interface for task entity.
 */
public interface Task {
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
}
