package at.jku.dke.etutor.task_app.data.entities;

import at.jku.dke.etutor.task_app.data.converters.TaskStatusConverter;
import at.jku.dke.etutor.task_app.dto.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a task.
 * <p>
 * Add following annotations to extending classes:
 * <pre>
 * &#64;Entity
 * &#64;Table(name = "task")
 * </pre>
 */
@MappedSuperclass
public abstract class BaseTask implements Task {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "max_points", nullable = false, precision = 7, scale = 2)
    private BigDecimal maxPoints;

    @Convert(converter = TaskStatusConverter.class)
    @Column(name = "status", columnDefinition = "task_status not null")
    private TaskStatus status;

    /**
     * Creates a new instance of class {@link BaseTask}.
     */
    protected BaseTask() {
    }

    /**
     * Creates a new instance of class {@link BaseTask}.
     *
     * @param maxPoints The maximum achievable points.
     * @param status    The status.
     */
    protected BaseTask(BigDecimal maxPoints, TaskStatus status) {
        this.maxPoints = maxPoints;
        this.status = status;
    }

    /**
     * Creates a new instance of class {@link BaseTask}.
     *
     * @param id        The id.
     * @param maxPoints The maximum achievable points.
     * @param status    The status.
     */
    protected BaseTask(Long id, BigDecimal maxPoints, TaskStatus status) {
        this.id = id;
        this.maxPoints = maxPoints;
        this.status = status;
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id The id.
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the max points.
     *
     * @return The max points.
     */
    @Override
    public BigDecimal getMaxPoints() {
        return maxPoints;
    }

    /**
     * Sets the max points.
     *
     * @param maxPoints The max points.
     */
    @Override
    public void setMaxPoints(BigDecimal maxPoints) {
        this.maxPoints = maxPoints;
    }

    /**
     * Gets the status.
     *
     * @return The status.
     */
    @Override
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status The status.
     */
    @Override
    public void setStatus(TaskStatus status) {
        this.status = status;
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

        Task task = (Task) o;
        return getId() != null && Objects.equals(getId(), task.getId());
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
            .add("id=" + id)
            .toString();
    }
}
