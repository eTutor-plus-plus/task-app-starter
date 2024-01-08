package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.auth.AuthConstants;
import at.jku.dke.etutor.task_app.data.entities.Task;
import at.jku.dke.etutor.task_app.data.entities.TaskGroup;
import at.jku.dke.etutor.task_app.data.repositories.TaskGroupRepository;
import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * This class provides methods for managing {@link Task}s.
 *
 * @param <T> The task type.
 * @param <G> The task group type.
 * @param <S> The type of the additional data used in {@link ModifyTaskDto}.
 */
@PreAuthorize(AuthConstants.CRUD_AUTHORITY)
public abstract class BaseTaskService<T extends Task<G>, G extends TaskGroup, S> implements TaskService<T, G, S> {
    /**
     * The logger used in this class.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BaseTaskService.class);

    /**
     * The task repository.
     */
    protected final TaskRepository<T> repository;

    /**
     * The task group repository.
     */
    protected final TaskGroupRepository<G> taskGroupRepository;

    /**
     * Creates a new instance of class {@link BaseTaskService}.
     *
     * @param repository          The task repository.
     * @param taskGroupRepository The task group repository.
     */
    protected BaseTaskService(TaskRepository<T> repository, TaskGroupRepository<G> taskGroupRepository) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
    }

    //#region --- View ---

    /**
     * Returns the task with the specified identifier.
     *
     * @param id The identifier.
     * @return The task or an empty result if the task does not exist.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<T> get(long id) {
        LOG.debug("Loading task {}", id);
        return this.repository.findById(id);
    }

    //#endregion

    //#region --- Modify ---

    /**
     * Creates a new task.
     *
     * @param id  The task identifier.
     * @param dto The task data.
     * @return The created task.
     * @throws DuplicateKeyException If a task with the specified identifier already exists.
     */
    @Override
    @Transactional
    public T create(long id, ModifyTaskDto<S> dto) {
        if (this.repository.existsById(id))
            throw new DuplicateKeyException("Task " + id + " already exists.");

        LOG.info("Creating task {}", id);
        T task = this.createTask(id, dto);
        task.setId(id);
        task.setStatus(dto.status());
        task.setMaxPoints(dto.maxPoints());
        task.setTaskGroup(this.taskGroupRepository.getReferenceById(dto.taskGroupId()));

        task = this.repository.save(task);

        return task;
    }

    /**
     * Updates an existing task.
     *
     * @param id  The task identifier.
     * @param dto The new task data.
     * @throws EntityNotFoundException If the task does not exist.
     */
    @Override
    @Transactional
    public void update(long id, ModifyTaskDto<S> dto) {
        var task = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task " + id + " does not exist."));

        LOG.info("Updating task {}", id);
        task.setStatus(dto.status());
        task.setMaxPoints(dto.maxPoints());
        task.setTaskGroup(this.taskGroupRepository.getReferenceById(dto.taskGroupId()));
        this.updateTask(task, dto);

        this.repository.save(task);
    }

    /**
     * Deletes the task with the specified identifier.
     *
     * @param id The identifier of the task to delete.
     */
    @Override
    @Transactional
    public void delete(long id) {
        LOG.info("Deleting task {}", id);
        this.repository.deleteById(id);
    }

    //#endregion

    /**
     * Creates a new task.
     *
     * @param id  The task identifier.
     * @param dto The task data.
     * @return The created task.
     * @implSpec This method SHOULD NOT save the entity to the database as this is done by the caller. This method SHOULD ONLY set the task type specific attributes of the task.
     * The other attributes are set by the caller.
     */
    protected abstract T createTask(long id, ModifyTaskDto<S> dto);

    /**
     * Sets the task type specific attributes of the task.
     *
     * @param task The task.
     * @param dto  The new task data.
     */
    protected abstract void updateTask(T task, ModifyTaskDto<S> dto);
}
