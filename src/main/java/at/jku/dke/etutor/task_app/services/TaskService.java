package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.data.entities.Task;
import at.jku.dke.etutor.task_app.data.entities.TaskGroup;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.dto.TaskModificationResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * Interface for classes that manage {@link Task}s.
 *
 * @param <T> The task type.
 * @param <G> The task group type.
 * @param <S> The type of the additional data used in {@link ModifyTaskDto}.
 */
@Validated
public interface TaskService<T extends Task<G>, G extends TaskGroup, S> {
    /**
     * Returns the task with the specified identifier.
     *
     * @param id The identifier.
     * @return The task or an empty result if the task does not exist.
     */
    Optional<T> get(long id);

    /**
     * Creates a new task.
     *
     * @param id  The task identifier.
     * @param dto The task data.
     * @return The data that should be sent to the task administration UI (might be {@code null}).
     * @throws DuplicateKeyException If a task with the specified identifier already exists.
     */
    TaskModificationResponseDto create(long id, @Valid ModifyTaskDto<S> dto);

    /**
     * Updates an existing task.
     *
     * @param id  The task identifier.
     * @param dto The new task data.
     * @return The data that should be sent to the task administration UI (might be {@code null}).
     * @throws EntityNotFoundException If the task does not exist.
     */
    TaskModificationResponseDto update(long id, @Valid ModifyTaskDto<S> dto);

    /**
     * Deletes the task with the specified identifier.
     *
     * @param id The identifier of the task to delete.
     */
    void delete(long id);
}
