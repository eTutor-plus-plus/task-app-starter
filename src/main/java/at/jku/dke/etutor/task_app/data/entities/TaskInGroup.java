package at.jku.dke.etutor.task_app.data.entities;

/**
 * Interface for task entity in a task group.
 *
 * @param <T> The type of the task group.
 */
public interface TaskInGroup<T extends TaskGroup> extends Task {
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
