package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.auth.AuthConstants;
import at.jku.dke.etutor.task_app.data.entities.BaseTaskGroup;
import at.jku.dke.etutor.task_app.data.entities.BaseTaskInGroup;
import at.jku.dke.etutor.task_app.data.repositories.TaskGroupRepository;
import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.dto.TaskStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.Serializable;
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
        when(service.getRepository().save(any())).thenAnswer(invocation -> new PersistedEntity(invocation.getArgument(0)));

        // Act
        var result = service.create(id, dto);

        // Assert
        assertNotNull(result);
        assertFalse(service.beforeCreateCalled instanceof PersistedEntity);
        assertInstanceOf(PersistedEntity.class, service.afterCreateCalled);
    }
    @Test
    void testCreateNullGroupId() {
        // Act
        final long id = 4L;
        final TaskStatus taskStatus = TaskStatus.APPROVED;
        final String someData = "some data";
        final BigDecimal maxPoints = BigDecimal.TEN;

        var service = new TaskServiceImpl();
        var dto = new ModifyTaskDto<>(null, maxPoints, "test", taskStatus, new AdditionalData(someData));
        when(service.getRepository().existsById(id)).thenReturn(false);
        when(service.getRepository().save(any())).thenAnswer(invocation -> new PersistedEntity(invocation.getArgument(0)));

        // Act & Assert
        assertThrows(ValidationException.class, () -> service.create(id, dto));
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
        when(service.getRepository().save(any())).thenAnswer(invocation -> new PersistedEntity(invocation.getArgument(0)));

        // Act
        var result = service.update(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.status(), entity.getStatus());
        assertEquals(dto.maxPoints(), entity.getMaxPoints());
        assertEquals(dto.taskGroupId(), entity.getTaskGroup().getId());
        assertEquals(dto.additionalData().someData(), entity.getSomeData());
        assertInstanceOf(PersistedEntity.class, service.afterUpdateCalled);
    }

    @Test
    void testUpdateNullGroupId() {
        // Act
        final long id = 3L;

        var service = new TaskServiceImpl();
        var dto = new ModifyTaskDto<>(null, BigDecimal.TEN, "test", TaskStatus.APPROVED, new AdditionalData("some data"));
        var entity = new TaskEntity(id, TaskStatus.DRAFT, new TaskGroupEntity(1L), "old data", BigDecimal.TWO);
        when(service.getRepository().findById(id)).thenReturn(Optional.of(entity));
        when(service.getRepository().save(any())).thenAnswer(invocation -> new PersistedEntity(invocation.getArgument(0)));

        // Act & Assert
        assertThrows(ValidationException.class, () -> service.update(id, dto));
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
        assertEquals(id, service.beforeDeleteCalled);
        assertEquals(id, service.afterDeleteCalled);
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

    @Test
    void testAuthorities() {
        var cls = BaseTaskService.class.getAnnotation(PreAuthorize.class);
        assertEquals(AuthConstants.CRUD_AUTHORITY, cls.value());
    }

    private static class TaskServiceImpl extends BaseTaskInGroupService<TaskEntity, TaskGroupEntity, AdditionalData> {

        private TaskEntity beforeCreateCalled;
        private TaskEntity afterCreateCalled;
        private TaskEntity afterUpdateCalled;
        private long beforeDeleteCalled = -1;
        private long afterDeleteCalled = -1;

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

        @Override
        protected void beforeCreate(TaskEntity task, ModifyTaskDto<AdditionalData> dto) {
            this.beforeCreateCalled = task;
        }

        @Override
        protected void afterCreate(TaskEntity task, ModifyTaskDto<AdditionalData> dto) {
            this.afterCreateCalled = task;
        }

        @Override
        protected void afterUpdate(TaskEntity task, ModifyTaskDto<AdditionalData> dto) {
            this.afterUpdateCalled = task;
        }

        @Override
        protected void beforeDelete(long id) {
            this.beforeDeleteCalled = id;
        }

        @Override
        protected void afterDelete(long id) {
            this.afterDeleteCalled = id;
        }
    }

    private static class TaskEntity extends BaseTaskInGroup<TaskGroupEntity> implements Serializable {
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

    private static class PersistedEntity extends TaskEntity {
        public PersistedEntity(TaskEntity te) {
            super(te.getId(), te.getStatus(), te.getTaskGroup(), te.getSomeData(), te.getMaxPoints());
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
