package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.data.entities.Task;
import at.jku.dke.etutor.task_app.data.entities.TaskGroup;
import at.jku.dke.etutor.task_app.data.entities.TaskInGroup;
import at.jku.dke.etutor.task_app.data.repositories.TaskGroupRepository;
import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import jakarta.validation.ValidationException;

/**
 * This class provides methods for managing {@link Task}s.
 *
 * @param <T> The task type.
 * @param <G> The task group type.
 * @param <S> The type of the additional data used in {@link ModifyTaskDto}.
 */
public abstract class BaseTaskInGroupService<T extends TaskInGroup<G>, G extends TaskGroup, S> extends BaseTaskService<T, S> {

    /**
     * The task group repository.
     */
    protected final TaskGroupRepository<G> taskGroupRepository;

    /**
     * Creates a new instance of class {@link BaseTaskInGroupService}.
     *
     * @param repository          The task repository.
     * @param taskGroupRepository The task group repository.
     */
    protected BaseTaskInGroupService(TaskRepository<T> repository, TaskGroupRepository<G> taskGroupRepository) {
        super(repository);
        this.taskGroupRepository = taskGroupRepository;
    }

    @Override
    final void beforeCreateInternal(T task, ModifyTaskDto<S> dto) {
        if (dto.taskGroupId() == null)
            throw new ValidationException("Task group id is required.");
        task.setTaskGroup(this.taskGroupRepository.getReferenceById(dto.taskGroupId()));
    }

    @Override
    final void updateInternal(T task, ModifyTaskDto<S> dto) {
        if (dto.taskGroupId() == null)
            throw new ValidationException("Task group id is required.");
        task.setTaskGroup(this.taskGroupRepository.getReferenceById(dto.taskGroupId()));
    }

}
