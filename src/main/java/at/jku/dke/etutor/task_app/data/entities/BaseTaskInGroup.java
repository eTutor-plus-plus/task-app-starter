package at.jku.dke.etutor.task_app.data.entities;

import at.jku.dke.etutor.task_app.dto.TaskStatus;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a task belonging to a task group.
 * <p>
 * Add following annotations to extending classes:
 * <pre>
 * &#64;Entity
 * &#64;Table(name = "task")
 * </pre>
 *
 * @param <T> The type of the task group.
 */
@MappedSuperclass
public abstract class BaseTaskInGroup<T extends TaskGroup> extends BaseTask implements TaskInGroup<T> {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "task_group_id")
    private T taskGroup;

    /**
     * Creates a new instance of class {@link BaseTaskInGroup}.
     */
    protected BaseTaskInGroup() {
    }

    /**
     * Creates a new instance of class {@link BaseTaskInGroup}.
     *
     * @param maxPoints The maximum achievable points.
     * @param status    The status.
     * @param taskGroup The task group.
     */
    protected BaseTaskInGroup(BigDecimal maxPoints, TaskStatus status, T taskGroup) {
        super(maxPoints, status);
        this.taskGroup = taskGroup;
    }

    /**
     * Creates a new instance of class {@link BaseTaskInGroup}.
     *
     * @param id        The id.
     * @param maxPoints The maximum achievable points.
     * @param status    The status.
     * @param taskGroup The task group.
     */
    protected BaseTaskInGroup(Long id, BigDecimal maxPoints, TaskStatus status, T taskGroup) {
        super(id, maxPoints, status);
        this.taskGroup = taskGroup;
    }

    /**
     * Gets the task group.
     *
     * @return The task group.
     */
    @Override
    public T getTaskGroup() {
        return taskGroup;
    }

    /**
     * Sets the task group.
     *
     * @param taskGroup The task group.
     */
    @Override
    public void setTaskGroup(T taskGroup) {
        this.taskGroup = taskGroup;
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

        BaseTaskInGroup<?> task = (BaseTaskInGroup<?>) o;
        return getId() != null && Objects.equals(getId(), task.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hp ?
            hp.getHibernateLazyInitializer().getPersistentClass().hashCode() :
            getClass().hashCode();
    }
}
