package at.jku.dke.etutor.task_app.controllers;

import at.jku.dke.etutor.task_app.data.entities.Task;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.services.TaskService;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

/**
 * Base implementation of {@link TaskController} with predefined request mapping base.
 * <p>
 * Add <code>@RestController</code> to the extending class.
 *
 * @param <E> The type of the task entity.
 * @param <D> The type of the task DTO.
 * @param <A> The type of the additional data in {@link ModifyTaskDto}.
 */
@RequestMapping("/api/task")
public abstract class BaseTaskController<E extends Task, D, A> extends BaseTaskControllerWithoutRequestMapping<E, D, A> {

    /**
     * Creates a new instance of class {@link BaseTaskController}.
     *
     * @param taskService The task group service.
     */
    protected BaseTaskController(TaskService<E, A> taskService) {
        super(taskService);
    }

    @Override
    protected URI createDetailsUri(long id) {
        return URI.create("/api/task/" + id);
    }
}
