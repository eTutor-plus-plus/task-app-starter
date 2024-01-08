package at.jku.dke.etutor.task_app.data.repositories;

import at.jku.dke.etutor.task_app.data.entities.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for entity {@link TaskGroup}.
 */
public interface TaskGroupRepository<T extends TaskGroup> extends JpaRepository<T, Long>  {
}
