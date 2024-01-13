package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.data.entities.BaseTaskGroup;
import at.jku.dke.etutor.task_app.data.repositories.TaskGroupRepository;
import at.jku.dke.etutor.task_app.dto.ModifyTaskGroupDto;
import at.jku.dke.etutor.task_app.dto.TaskStatus;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import java.io.Serializable;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TaskGroupServiceTest {

    //#region --- Get ---
    @Test
    void testGetExists() {
        // Arrange
        final long id = 9L;
        var service = new TaskGroupServiceImpl();
        when(service.getRepository().findById(id)).thenReturn(Optional.of(new TaskGroupEntity(id)));

        // Act
        var result = service.get(id);

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    void testGetNotExists() {
        // Arrange
        var service = new TaskGroupServiceImpl();
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
        final TaskStatus taskStatus = TaskStatus.APPROVED;
        final String someData = "some data";

        var service = new TaskGroupServiceImpl();
        var dto = new ModifyTaskGroupDto<>("test", taskStatus, new AdditionalData(someData));
        when(service.getRepository().existsById(id)).thenReturn(false);
        when(service.getRepository().save(any())).thenAnswer(invocation -> new PersistedEntity(invocation.getArgument(0)));

        // Act
        var result = service.create(id, dto);

        // Assert
        assertNotNull(result);
        assertFalse(service.beforeCreateCalled instanceof PersistedEntity);
        assertInstanceOf(PersistedEntity.class, service.afterCreateCalled);
    }

    @Test
    void testCreateAlreadyExists() {
        // Act
        final long id = 1L;

        var service = new TaskGroupServiceImpl();
        var dto = new ModifyTaskGroupDto<>("test", TaskStatus.APPROVED, new AdditionalData("some data"));
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

        var service = new TaskGroupServiceImpl();
        var dto = new ModifyTaskGroupDto<>("test", TaskStatus.APPROVED, new AdditionalData("some data"));
        var entity = new TaskGroupEntity(id, TaskStatus.DRAFT, "old data");
        when(service.getRepository().findById(id)).thenReturn(Optional.of(entity));
        when(service.getRepository().save(any())).thenAnswer(invocation -> new PersistedEntity(invocation.getArgument(0)));

        // Act
        var result = service.update(id, dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.status(), entity.getStatus());
        assertEquals(dto.additionalData().someData(), entity.getSomeData());
        assertInstanceOf(PersistedEntity.class, service.afterUpdateCalled);
    }

    @Test
    void testUpdateNotExists() {
        // Act
        final long id = 1L;

        var service = new TaskGroupServiceImpl();
        var dto = new ModifyTaskGroupDto<>("test", TaskStatus.APPROVED, new AdditionalData("some data"));
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
        var service = new TaskGroupServiceImpl();

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
        var service = new TaskGroupServiceImpl();

        // Act
        service.delete(id);

        // Assert
        verify(service.getRepository(), times(1)).deleteById(id);
    }
    //#endregion

    // TODO: test authorities

    private static class TaskGroupServiceImpl extends BaseTaskGroupService<TaskGroupEntity, AdditionalData> {

        private TaskGroupEntity beforeCreateCalled;
        private TaskGroupEntity afterCreateCalled;
        private TaskGroupEntity afterUpdateCalled;
        private long beforeDeleteCalled = -1;
        private long afterDeleteCalled = -1;

        public TaskGroupServiceImpl() {
            //noinspection unchecked
            super(mock(TaskGroupRepository.class));
        }

        public TaskGroupRepository<TaskGroupEntity> getRepository() {
            return super.repository;
        }

        @Override
        protected TaskGroupEntity createTaskGroup(long id, ModifyTaskGroupDto<AdditionalData> dto) {
            var entity = new TaskGroupEntity();
            entity.setSomeData(dto.additionalData().someData());
            return entity;
        }

        @Override
        protected void updateTaskGroup(TaskGroupEntity taskGroup, ModifyTaskGroupDto<AdditionalData> dto) {
            taskGroup.setSomeData(dto.additionalData().someData());
        }

        @Override
        protected void beforeCreate(TaskGroupEntity taskGroup) {
            this.beforeCreateCalled = taskGroup;
        }

        @Override
        protected void afterCreate(TaskGroupEntity taskGroup) {
            this.afterCreateCalled = taskGroup;
        }

        @Override
        protected void afterUpdate(TaskGroupEntity taskGroup) {
            this.afterUpdateCalled = taskGroup;
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

    private static class TaskGroupEntity extends BaseTaskGroup implements Serializable {
        private String someData;

        public TaskGroupEntity() {
        }

        public TaskGroupEntity(Long id) {
            super(id, TaskStatus.APPROVED);
        }

        public TaskGroupEntity(Long id, TaskStatus status, String someData) {
            super(id, status);
            this.someData = someData;
        }

        public String getSomeData() {
            return someData;
        }

        public void setSomeData(String someData) {
            this.someData = someData;
        }
    }

    private static class PersistedEntity extends TaskGroupEntity {
        public PersistedEntity(TaskGroupEntity te) {
            super(te.getId(), te.getStatus(), te.getSomeData());
        }
    }

    private record AdditionalData(String someData) {
    }
}
