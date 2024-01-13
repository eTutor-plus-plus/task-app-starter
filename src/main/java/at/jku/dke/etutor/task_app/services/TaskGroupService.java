package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.data.entities.TaskGroup;
import at.jku.dke.etutor.task_app.dto.ModifyTaskGroupDto;
import at.jku.dke.etutor.task_app.dto.TaskGroupModificationResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * Interface for classes that manage {@link TaskGroup}s.
 *
 * @param <G> The task group type.
 * @param <S> The type of the additional data used in {@link ModifyTaskGroupDto}.
 */
@Validated
public interface TaskGroupService<G extends TaskGroup, S> {
    /**
     * Returns the task group with the specified identifier.
     *
     * @param id The identifier.
     * @return The task group or an empty result if the task group does not exist.
     */
    Optional<G> get(long id);

    /**
     * Creates a new task group.
     *
     * @param id  The task group identifier.
     * @param dto The task group data.
     * @return The data that should be sent to the task administration UI (might be {@code null}).
     * @throws DuplicateKeyException If a task group with the specified identifier already exists.
     */
    TaskGroupModificationResponseDto create(long id, @Valid ModifyTaskGroupDto<S> dto);

    /**
     * Updates an existing task group.
     *
     * @param id  The task group identifier.
     * @param dto The new task group data.
     * @return The data that should be sent to the task administration UI (might be {@code null}).
     * @throws EntityNotFoundException If the task group does not exist.
     */
    TaskGroupModificationResponseDto update(long id, @Valid ModifyTaskGroupDto<S> dto);

    /**
     * Deletes the task group with the specified identifier.
     *
     * @param id The identifier of the task group to delete.
     */
    void delete(long id);
}
