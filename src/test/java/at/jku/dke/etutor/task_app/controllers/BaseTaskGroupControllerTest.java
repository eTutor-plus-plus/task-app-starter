package at.jku.dke.etutor.task_app.controllers;

import at.jku.dke.etutor.task_app.data.entities.BaseTaskGroup;
import at.jku.dke.etutor.task_app.dto.ModifyTaskGroupDto;
import at.jku.dke.etutor.task_app.dto.TaskGroupModificationResponseDto;
import at.jku.dke.etutor.task_app.dto.TaskStatus;
import at.jku.dke.etutor.task_app.services.BaseTaskGroupService;
import at.jku.dke.etutor.task_app.services.TaskGroupService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseTaskGroupControllerTest {

    @Test
    void get() {
        // Arrange
        var controller = new TaskGroupController();
        final long id = 2L;
        var entity = new TestTaskGroup(9L);
        when(controller.getTaskGroupService().get(id)).thenReturn(Optional.of(entity));

        // Act
        var result = controller.get(id);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(new TaskGroupDto(entity.getData()), result.getBody());
    }

    @Test
    void getNotExisting() {
        // Arrange
        var controller = new TaskGroupController();
        final long id = 2L;
        when(controller.getTaskGroupService().get(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> controller.get(id));
    }

    @Test
    void create() {
        // Arrange
        var controller = new TaskGroupController();
        final long id = 2L;
        var dto = new ModifyTaskGroupDto<>("test", TaskStatus.APPROVED, new AdditionalData("data"));
        when(controller.getTaskGroupService().create(id, dto)).thenReturn(new TaskGroupModificationResponseDto(null, null));

        // Act
        var result = controller.create(id, dto);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getHeaders().getLocation());
        assertEquals("/api/taskGroup/2", result.getHeaders().getLocation().toString());
    }

    @Test
    void update() {
        // Arrange
        var controller = new TaskGroupController();
        final long id = 2L;
        var dto = new ModifyTaskGroupDto<>("test", TaskStatus.APPROVED, new AdditionalData("data"));

        // Act
        var result = controller.update(id, dto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void updateReturnsValue() {
        // Arrange
        var controller = new TaskGroupController();
        final long id = 2L;
        var dto = new ModifyTaskGroupDto<>("test", TaskStatus.APPROVED, new AdditionalData("data"));
        when(controller.getTaskGroupService().update(id, dto)).thenReturn(new TaskGroupModificationResponseDto(null, null));

        // Act
        var result = controller.update(id, dto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void delete() {
        // Arrange
        var controller = new TaskGroupController();
        final long id = 2L;

        // Act
        var result = controller.delete(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    private static class TaskGroupController extends BaseTaskGroupController<TestTaskGroup, TaskGroupDto, AdditionalData> {
        protected TaskGroupController() {
            //noinspection unchecked
            super(mock(BaseTaskGroupService.class));
        }

        public TaskGroupService<TestTaskGroup, AdditionalData> getTaskGroupService() {
            return taskGroupService;
        }

        @Override
        protected TaskGroupDto mapToDto(TestTaskGroup entity) {
            return new TaskGroupDto(entity.getData());
        }
    }

    private record TaskGroupDto(String data) {
    }

    private static class TestTaskGroup extends BaseTaskGroup implements Serializable {
        private String data;

        public TestTaskGroup(Long id) {
            super(id, TaskStatus.APPROVED);
        }

        public TestTaskGroup(Long id, String data) {
            super(id, TaskStatus.APPROVED);
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }

    private record AdditionalData(String data) {
    }
}

