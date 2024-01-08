package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.auth.AuthConstants;
import at.jku.dke.etutor.task_app.data.entities.TaskGroup;
import at.jku.dke.etutor.task_app.data.repositories.TaskGroupRepository;
import at.jku.dke.etutor.task_app.dto.ModifyTaskGroupDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * This class provides methods for managing {@link TaskGroup}s.
 *
 * @param <G> The task group type.
 * @param <S> The type of the additional data used in {@link ModifyTaskGroupDto}.
 */
@PreAuthorize(AuthConstants.CRUD_AUTHORITY)
public abstract class BaseTaskGroupService<G extends TaskGroup, S> implements TaskGroupService<G, S> {
    /**
     * The logger used in this class.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BaseTaskGroupService.class);

    /**
     * The task group repository.
     */
    protected final TaskGroupRepository<G> repository;

    /**
     * Creates a new instance of class {@link BaseTaskGroupService}.
     *
     * @param repository The task group repository.
     */
    protected BaseTaskGroupService(TaskGroupRepository<G> repository) {
        this.repository = repository;
    }

    //#region --- View ---

    /**
     * Returns the task group with the specified identifier.
     *
     * @param id The identifier.
     * @return The task group or an empty result if the task group does not exist.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<G> get(long id) {
        LOG.debug("Loading task group {}", id);
        return this.repository.findById(id);
    }

    //#endregion

    //#region --- Modify ---

    /**
     * Creates a new task group.
     *
     * @param id  The task group identifier.
     * @param dto The task group data.
     * @return The created task group.
     * @throws DuplicateKeyException If a task group with the specified identifier already exists.
     */
    @Override
    @Transactional
    public G create(long id, ModifyTaskGroupDto<S> dto) {
        if (this.repository.existsById(id))
            throw new DuplicateKeyException("Task group " + id + " already exists.");

        LOG.info("Creating task group {}", id);
        G taskGroup = this.createTaskGroup(id, dto);
        taskGroup.setId(id);
        taskGroup.setStatus(dto.status());

        taskGroup = this.repository.save(taskGroup);

        return taskGroup;
    }

    /**
     * Updates an existing task group.
     *
     * @param id  The task group identifier.
     * @param dto The new task group data.
     * @throws EntityNotFoundException If the task group does not exist.
     */
    @Override
    @Transactional
    public void update(long id, ModifyTaskGroupDto<S> dto) {
        var taskGroup = this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task group " + id + " does not exist."));

        LOG.info("Updating task group {}", id);
        taskGroup.setStatus(dto.status());
        this.updateTaskGroup(taskGroup, dto);

        this.repository.save(taskGroup);
    }

    /**
     * Deletes the task group with the specified identifier.
     *
     * @param id The identifier of the task group to delete.
     */
    @Override
    @Transactional
    public void delete(long id) {
        LOG.info("Deleting task {}", id);
        this.repository.deleteById(id);
    }

    //#endregion

    /**
     * Creates a new task group.
     *
     * @param id  The task group identifier.
     * @param dto The task group data.
     * @return The created task group.
     * @implSpec This method SHOULD NOT save the entity to the database as this is done by the caller. This method SHOULD ONLY set the task group type specific attributes
     * of the task group. The other attributes are set by the caller.
     */
    protected abstract G createTaskGroup(long id, ModifyTaskGroupDto<S> dto);

    /**
     * Sets the task group type specific attributes of the task group.
     *
     * @param taskGroup task The task group.
     * @param dto       The new task group data.
     */
    protected abstract void updateTaskGroup(G taskGroup, ModifyTaskGroupDto<S> dto);
}
