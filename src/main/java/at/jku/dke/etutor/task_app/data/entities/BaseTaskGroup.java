package at.jku.dke.etutor.task_app.data.entities;

import at.jku.dke.etutor.task_app.data.converters.TaskStatusConverter;
import at.jku.dke.etutor.task_app.dto.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a task group.
 * <p>
 * Add following annotations to extending classes:
 * <pre>
 * &#64;Entity
 * &#64;Table(name = "task_group")
 * </pre>
 */
@MappedSuperclass
public abstract class BaseTaskGroup implements TaskGroup {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Convert(converter = TaskStatusConverter.class)
    @Column(name = "status", columnDefinition = "task_status not null")
    private TaskStatus status;

    /**
     * Creates a new instance of class {@link BaseTaskGroup}.
     */
    protected BaseTaskGroup() {
    }

    /**
     * Creates a new instance of class {@link BaseTaskGroup}.
     *
     * @param status The status.
     */
    protected BaseTaskGroup(TaskStatus status) {
        this.status = status;
    }

    /**
     * Creates a new instance of class {@link BaseTaskGroup}.
     *
     * @param id     The id.
     * @param status The status.
     */
    protected BaseTaskGroup(Long id, TaskStatus status) {
        this.id = id;
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
    public final boolean equals(Object o) {
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

        TaskGroup taskGroup = (TaskGroup) o;
        return getId() != null && Objects.equals(getId(), taskGroup.getId());
    }

    @Override
    public final int hashCode() {
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
