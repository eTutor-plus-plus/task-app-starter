package at.jku.dke.etutor.task_app.data.entities;

import at.jku.dke.etutor.task_app.data.converters.SubmissionModeConverter;
import at.jku.dke.etutor.task_app.dto.GradingDto;
import at.jku.dke.etutor.task_app.dto.SubmissionMode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * Represents a submission.
 * <p>
 * Add following annotations to extending classes:
 * <pre>
 * &#64;Entity
 * &#64;Table(name = "submission")
 * </pre>
 *
 * @param <T> The type of the task.
 */
@MappedSuperclass
public abstract class BaseSubmission<T extends Task> implements Submission<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Size(max = 255)
    @Column(name = "user_id")
    private String userId;

    @Size(max = 255)
    @Column(name = "assignment_id")
    private String assignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "task_id")
    private T task;

    @NotNull
    @Column(name = "submission_time", nullable = false)
    private Instant submissionTime;

    @Size(min = 2, max = 2)
    @NotNull
    @Column(name = "language", nullable = false, length = 2)
    private String language;

    @NotNull
    @Min(0)
    @Max(4)
    @Column(name = "feedback_level", nullable = false)
    private int feedbackLevel;

    @Convert(converter = SubmissionModeConverter.class)
    @Column(name = "mode", columnDefinition = "submission_mode not null")
    private SubmissionMode mode;

    @Column(name = "evaluation_result", length = Integer.MAX_VALUE)
    @JdbcTypeCode(SqlTypes.JSON)
    private GradingDto evaluationResult;

    /**
     * Creates a new instance of class {@link BaseSubmission}.
     */
    protected BaseSubmission() {
    }

    /**
     * Creates a new instance of class {@link BaseSubmission}.
     *
     * @param userId        The user id.
     * @param assignmentId  The assignment id.
     * @param task          The task.
     * @param language      The language.
     * @param feedbackLevel The feedback level.
     * @param mode          The mode.
     */
    protected BaseSubmission(String userId, String assignmentId, T task, String language, int feedbackLevel, SubmissionMode mode) {
        this.userId = userId;
        this.assignmentId = assignmentId;
        this.task = task;
        this.language = language;
        this.feedbackLevel = feedbackLevel;
        this.mode = mode;
        this.submissionTime = Instant.now();
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id The id.
     */
    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the assignment id.
     *
     * @return The assignment id.
     */
    @Override
    public String getAssignmentId() {
        return assignmentId;
    }

    /**
     * Sets the assignment id.
     *
     * @param assignmentId The assignment id.
     */
    @Override
    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    /**
     * Gets the user id.
     *
     * @return The user id.
     */
    @Override
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId The user id.
     */
    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the task.
     *
     * @return The task.
     */
    @Override
    public T getTask() {
        return task;
    }

    /**
     * Sets the task.
     *
     * @param task The task.
     */
    @Override
    public void setTask(T task) {
        this.task = task;
    }

    /**
     * Gets the submission time.
     *
     * @return The submission time.
     */
    @Override
    public Instant getSubmissionTime() {
        return submissionTime;
    }

    /**
     * Sets the submission time.
     *
     * @param submissionTime The submission time.
     */
    @Override
    public void setSubmissionTime(Instant submissionTime) {
        this.submissionTime = submissionTime;
    }

    /**
     * Gets the feedback level.
     *
     * @return The feedback level.
     */
    @Override
    public int getFeedbackLevel() {
        return feedbackLevel;
    }

    /**
     * Sets the feedback level.
     *
     * @param feedbackLevel The feedback level.
     */
    @Override
    public void setFeedbackLevel(int feedbackLevel) {
        this.feedbackLevel = feedbackLevel;
    }

    /**
     * Gets the language.
     *
     * @return The language.
     */
    @Override
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language.
     *
     * @param language The language.
     */
    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Gets the mode.
     *
     * @return The mode.
     */
    @Override
    public SubmissionMode getMode() {
        return mode;
    }

    /**
     * Sets the mode.
     *
     * @param mode The mode.
     */
    @Override
    public void setMode(SubmissionMode mode) {
        this.mode = mode;
    }

    /**
     * Gets the evaluation results.
     *
     * @return The evaluation results.
     */
    @Override
    public GradingDto getEvaluationResult() {
        return evaluationResult;
    }

    /**
     * Sets the evaluation results.
     *
     * @param evaluationResult The evaluation results.
     */
    @Override
    public void setEvaluationResult(GradingDto evaluationResult) {
        this.evaluationResult = evaluationResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;

        Class<?> oEffectiveClass = o instanceof HibernateProxy oHP ?
            oHP.getHibernateLazyInitializer().getPersistentClass() :
            o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hp ?
            hp.getHibernateLazyInitializer().getPersistentClass() :
            this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
            return false;

        Submission<?> that = (Submission<?>) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy hp ?
            hp.getHibernateLazyInitializer().getPersistentClass().hashCode() :
            getClass().hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
            .add("id='" + id + "'")
            .add("userId='" + userId + "'")
            .add("assignmentId='" + assignmentId + "'")
            .toString();
    }
}
