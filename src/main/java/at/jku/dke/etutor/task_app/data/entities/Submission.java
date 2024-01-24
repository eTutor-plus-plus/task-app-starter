package at.jku.dke.etutor.task_app.data.entities;

import at.jku.dke.etutor.task_app.dto.GradingDto;
import at.jku.dke.etutor.task_app.dto.SubmissionMode;

import java.time.Instant;
import java.util.UUID;

/**
 * Interface for submission entity.
 *
 * @param <T> The type of the task.
 */
public interface Submission<T extends Task> {
    /**
     * Gets the id.
     *
     * @return The id.
     */
    UUID getId();

    /**
     * Sets the id.
     *
     * @param id The id.
     */
    void setId(UUID id);

    /**
     * Gets the assignment id.
     *
     * @return The assignment id.
     */
    String getAssignmentId();

    /**
     * Sets the assignment id.
     *
     * @param assignmentId The assignment id.
     */
    void setAssignmentId(String assignmentId);

    /**
     * Gets the user id.
     *
     * @return The user id.
     */
    String getUserId();

    /**
     * Sets the user id.
     *
     * @param userId The user id.
     */
    void setUserId(String userId);

    /**
     * Gets the task.
     *
     * @return The task.
     */
    T getTask();

    /**
     * Sets the task.
     *
     * @param task The task.
     */
    void setTask(T task);

    /**
     * Gets the submission time.
     *
     * @return The submission time.
     */
    Instant getSubmissionTime();

    /**
     * Sets the submission time.
     *
     * @param submissionTime The submission time.
     */
    void setSubmissionTime(Instant submissionTime);

    /**
     * Gets the feedback level.
     *
     * @return The feedback level.
     */
    int getFeedbackLevel();

    /**
     * Sets the feedback level.
     *
     * @param feedbackLevel The feedback level.
     */
    void setFeedbackLevel(int feedbackLevel);

    /**
     * Gets the language.
     *
     * @return The language.
     */
    String getLanguage();

    /**
     * Sets the language.
     *
     * @param language The language.
     */
    void setLanguage(String language);

    /**
     * Gets the mode.
     *
     * @return The mode.
     */
    SubmissionMode getMode();

    /**
     * Sets the mode.
     *
     * @param mode The mode.
     */
    void setMode(SubmissionMode mode);

    /**
     * Gets the evaluation results.
     *
     * @return The evaluation results.
     */
    GradingDto getEvaluationResult();

    /**
     * Sets the evaluation results.
     *
     * @param evaluationResult The evaluation results.
     */
    void setEvaluationResult(GradingDto evaluationResult);
}
