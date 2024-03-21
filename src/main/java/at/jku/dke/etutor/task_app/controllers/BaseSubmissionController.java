package at.jku.dke.etutor.task_app.controllers;

import at.jku.dke.etutor.task_app.dto.SubmitSubmissionDto;
import at.jku.dke.etutor.task_app.services.SubmissionService;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.UUID;

/**
 * Base implementation of {@link SubmissionController} with predefined request mapping base.
 * <p>
 * Add <code>@RestController</code> to the extending class.
 *
 * @param <A> The type of the submission input used in {@link SubmitSubmissionDto}.
 */
@RequestMapping("/api/submission")
public abstract class BaseSubmissionController<A> extends BaseSubmissionControllerWithoutRequestMapping<A> {

    /**
     * Creates a new instance of class {@link BaseSubmissionControllerWithoutRequestMapping}.
     *
     * @param submissionService The submission service.
     */
    protected BaseSubmissionController(SubmissionService<A> submissionService) {
        super(submissionService);
    }

    @Override
    protected URI createDetailsUri(UUID id) {
        return URI.create("/api/submission/" + id + "/result");
    }

}
