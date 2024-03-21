package at.jku.dke.etutor.task_app.controllers;

import at.jku.dke.etutor.task_app.data.entities.Task;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.dto.TaskModificationResponseDto;
import at.jku.dke.etutor.task_app.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;

/**
 * Base implementation of {@link TaskController}.
 * <p>
 * Add <code>@RestController</code> and <code>@RequestMapping("/api/task/[subpath]")</code> to the extending class.
 * If your task app only supports one task type, you can use {@link BaseTaskController} instead.
 *
 * @param <E> The type of the task entity.
 * @param <D> The type of the task DTO.
 * @param <A> The type of the additional data in {@link ModifyTaskDto}.
 */
public abstract class BaseTaskControllerWithoutRequestMapping<E extends Task, D, A> implements TaskController<D, A> {

    /**
     * The task group service.
     */
    protected final TaskService<E, A> taskService;

    /**
     * Creates a new instance of class {@link BaseTaskControllerWithoutRequestMapping}.
     *
     * @param taskService The task group service.
     */
    protected BaseTaskControllerWithoutRequestMapping(TaskService<E, A> taskService) {
        this.taskService = taskService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<D> get(long id) {
        Optional<E> entity = this.taskService.get(id);
        return entity
            .map(this::mapToDto)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Task %s does not exist.", id)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TaskModificationResponseDto> create(long id, ModifyTaskDto<A> dto) {
        TaskModificationResponseDto created = this.taskService.create(id, dto);
        return ResponseEntity
            .created(this.createDetailsUri(id))
            .body(created);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TaskModificationResponseDto> update(long id, ModifyTaskDto<A> dto) {
        TaskModificationResponseDto updated = this.taskService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Void> delete(long id) {
        this.taskService.delete(id);
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
     * Creates the URI for the details of the task with the given ID.
     *
     * @param id The ID of the task.
     * @return The URI where the details of the task can be found.
     */
    protected abstract URI createDetailsUri(long id);

}
