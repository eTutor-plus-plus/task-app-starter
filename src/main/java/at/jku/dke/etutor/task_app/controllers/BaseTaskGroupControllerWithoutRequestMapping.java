package at.jku.dke.etutor.task_app.controllers;

import at.jku.dke.etutor.task_app.data.entities.TaskGroup;
import at.jku.dke.etutor.task_app.dto.ModifyTaskGroupDto;
import at.jku.dke.etutor.task_app.dto.TaskGroupModificationResponseDto;
import at.jku.dke.etutor.task_app.services.TaskGroupService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;

/**
 * Base implementation of {@link TaskGroupController}.
 * <p>
 * Add <code>@RestController</code> and <code>@RequestMapping("/api/taskGroup/[subpath]")</code> to the extending class.
 * If your task app only supports one task type, you can use {@link BaseTaskGroupController} instead.
 *
 * @param <E> The type of the task group entity.
 * @param <D> The type of the task group DTO.
 * @param <A> The type of the additional data in {@link ModifyTaskGroupDto}.
 */
public abstract class BaseTaskGroupControllerWithoutRequestMapping<E extends TaskGroup, D, A> implements TaskGroupController<D, A> {

    /**
     * The task group service.
     */
    protected final TaskGroupService<E, A> taskGroupService;

    /**
     * Creates a new instance of class {@link BaseTaskGroupControllerWithoutRequestMapping}.
     *
     * @param taskGroupService The task group service.
     */
    protected BaseTaskGroupControllerWithoutRequestMapping(TaskGroupService<E, A> taskGroupService) {
        this.taskGroupService = taskGroupService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<D> get(long id) {
        Optional<E> entity = this.taskGroupService.get(id);
        return entity
            .map(this::mapToDto)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Task group %s does not exist.", id)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TaskGroupModificationResponseDto> create(long id, ModifyTaskGroupDto<A> dto) {
        TaskGroupModificationResponseDto created = this.taskGroupService.create(id, dto);
        return ResponseEntity
            .created(this.createDetailsUri(id))
            .body(created);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TaskGroupModificationResponseDto> update(long id, ModifyTaskGroupDto<A> dto) {
        var updated = this.taskGroupService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Void> delete(long id) {
        this.taskGroupService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Maps the given task group to a DTO.
     *
     * @param entity The task group entity.
     * @return The DTO.
     */
    protected abstract D mapToDto(E entity);

    /**
     * Creates the URI for the details of the task group with the given ID.
     *
     * @param id The ID of the task group.
     * @return The URI where the details of the task group can be found.
     */
    protected abstract URI createDetailsUri(long id);

}
