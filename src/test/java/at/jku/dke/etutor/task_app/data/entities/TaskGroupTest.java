package at.jku.dke.etutor.task_app.data.entities;

import at.jku.dke.etutor.task_app.dto.TaskStatus;
import org.hibernate.proxy.AbstractLazyInitializer;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.junit.jupiter.api.Test;

import java.io.Serial;

import static org.junit.jupiter.api.Assertions.*;

class TaskGroupTest {

    @Test
    void testConstructorPartial() {
        // Arrange
        final TaskStatus status = TaskStatus.APPROVED;

        // Act
        var task = new TaskGroupTestEntity(status);

        // Assert
        assertEquals(status, task.getStatus());
    }

    @Test
    void testConstructorFull() {
        // Arrange
        final long id = 3;
        final TaskStatus status = TaskStatus.APPROVED;

        // Act
        var task = new TaskGroupTestEntity(id, status);

        // Assert
        assertEquals(id, task.getId());
        assertEquals(status, task.getStatus());
    }

    @Test
    void testGetSetId() {
        // Arrange
        var task = new TaskGroupTestEntity();
        final long value = 2;

        // Act
        task.setId(value);
        var result = task.getId();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testGetSetStatus() {
        // Arrange
        var task = new TaskGroupTestEntity();
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
        var taskGroup1 = new TaskGroupTestEntity();
        var taskGroup2 = new TaskGroupTestEntity();

        var taskGroup3 = new TaskGroupTestEntity();
        taskGroup3.setId(1L);

        var taskGroup4 = new TaskGroupTestEntity();
        taskGroup4.setId(1L);
        taskGroup4.setStatus(TaskStatus.DRAFT);

        // Act
        var result1 = taskGroup1.equals(taskGroup2);
        var result2 = taskGroup3.equals(taskGroup4);

        // Assert
        assertFalse(result1);
        assertTrue(result2);
    }

    @Test
    void testEqualsIdNull() {
        // Arrange
        var taskGroup1 = new TaskGroupTestEntity();
        var taskGroup2 = new TaskGroupTestEntity();
        taskGroup2.setId(1L);

        // Act
        var result = taskGroup1.equals(taskGroup2);

        // Assert
        assertFalse(result);
    }

    @Test
    void testEqualsIdNotEqual() {
        // Arrange
        var taskGroup1 = new TaskGroupTestEntity();
        taskGroup1.setId(2L);
        var taskGroup2 = new TaskGroupTestEntity();
        taskGroup2.setId(1L);

        // Act
        var result = taskGroup1.equals(taskGroup2);

        // Assert
        assertFalse(result);
    }

    @Test
    void testEqualsSame() {
        // Arrange
        var taskGroup = new TaskGroupTestEntity();

        // Act
        @SuppressWarnings("EqualsWithItself") var result = taskGroup.equals(taskGroup);

        // Assert
        //noinspection ConstantValue
        assertTrue(result);
    }

    @Test
    void testEqualsNull() {
        // Arrange
        var taskGroup = new TaskGroupTestEntity();

        // Act
        @SuppressWarnings("ConstantValue") var result = taskGroup.equals(null);

        // Assert
        //noinspection ConstantValue
        assertFalse(result);
    }

    @Test
    void testEqualsOtherClass() {
        // Arrange
        var taskGroup = new TaskGroupTestEntity();

        // Act
        var result = taskGroup.equals(new Object());

        // Assert
        assertFalse(result);
    }

    @Test
    void testEqualsHibernateProxy() {
        // Arrange
        var task1 = new HibernateProxyTaskGroupTestEntity();
        task1.setId(2L);
        var task2 = new TaskGroupTestEntity();
        task2.setId(2L);

        // Act
        var result = task1.equals(task2);

        // Assert
        assertTrue(result);
    }

    @Test
    void testEqualsOtherHibernateProxy() {
        // Arrange
        var task1 = new TaskGroupTestEntity();
        task1.setId(2L);
        var task2 = new HibernateProxyTaskGroupTestEntity();
        task2.setId(2L);

        // Act
        var result = task1.equals(task2);

        // Assert
        assertTrue(result);
    }

    @Test
    void testHashCode() {
        // Arrange
        var taskGroup1 = new TaskGroupTestEntity();
        var taskGroup2 = new TaskGroupTestEntity();

        var taskGroup3 = new TaskGroupTestEntity();
        taskGroup3.setId(1L);

        var taskGroup4 = new TaskGroupTestEntity();
        taskGroup4.setId(1L);
        taskGroup4.setStatus(TaskStatus.DRAFT);

        // Act
        var result1 = taskGroup1.hashCode() == taskGroup2.hashCode();
        var result2 = taskGroup3.hashCode() == taskGroup4.hashCode();

        // Assert
        assertTrue(result1);
        assertTrue(result2);
    }

    @Test
    void testHashCodeHibernateProxy() {
        // Arrange
        var task1 = new HibernateProxyTaskGroupTestEntity();
        var task2 = new TaskGroupTestEntity();

        // Act
        var result = task1.hashCode() == task2.hashCode();

        // Assert
        assertTrue(result);
    }

    @Test
    void testToString() {
        // Arrange
        var taskGroup = new TaskGroupTestEntity();
        taskGroup.setId(4L);
        taskGroup.setStatus(TaskStatus.DRAFT);

        // Act
        var result = taskGroup.toString();

        // Assert
        assertEquals("TaskGroupTestEntity[id=4]", result);
    }

    private static class TaskGroupTestEntity extends BaseTaskGroup {
        public TaskGroupTestEntity() {
        }

        public TaskGroupTestEntity(TaskStatus status) {
            super(status);
        }

        public TaskGroupTestEntity(Long id, TaskStatus status) {
            super(id, status);
        }
    }

    private static class HibernateProxyTaskGroupTestEntity extends TaskGroupTestEntity implements HibernateProxy {
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
                    return TaskGroupTestEntity.class;
                }

                @Override
                public Class<?> getImplementationClass() {
                    return null;
                }
            };
        }
    }
}
