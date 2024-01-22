package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.auth.AuthConstants;
import at.jku.dke.etutor.task_app.data.entities.Submission;
import at.jku.dke.etutor.task_app.data.entities.Task;
import at.jku.dke.etutor.task_app.data.repositories.SubmissionRepository;
import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.etutor.task_app.dto.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This class provides methods for managing {@link Submission}s.
 *
 * @param <T> The task type.
 * @param <S> The submission type.
 * @param <U> The type of the submission input used in {@link SubmitSubmissionDto}.
 */
public abstract class BaseSubmissionService<T extends Task<?>, S extends Submission<T>, U> implements SubmissionService<U> {
    /**
     * The logger used in this class.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(BaseSubmissionService.class);

    /**
     * The submission repository.
     */
    protected final SubmissionRepository<S> submissionRepository;

    /**
     * The task repository.
     */
    protected final TaskRepository<T> taskRepository;

    /**
     * Creates a new instance of class {@link BaseSubmissionService}.
     *
     * @param submissionRepository The submission repository.
     * @param taskRepository       The task repository.
     */
    protected BaseSubmissionService(SubmissionRepository<S> submissionRepository, TaskRepository<T> taskRepository) {
        this.submissionRepository = submissionRepository;
        this.taskRepository = taskRepository;
    }

    //#region --- Submit ---

    /**
     * Enqueues the submission for evaluation.
     *
     * @param submission The submission.
     * @return The submission identifier.
     */
    @Override
    @Transactional
    @PreAuthorize(AuthConstants.SUBMIT_AUTHORITY)
    public UUID enqueue(@Valid SubmitSubmissionDto<U> submission) {
        LOG.info("Enqueueing submission of task {} for assignment {} for user {}", submission.taskId(), submission.assignmentId(), submission.userId());

        // create submission
        S entity = this.createSubmission(submission);

        // enqueue submission for evaluation
        CompletableFuture.runAsync(() -> this.execute(submission, entity.getId(), true));

        return entity.getId();
    }

    /**
     * Executes the submission and returns the evaluation results.
     *
     * @param submission The submission.
     * @param persist    Whether the submission should be stored permanently.
     * @return The evaluation results.
     */
    @Override
    @Transactional
    @PreAuthorize(AuthConstants.SUBMIT_AUTHORITY)
    public GradingResultDto execute(@Valid SubmitSubmissionDto<U> submission, boolean persist) {
        S entity = null;

        // Persist
        if (persist)
            entity = this.createSubmission(submission);

        // Execute
        return this.execute(submission, entity == null ? null : entity.getId(), persist);
    }

    private GradingResultDto execute(SubmitSubmissionDto<U> submission, UUID entityId, boolean persist) {
        assert !persist || entityId != null;
        LOG.info("Executing submission of task {} for assignment {} for user {}", submission.taskId(), submission.assignmentId(), submission.userId());

        // evaluate submission
        var result = this.evaluate(submission);

        // store result
        S entity = null;
        if (entityId != null) {
            entity = this.submissionRepository.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Submission " + entityId + " does not exist"));
            if (persist) {
                entity.setEvaluationResult(result);
                this.submissionRepository.save(entity);
            }

            // delete entity
            if (!persist && entity != null) {
                this.submissionRepository.delete(entity);
            }
        }

        return new GradingResultDto(!persist ? null : entity.getId(), result);
    }

    /**
     * Creates a new submission entity.
     *
     * @param dto The submission data.
     * @return The created submission.
     * @implSpec This method SHOULD NOT save the entity to the database as this is done by the caller. This method SHOULD ONLY set the task type specific submission attributes.
     * The other attributes are set by the caller.
     */
    protected abstract S createSubmissionEntity(SubmitSubmissionDto<U> dto);

    /**
     * Evaluates the submission.
     *
     * @param dto The submission data transfer object.
     * @return The evaluation results.
     */
    protected abstract GradingDto evaluate(SubmitSubmissionDto<U> dto);

    private S createSubmission(SubmitSubmissionDto<U> dto) {
        LOG.info("Persisting submission of task {} for assignment {} for user {}", dto.taskId(), dto.assignmentId(), dto.userId());

        S entity = this.createSubmissionEntity(dto);
        entity.setAssignmentId(dto.assignmentId());
        entity.setUserId(dto.userId());
        entity.setTask(this.taskRepository.getReferenceById(dto.taskId()));
        entity.setSubmissionTime(Instant.now());
        entity.setFeedbackLevel(dto.feedbackLevel());
        entity.setLanguage(dto.language());
        entity.setMode(dto.mode());
        return this.submissionRepository.save(entity);
    }

    //#endregion

    //#region --- View ---

    /**
     * Returns the evaluation results for the specified submission.
     *
     * @param id The submission identifier.
     * @return The evaluation results or {@code null} if the result is not available.
     * @throws EntityNotFoundException If the submission does not exist.
     */
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize(AuthConstants.SUBMIT_AUTHORITY)
    public GradingDto getEvaluationResult(UUID id) {
        var entity = this.submissionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Submission " + id + " does not exist"));
        if (entity.getEvaluationResult() == null)
            return null;

        return entity.getEvaluationResult();
    }

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
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize(AuthConstants.READ_SUBMISSION_AUTHORITY)
    public Page<SubmissionDto<U>> getSubmissions(Pageable page, String userFilter, Long taskFilter, String assignmentFilter, SubmissionMode modeFilter) {
        LOG.debug("Loading submissions for page {}", page);
        return this.submissionRepository.findAll(this.getFilterSpecification(userFilter, taskFilter, assignmentFilter, modeFilter), page).map(this::mapSubmissionToDto);
    }

    /**
     * Returns the filter specification for the given filter parameters.
     *
     * @param userFilter       The user filter.
     * @param taskFilter       The task filter.
     * @param assignmentFilter The assignment filter.
     * @param modeFilter       The mode filter.
     * @return The filter specification.
     */
    protected Specification<S> getFilterSpecification(String userFilter, Long taskFilter, String assignmentFilter, SubmissionMode modeFilter) {
        return new FilterSpecification<>(userFilter, taskFilter, assignmentFilter, modeFilter);
    }

    /**
     * Maps the submission to a DTO.
     *
     * @param submission The submission.
     * @return The DTO.
     */
    protected SubmissionDto<U> mapSubmissionToDto(S submission) {
        U data = this.mapSubmissionToSubmissionData(submission);
        return new SubmissionDto<>(
            submission.getId(),
            submission.getUserId(),
            submission.getAssignmentId(),
            submission.getTask().getId(),
            submission.getSubmissionTime(),
            submission.getLanguage(),
            submission.getFeedbackLevel(),
            submission.getMode(),
            data,
            submission.getEvaluationResult()
        );
    }

    /**
     * Maps the submission to a DTO.
     *
     * @param submission The submission.
     * @return The DTO.
     */
    protected abstract U mapSubmissionToSubmissionData(S submission);

    private record FilterSpecification<X>(String user, Long taskId, String assignmentId, SubmissionMode mode) implements Specification<X> {

        @Override
        public Predicate toPredicate(@NonNull Root<X> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
            var predicates = new ArrayList<Predicate>();

            if (this.user != null)
                predicates.add(criteriaBuilder.equal(root.get("userId"), this.user));
            if (this.assignmentId != null)
                predicates.add(criteriaBuilder.equal(root.get("assignmentId"), this.assignmentId));
            if (this.taskId != null)
                predicates.add(criteriaBuilder.equal(root.get("task").get("id"), this.taskId));
            if (this.mode != null)
                predicates.add(criteriaBuilder.equal(root.get("mode"), criteriaBuilder.literal(this.mode.name().toLowerCase())));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }

    }

    //#endregion

    /**
     * Deletes the submission with the specified identifier.
     *
     * @param id The submission identifier.
     */
    @Override
    @Transactional
    @PreAuthorize(AuthConstants.SUBMIT_AUTHORITY)
    public void delete(UUID id) {
        LOG.info("Deleting submission {}", id);
        this.submissionRepository.deleteById(id);
    }

}
