package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.data.entities.Submission;
import at.jku.dke.etutor.task_app.dto.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

/**
 * Interface for classes that manage {@link Submission}s.
 *
 * @param <U> The type of the submission input used in {@link SubmitSubmissionDto}.
 */
@Validated
public interface SubmissionService<U> {
    /**
     * Enqueues the submission for evaluation.
     *
     * @param submission The submission.
     * @return The submission identifier.
     */
    UUID enqueue(@Valid SubmitSubmissionDto<U> submission);

    /**
     * Executes the submission and returns the evaluation results.
     *
     * @param submission The submission.
     * @param persist    Whether the submission should be stored permanently.
     * @return The evaluation results.
     */
    GradingResultDto execute(@Valid SubmitSubmissionDto<U> submission, boolean persist);

    /**
     * Returns the evaluation results for the specified submission.
     *
     * @param id The submission identifier.
     * @return The evaluation results or {@code null} if the result is not available.
     * @throws EntityNotFoundException If the submission does not exist.
     */
    GradingDto getEvaluationResult(UUID id);

    /**
     * Returns all submissions for the requested page.
     *
     * @param page             The page and sorting information.
     * @param userFilter       Optional user filter string (applies equals to userId attribute).
     * @param taskFilter       Optional task filter string (applies equals to taskId attribute).
     * @param assignmentFilter Optional assignment filter string (applies equals to assignmentId attribute).
     * @param modeFilter       Optional mode filter (applies equals to mode attribute).
     * @return List of submissions
     */
    Page<SubmissionDto<U>> getSubmissions(Pageable page, String userFilter, Long taskFilter, String assignmentFilter, SubmissionMode modeFilter);

    /**
     * Deletes the submission with the specified identifier.
     *
     * @param id The submission identifier.
     */
    void delete(UUID id);
}
