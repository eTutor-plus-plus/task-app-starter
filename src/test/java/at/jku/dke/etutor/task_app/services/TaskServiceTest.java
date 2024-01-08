package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.data.entities.BaseTask;
import at.jku.dke.etutor.task_app.data.entities.BaseTaskGroup;
import at.jku.dke.etutor.task_app.data.repositories.TaskGroupRepository;
import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.dto.TaskStatus;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    //#region --- Get ---
    @Test
    void testGetExists() {
        // Arrange
        final long id = 9L;
        var service = new TaskServiceImpl();
        when(service.getRepository().findById(id)).thenReturn(Optional.of(new TaskEntity(id)));

        // Act
        var result = service.get(id);

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    void testGetNotExists() {
        // Arrange
        var service = new TaskServiceImpl();
        when(service.getRepository().findById(anyLong())).thenReturn(Optional.empty());

        // Act
        var result = service.get(99L);

        // Assert
        assertTrue(result.isEmpty());
    }
    //#endregion

    //#region --- Create ---
    @Test
    void testCreate() {
        // Act
        final long id = 4L;
        final long groupId = 1L;
        final TaskStatus taskStatus = TaskStatus.APPROVED;
        final String someData = "some data";
        final BigDecimal maxPoints = BigDecimal.TEN;

        var service = new TaskServiceImpl();
        var dto = new ModifyTaskDto<>(groupId, maxPoints, "test", taskStatus, new AdditionalData(someData));
        when(service.getRepository().existsById(id)).thenReturn(false);
        when(service.getTaskGroupRepository().getReferenceById(groupId)).thenReturn(new TaskGroupEntity(groupId));
        when(service.getRepository().save(new TaskEntity(id))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = service.create(id, dto);

        // Assert
        assertEquals(id, result.getId());
        assertEquals(taskStatus, result.getStatus());
        assertEquals(someData, result.getSomeData());
        assertEquals(maxPoints, result.getMaxPoints());
        assertEquals(groupId, result.getTaskGroup().getId());
    }

    @Test
    void testCreateAlreadyExists() {
        // Act
        final long id = 1L;

        var service = new TaskServiceImpl();
        var dto = new ModifyTaskDto<>(1L, BigDecimal.TEN, "test", TaskStatus.APPROVED, new AdditionalData("some data"));
        when(service.getRepository().existsById(id)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateKeyException.class, () -> service.create(id, dto));
    }
    //#endregion

    //#region --- Update ---
    @Test
    void testUpdate() {
        // Act
        final long id = 3L;

        var service = new TaskServiceImpl();
        var dto = new ModifyTaskDto<>(2L, BigDecimal.TEN, "test", TaskStatus.APPROVED, new AdditionalData("some data"));
        var entity = new TaskEntity(id, TaskStatus.DRAFT, new TaskGroupEntity(1L), "old data", BigDecimal.TWO);
        when(service.getRepository().findById(id)).thenReturn(Optional.of(entity));
        when(service.getTaskGroupRepository().getReferenceById(2L)).thenReturn(new TaskGroupEntity(2L));
        when(service.getRepository().save(new TaskEntity(id))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        service.update(id, dto);

        // Assert
        assertEquals(dto.status(), entity.getStatus());
        assertEquals(dto.maxPoints(), entity.getMaxPoints());
        assertEquals(dto.taskGroupId(), entity.getTaskGroup().getId());
        assertEquals(dto.additionalData().someData(), entity.getSomeData());
    }

    @Test
    void testUpdateNotExists() {
        // Act
        final long id = 1L;

        var service = new TaskServiceImpl();
        var dto = new ModifyTaskDto<>(2L, BigDecimal.TEN, "test", TaskStatus.APPROVED, new AdditionalData("some data"));
        when(service.getRepository().findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.update(id, dto));
    }
    //#endregion

    //#region --- Delete ---
    @Test
    void testDelete() {
        // Act
        final long id = 3L;
        var service = new TaskServiceImpl();

        // Act
        service.delete(id);

        // Assert
        verify(service.getRepository(), times(1)).deleteById(id);
    }

    @Test
    void testDeleteNotExists() {
        // Act
        final long id = 1L;
        var service = new TaskServiceImpl();

        // Act
        service.delete(id);

        // Assert
        verify(service.getRepository(), times(1)).deleteById(id);
    }
    //#endregion

    // TODO: test authorities

    private static class TaskServiceImpl extends BaseTaskService<TaskEntity, TaskGroupEntity, AdditionalData> {

        public TaskServiceImpl() {
            //noinspection unchecked
            super(mock(TaskRepository.class), mock(TaskGroupRepository.class));
        }

        public TaskGroupRepository<TaskGroupEntity> getTaskGroupRepository() {
            return super.taskGroupRepository;
        }

        public TaskRepository<TaskEntity> getRepository() {
            return super.repository;
        }

        @Override
        protected TaskEntity createTask(long id, ModifyTaskDto<AdditionalData> dto) {
            var entity = new TaskEntity();
            entity.setSomeData(dto.additionalData().someData());
            return entity;
        }

        @Override
        protected void updateTask(TaskEntity Task, ModifyTaskDto<AdditionalData> dto) {
            Task.setSomeData(dto.additionalData().someData());
        }
    }

    private static class TaskEntity extends BaseTask<TaskGroupEntity> {
        private String someData;

        public TaskEntity() {
        }

        public TaskEntity(Long id) {
            super(id, BigDecimal.ZERO, TaskStatus.APPROVED, new TaskGroupEntity());
        }

        public TaskEntity(Long id, TaskStatus status, TaskGroupEntity taskGroup, String someData, BigDecimal maxPoints) {
            super(id, maxPoints, status, taskGroup);
            this.someData = someData;
        }

        public String getSomeData() {
            return someData;
        }

        public void setSomeData(String someData) {
            this.someData = someData;
        }
    }

    private static class TaskGroupEntity extends BaseTaskGroup {

        public TaskGroupEntity() {
        }

        public TaskGroupEntity(Long id) {
            super(id, TaskStatus.APPROVED);
        }
    }

    private record AdditionalData(String someData) {
    }
}
