package at.jku.dke.etutor.task_app.controllers;

import at.jku.dke.etutor.task_app.data.entities.TaskGroup;
import at.jku.dke.etutor.task_app.services.TaskGroupService;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

/**
 * Base implementation of {@link TaskGroupController} with predefined request mapping base.
 * <p>
 * Add <code>@RestController</code> to the extending class.
 *
 * @param <E> The type of the task group entity.
 * @param <D> The type of the task group DTO.
 * @param <A> The type of the additional data in {@link at.jku.dke.etutor.task_app.dto.ModifyTaskGroupDto}.
 */
@RequestMapping("/api/taskGroup")
public abstract class BaseTaskGroupController<E extends TaskGroup, D, A> extends BaseTaskGroupControllerWithoutRequestMapping<E, D, A> {

    /**
     * Creates a new instance of class {@link BaseTaskGroupController}.
     *
     * @param taskGroupService The task group service.
     */
    protected BaseTaskGroupController(TaskGroupService<E, A> taskGroupService) {
        super(taskGroupService);
    }

    @Override
    protected URI createDetailsUri(long id) {
        return URI.create("/api/taskGroup/" + id);
    }

}
