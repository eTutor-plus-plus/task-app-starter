package at.jku.dke.etutor.task_app.controllers;

import at.jku.dke.etutor.task_app.data.entities.Task;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;

/**
 * Base implementation of {@link TaskController}.
 * <p>
 * Add <code>@RestController</code> to the extending class.
 *
 * @param <E> The type of the task entity.
 * @param <D> The type of the task DTO.
 * @param <A> The type of the additional data in {@link ModifyTaskDto}.
 */
public abstract class BaseTaskController<E extends Task<?>, D, A> implements TaskController<D, A> {

    /**
     * The task group service.
     */
    protected final TaskService<E, ?, A> taskService;

    /**
     * Creates a new instance of class {@link BaseTaskController}.
     *
     * @param taskService The task group service.
     */
    protected BaseTaskController(TaskService<E, ?, A> taskService) {
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
    public ResponseEntity<D> create(long id, ModifyTaskDto<A> dto) {
        E created = this.taskService.create(id, dto);
        return ResponseEntity
            .created(URI.create("/api/task/" + id))
            .body(this.mapToDto(created));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Void> update(long id, ModifyTaskDto<A> dto) {
        this.taskService.update(id, dto);
        return ResponseEntity.noContent().build();
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

}
