package at.jku.dke.etutor.task_app.controllers;

import at.jku.dke.etutor.task_app.data.entities.BaseTask;
import at.jku.dke.etutor.task_app.data.entities.BaseTaskGroup;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.dto.TaskModificationResponseDto;
import at.jku.dke.etutor.task_app.dto.TaskStatus;
import at.jku.dke.etutor.task_app.services.BaseTaskService;
import at.jku.dke.etutor.task_app.services.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseTaskControllerTest {

    @Test
    void get() {
        // Arrange
        var controller = new TaskController();
        final long id = 2L;
        var entity = new TestTask(9L);
        when(controller.getTaskService().get(id)).thenReturn(Optional.of(entity));

        // Act
        var result = controller.get(id);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(new TaskDto(entity.getData()), result.getBody());
    }

    @Test
    void getNotExisting() {
        // Arrange
        var controller = new TaskController();
        final long id = 2L;
        when(controller.getTaskService().get(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> controller.get(id));
    }

    @Test
    void create() {
        // Arrange
        var controller = new TaskController();
        final long id = 2L;
        var dto = new ModifyTaskDto<>(2L, BigDecimal.TEN, "test", TaskStatus.APPROVED, new AdditionalData("data"));
        when(controller.getTaskService().create(id, dto)).thenReturn(new TaskModificationResponseDto(null, null, null, null));

        // Act
        var result = controller.create(id, dto);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getHeaders().getLocation());
        assertEquals("/api/task/2", result.getHeaders().getLocation().toString());
    }

    @Test
    void update() {
        // Arrange
        var controller = new TaskController();
        final long id = 2L;
        var dto = new ModifyTaskDto<>(2L, BigDecimal.TEN, "test", TaskStatus.APPROVED, new AdditionalData("data"));

        // Act
        var result = controller.update(id, dto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void updateReturnsValue() {
        // Arrange
        var controller = new TaskController();
        final long id = 2L;
        var dto = new ModifyTaskDto<>(2L, BigDecimal.TEN, "test", TaskStatus.APPROVED, new AdditionalData("data"));
        when(controller.getTaskService().update(id, dto)).thenReturn(new TaskModificationResponseDto(null, null, null, null));

        // Act
        var result = controller.update(id, dto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void delete() {
        // Arrange
        var controller = new TaskController();
        final long id = 2L;

        // Act
        var result = controller.delete(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    private static class TaskController extends BaseTaskController<TestTask, TaskDto, AdditionalData> {
        protected TaskController() {
            //noinspection unchecked
            super(mock(BaseTaskService.class));
        }

        public TaskService<TestTask, ?, AdditionalData> getTaskService() {
            return taskService;
        }

        @Override
        protected TaskDto mapToDto(TestTask entity) {
            return new TaskDto(entity.getData());
        }
    }

    private record TaskDto(String data) {
    }

    private static class TestTask extends BaseTask<TestTaskGroup> implements Serializable {
        private String data;

        public TestTask(Long id) {
            super(id, BigDecimal.ZERO, TaskStatus.APPROVED, new TestTaskGroup(9L));
        }

        public TestTask(Long id, String data) {
            super(id, BigDecimal.ZERO, TaskStatus.APPROVED, new TestTaskGroup(9L));
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }

    private static class TestTaskGroup extends BaseTaskGroup implements Serializable {
        public TestTaskGroup(Long id) {
            super(id, TaskStatus.APPROVED);
        }
    }

    private record AdditionalData(String data) {
    }
}

