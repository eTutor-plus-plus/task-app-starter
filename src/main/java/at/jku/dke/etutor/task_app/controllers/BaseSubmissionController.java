package at.jku.dke.etutor.task_app.controllers;

import at.jku.dke.etutor.task_app.data.entities.Task;
import at.jku.dke.etutor.task_app.dto.*;
import at.jku.dke.etutor.task_app.services.SubmissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.net.URI;
import java.util.UUID;

/**
 * Base implementation of {@link SubmissionController}.
 * <p>
 * Add <code>@RestController</code> to the extending class.
 *
 * @param <T> The task type.
 * @param <A> The type of the submission input used in {@link SubmitSubmissionDto}.
 */
public abstract class BaseSubmissionController<T extends Task, A> implements SubmissionController<A> {

    /**
     * The submission service.
     */
    protected final SubmissionService<A> submissionService;

    /**
     * Creates a new instance of class {@link BaseSubmissionController}.
     *
     * @param submissionService The submission service.
     */
    protected BaseSubmissionController(SubmissionService<A> submissionService) {
        this.submissionService = submissionService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Serializable> submit(SubmitSubmissionDto<A> submission, boolean runInBackground, boolean persist) {
        if (runInBackground) {
            UUID id = this.submissionService.enqueue(submission);
            return ResponseEntity
                .accepted()
                .location(URI.create("/api/submission/" + id + "/result"))
                .contentType(MediaType.TEXT_PLAIN)
                .body(id.toString());
        } else {
            GradingResultDto result = this.submissionService.execute(submission, persist);
            var response = ResponseEntity.status(HttpStatus.OK);
            if (result.submissionId() != null)
                response = response.location(URI.create("/api/submission/" + result.submissionId() + "/result"));
            return response.body(result);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GradingDto> getResult(UUID id, int timeout, boolean delete) {
        if (timeout < 0)
            timeout = 0;

        GradingDto result;
        int remainingTimeout = timeout;
        do {
            result = this.submissionService.getEvaluationResult(id);
            remainingTimeout--;

            if (result == null && remainingTimeout > 0) {
                try {
                    Thread.sleep(990);
                } catch (InterruptedException ignore) {
                }
            }
        } while (result == null && remainingTimeout > 0);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        } else {
            if (delete)
                this.submissionService.delete(id);
            return ResponseEntity.ok(result);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Page<SubmissionDto<A>>> getSubmissions(Pageable page, String userFilter, Long taskFilter, String assignmentFilter, SubmissionMode modeFilter) {
        var result = this.submissionService.getSubmissions(page, userFilter, taskFilter, assignmentFilter, modeFilter);
        return ResponseEntity.ok(result);
    }
}
