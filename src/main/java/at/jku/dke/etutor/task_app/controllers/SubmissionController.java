package at.jku.dke.etutor.task_app.controllers;

import at.jku.dke.etutor.task_app.auth.AuthConstants;
import at.jku.dke.etutor.task_app.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * Controller for managing submissions.
 * <p>
 * Add <code>@RestController</code> and <code>@RequestMapping("/api/submission")</code> to the implementing class.
 *
 * @param <T> The type of the additional data in {@link SubmitSubmissionDto}.
 */
@Tag(name = "Submission", description = "Manage submissions")
public interface SubmissionController<T> {
    /**
     * Executes and grades a submission.
     *
     * @param submission      The submission.
     * @param runInBackground Whether to run the grading in background or wait for grading to finish (default: {@code false}).
     * @param persist         Whether to persist the submission (default: {@code true}). Only applies if {@code runInBackground} is {@code false}.
     * @return The submission identifier if {@code runInBackground} is {@code true}; the grading result if {@code runInBackground} is {@code false}; or an error response.
     * @implSpec Only clients with role {@link at.jku.dke.etutor.task_app.auth.AuthConstants#SUBMIT} should be allowed to access this endpoint.
     */
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submission graded", content = @Content(schema = @Schema(implementation = GradingResultDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
        @ApiResponse(responseCode = "202", description = "Submission enqueued for grading", content = @Content(schema = @Schema(implementation = UUID.class, description = "The submission identifier."),
            mediaType = MediaType.TEXT_PLAIN_VALUE), headers = @Header(name = "Location", description = "The location of the submission result.")),
        @ApiResponse(responseCode = "400", description = "Invalid submission data", content = @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)),
        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)),
        @ApiResponse(responseCode = "403", description = "Operation not allowed", content = @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE))
    })
    @Operation(
        summary = "Execute and grade submission",
        description = "Executes and grades a submission. If <code>runInBackground</code> is <code>true</code>, only the submission identifier will be returned; otherwise the evaluation result will be returned. Requires the SUBMIT role.",
        security = @SecurityRequirement(name = AuthConstants.API_KEY_REQUIREMENT))
    ResponseEntity<Serializable> submit(@RequestBody SubmitSubmissionDto<T> submission,
                                        @Parameter(description = "Whether to run the grading in background or wait for grading to finish.") @RequestParam(required = false, defaultValue = "false") boolean runInBackground,
                                        @Parameter(description = "Whether to persist the submission. Only applies if <code>runInBackground</code> is <code>false</code>.") @RequestParam(required = false, defaultValue = "true") boolean persist);

    /**
     * Returns the evaluation result for a submission.
     *
     * @param id      The submission identifier.
     * @param timeout The maximum time to wait for the result in seconds (default: {@code 10}, maximum: {@code 60}).
     * @param delete  Whether the submission should be deleted.
     * @return The result of the submission or an error response.
     * @implSpec Only clients with role {@link at.jku.dke.etutor.task_app.auth.AuthConstants#SUBMIT} should be allowed to access this endpoint.
     */
    @GetMapping(value = "/{id}/result", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grading result"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)),
        @ApiResponse(responseCode = "403", description = "Operation not allowed", content = @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)),
        @ApiResponse(responseCode = "404", description = "Submission does not exist", content = @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)),
        @ApiResponse(responseCode = "408", description = "Result is not yet available, try again later", content = @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE))
    })
    @Operation(
        summary = "Get evaluation result",
        description = "Returns the evaluation result for the requested submission. Waits for the specified timeout for the result to be available. Requires the SUBMIT role.",
        security = @SecurityRequirement(name = AuthConstants.API_KEY_REQUIREMENT))
    ResponseEntity<GradingDto> getResult(@Parameter(description = "The submission identifier.") @PathVariable UUID id,
                                         @Parameter(description = "The maximum amount of seconds to wait for the result.") @RequestHeader(value = "X-API-TIMEOUT", required = false, defaultValue = "10") int timeout,
                                         @Parameter(description = "Whether to delete the submission after retrieval.") @RequestParam(required = false, defaultValue = "false") boolean delete);

    /**
     * Returns a paged (filtered) list of submissions.
     *
     * @param page             The page of submissions to load.
     * @param userFilter       Optional user filter string (applies equals to {@link SubmissionDto#userId()}).
     * @param taskFilter       Optional task filter (applies equals to {@link SubmissionDto#taskId()}).
     * @param assignmentFilter Optional assignment filter string (applies equals to {@link SubmissionDto#assignmentId()}).
     * @param modeFilter       Optional mode filter (applies equals to {@link SubmissionDto#mode()}).
     * @return Page of submissions or an error response.
     * @implSpec Only clients with role {@link at.jku.dke.etutor.task_app.auth.AuthConstants#READ_SUBMISSION} should be allowed to access this endpoint.
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paged list of Submission"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)),
        @ApiResponse(responseCode = "403", description = "Operation not allowed", content = @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE))
    })
    @Operation(
        summary = "Get submissions",
        description = "Returns a paged and filtered list of submissions. Requires the READ_SUBMISSION role.",
        security = @SecurityRequirement(name = AuthConstants.API_KEY_REQUIREMENT))
    ResponseEntity<Page<SubmissionDto<T>>> getSubmissions(@ParameterObject Pageable page,
                                                          @Parameter(description = "User filter string (applies equals to userId.") @RequestParam(required = false) String userFilter,
                                                          @Parameter(description = "Task filter string (applies equals to taskId.") @RequestParam(required = false) Long taskFilter,
                                                          @Parameter(description = "Assignment filter string (applies equals to assignmentId.") @RequestParam(required = false) String assignmentFilter,
                                                          @Parameter(description = "Submission mode filter string (applies equals to mode.") @RequestParam(required = false) SubmissionMode modeFilter);
}
