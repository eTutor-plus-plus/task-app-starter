package at.jku.dke.etutor.task_app.data.repositories;

import at.jku.dke.etutor.task_app.data.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Repository for entity {@link Submission}.
 */
public interface SubmissionRepository<S extends Submission<?>> extends JpaRepository<S, UUID>, JpaSpecificationExecutor<S> {
}
