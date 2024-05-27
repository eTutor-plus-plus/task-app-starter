package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.auth.AuthConstants;
import at.jku.dke.etutor.task_app.data.entities.Task;
import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.dto.TaskModificationResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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
 * @param <S> The type of the additional data used in {@link ModifyTaskDto}.
 */
@PreAuthorize(AuthConstants.CRUD_AUTHORITY)
public abstract class BaseTaskService<T extends Task, S> implements TaskService<T, S> {
    /**
     * The logger used in this class.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BaseTaskService.class);

    /**
     * The task repository.
     */
    protected final TaskRepository<T> repository;

    /**
     * Creates a new instance of class {@link BaseTaskService}.
     *
     * @param repository The task repository.
     */
    protected BaseTaskService(TaskRepository<T> repository) {
        this.repository = repository;
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
     * @return The data that should be sent to the task administration UI (might be {@code null}).
     * @throws DuplicateKeyException If a task with the specified identifier already exists.
     */
    @Override
    @Transactional
    public TaskModificationResponseDto create(long id, @Valid ModifyTaskDto<S> dto) {
        if (this.repository.existsById(id))
            throw new DuplicateKeyException("Task " + id + " already exists.");

        LOG.info("Creating task {}", id);
        T task = this.createTask(id, dto);
        task.setId(id);
        task.setStatus(dto.status());
        task.setMaxPoints(dto.maxPoints());
        this.beforeCreateInternal(task, dto);

        this.beforeCreate(task, dto);
        task = this.repository.save(task);
        this.afterCreate(task, dto);

        return this.mapToReturnData(task, true);
    }

    /**
     * Updates an existing task.
     *
     * @param id  The task identifier.
     * @param dto The new task data.
     * @return The data that should be sent to the task administration UI (might be {@code null}).
     * @throws EntityNotFoundException If the task does not exist.
     */
    @Override
    @Transactional
    public TaskModificationResponseDto update(long id, @Valid ModifyTaskDto<S> dto) {
        var task = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task " + id + " does not exist."));

        LOG.info("Updating task {}", id);
        task.setStatus(dto.status());
        task.setMaxPoints(dto.maxPoints());
        this.updateInternal(task, dto);
        this.updateTask(task, dto);

        task = this.repository.save(task);
        this.afterUpdate(task, dto);

        return this.mapToReturnData(task, false);
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
        this.beforeDelete(id);
        this.repository.deleteById(id);
        this.afterDelete(id);
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

    /**
     * Maps the task to the data that should be returned to the task administration UI.
     *
     * @param task   The task.
     * @param create {@code true}, if the specified task was just created; {@code false} if the task was updated.
     * @return The data to send.
     */
    protected TaskModificationResponseDto mapToReturnData(T task, boolean create) {
        return new TaskModificationResponseDto(null, null, null, null);
    }

    /**
     * Called before the task is stored in the database.
     * Only available for internal classes.
     *
     * @param task The task to create.
     * @param dto  The task data.
     */
    void beforeCreateInternal(T task, ModifyTaskDto<S> dto) {
    }

    /**
     * Called before the task is updated in the database.
     * Only available for internal classes.
     *
     * @param task The task to update.
     * @param dto  The task data.
     */
    void updateInternal(T task, ModifyTaskDto<S> dto) {
    }

    /**
     * Called before the task is stored in the database.
     * <p>
     * Override this method to perform additional actions before creating the task.
     *
     * @param task The task to create.
     * @param dto  The task data.
     */
    protected void beforeCreate(T task, ModifyTaskDto<S> dto) {
    }

    /**
     * Called after the task is stored in the database.
     * <p>
     * This method runs in the same transaction as the calling method.
     * Override this method to perform additional actions after creating the task.
     *
     * @param task The created task.
     * @param dto  The task data.
     */
    protected void afterCreate(T task, ModifyTaskDto<S> dto) {
    }

    /**
     * Called after the task is updated in the database.
     * <p>
     * This method runs in the same transaction as the calling method.
     * Override this method to perform additional actions after updating the task.
     *
     * @param task The updated task.
     * @param dto  The task data.
     */
    protected void afterUpdate(T task, ModifyTaskDto<S> dto) {
    }

    /**
     * Called before the task with the specified identifier is deleted.
     * <p>
     * Override this method to perform additional actions before deleting the task.
     *
     * @param id The identifier of the task to delete.
     */
    protected void beforeDelete(long id) {
    }

    /**
     * Called after the task with the specified identifier is deleted.
     * <p>
     * This method runs in the same transaction as the calling method.
     * Override this method to perform additional actions after deleting the task.
     *
     * @param id The identifier of the deleted task.
     */
    protected void afterDelete(long id) {
    }
}
