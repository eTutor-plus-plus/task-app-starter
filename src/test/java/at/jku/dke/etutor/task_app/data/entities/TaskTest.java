package at.jku.dke.etutor.task_app.data.entities;

import at.jku.dke.etutor.task_app.dto.TaskStatus;
import org.hibernate.proxy.AbstractLazyInitializer;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.junit.jupiter.api.Test;

import java.io.Serial;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testConstructorPartial() {
        // Arrange
        final BigDecimal points = BigDecimal.TWO;
        final TaskStatus status = TaskStatus.READY_FOR_APPROVAL;

        // Act
        var task = new TaskTestEntity(points, status);

        // Assert
        assertEquals(status, task.getStatus());
        assertEquals(points, task.getMaxPoints());
    }

    @Test
    void testConstructorFull() {
        // Arrange
        final long id = 11;
        final BigDecimal points = BigDecimal.TWO;
        final TaskStatus status = TaskStatus.READY_FOR_APPROVAL;

        // Act
        var task = new TaskTestEntity(id, points, status);

        // Assert
        assertEquals(id, task.getId());
        assertEquals(status, task.getStatus());
        assertEquals(points, task.getMaxPoints());
    }

    @Test
    void testGetSetId() {
        // Arrange
        var task = new TaskTestEntity();
        final long value = 2;

        // Act
        task.setId(value);
        var result = task.getId();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testGetSetMaxPoints() {
        // Arrange
        var task = new TaskTestEntity();
        final BigDecimal value = BigDecimal.TEN;

        // Act
        task.setMaxPoints(value);
        var result = task.getMaxPoints();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testGetSetStatus() {
        // Arrange
        var task = new TaskTestEntity();
        final TaskStatus value = TaskStatus.DRAFT;

        // Act
        task.setStatus(value);
        var result = task.getStatus();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testEquals() {
        // Arrange
        var task1 = new TaskTestEntity();
        var task2 = new TaskTestEntity();

        var task3 = new TaskTestEntity();
        task3.setId(1L);

        var task4 = new TaskTestEntity();
        task4.setId(1L);
        task4.setMaxPoints(BigDecimal.TWO);

        var task5 = new TaskTestEntity();
        task5.setId(1L);
        task4.setMaxPoints(BigDecimal.TWO);
        task5.setStatus(TaskStatus.DRAFT);

        // Act
        var result1 = task1.equals(task2);
        var result2 = task3.equals(task4);
        var result3 = task4.equals(task5);

        // Assert
        assertFalse(result1);
        assertTrue(result2);
        assertTrue(result3);
    }

    @Test
    void testEqualsIdNull() {
        // Arrange
        var task1 = new TaskTestEntity();
        var task2 = new TaskTestEntity();
        task2.setId(1L);

        // Act
        var result = task1.equals(task2);

        // Assert
        assertFalse(result);
    }

    @Test
    void testEqualsIdNotEqual() {
        // Arrange
        var task1 = new TaskTestEntity();
        task1.setId(2L);
        var task2 = new TaskTestEntity();
        task2.setId(1L);

        // Act
        var result = task1.equals(task2);

        // Assert
        assertFalse(result);
    }

    @Test
    void testEqualsSame() {
        // Arrange
        var task = new TaskTestEntity();

        // Act
        @SuppressWarnings("EqualsWithItself") var result = task.equals(task);

        // Assert
        //noinspection ConstantValue
        assertTrue(result);
    }

    @Test
    void testEqualsNull() {
        // Arrange
        var task = new TaskTestEntity();

        // Act
        @SuppressWarnings("ConstantValue") var result = task.equals(null);

        // Assert
        //noinspection ConstantValue
        assertFalse(result);
    }

    @Test
    void testEqualsOtherClass() {
        // Arrange
        var task = new TaskTestEntity();

        // Act
        var result = task.equals(new Object());

        // Assert
        assertFalse(result);
    }

    @Test
    void testEqualsHibernateProxy() {
        // Arrange
        var task1 = new HibernateProxyTaskTestEntity();
        task1.setId(2L);
        var task2 = new TaskTestEntity();
        task2.setId(2L);

        // Act
        var result = task1.equals(task2);

        // Assert
        assertTrue(result);
    }

    @Test
    void testEqualsOtherHibernateProxy() {
        // Arrange
        var task1 = new TaskTestEntity();
        task1.setId(2L);
        var task2 = new HibernateProxyTaskTestEntity();
        task2.setId(2L);

        // Act
        var result = task1.equals(task2);

        // Assert
        assertTrue(result);
    }

    @Test
    void testHashCode() {
        // Arrange
        var task1 = new TaskTestEntity();
        var task2 = new TaskTestEntity();

        var task3 = new TaskTestEntity();
        task3.setId(1L);

        var task4 = new TaskTestEntity();
        task4.setId(1L);
        task4.setMaxPoints(BigDecimal.TWO);

        var task5 = new TaskTestEntity();
        task5.setId(1L);
        task4.setMaxPoints(BigDecimal.TWO);
        task5.setStatus(TaskStatus.DRAFT);

        // Act
        var result1 = task1.hashCode() == task2.hashCode();
        var result2 = task3.hashCode() == task4.hashCode();
        var result3 = task4.hashCode() == task5.hashCode();

        // Assert
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
    }

    @Test
    void testHashCodeHibernateProxy() {
        // Arrange
        var task1 = new HibernateProxyTaskTestEntity();
        var task2 = new TaskTestEntity();

        // Act
        var result = task1.hashCode() == task2.hashCode();

        // Assert
        assertTrue(result);
    }

    @Test
    void testToString() {
        // Arrange
        var task = new TaskTestEntity();
        task.setId(1L);
        task.setMaxPoints(BigDecimal.TWO);
        task.setStatus(TaskStatus.DRAFT);

        // Act
        var result = task.toString();

        // Assert
        assertEquals("TaskTestEntity[id=1]", result);
    }

    private static class TaskTestEntity extends BaseTask {
        public TaskTestEntity() {
        }

        public TaskTestEntity(BigDecimal maxPoints, TaskStatus status) {
            super(maxPoints, status);
        }

        public TaskTestEntity(Long id, BigDecimal maxPoints, TaskStatus status) {
            super(id, maxPoints, status);
        }
    }

    private static class HibernateProxyTaskTestEntity extends TaskTestEntity implements HibernateProxy {
        @Serial
        @Override
        public Object writeReplace() {
            return null;
        }

        @Override
        public LazyInitializer getHibernateLazyInitializer() {
            return new AbstractLazyInitializer("test", this.getId(), null) {
                @Override
                public Class<?> getPersistentClass() {
                    return TaskTestEntity.class;
                }

                @Override
                public Class<?> getImplementationClass() {
                    return null;
                }
            };
        }
    }
}
