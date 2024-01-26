package at.jku.dke.etutor.task_app.data.repositories;

import at.jku.dke.etutor.task_app.data.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for entity {@link Task}.
 *
 * @param <T> The task type.
 */
public interface TaskRepository<T extends Task> extends JpaRepository<T, Long>  {
}
