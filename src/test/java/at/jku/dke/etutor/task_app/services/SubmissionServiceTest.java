package at.jku.dke.etutor.task_app.services;

import at.jku.dke.etutor.task_app.auth.AuthConstants;
import at.jku.dke.etutor.task_app.data.entities.BaseSubmission;
import at.jku.dke.etutor.task_app.data.entities.BaseTask;
import at.jku.dke.etutor.task_app.data.entities.Submission;
import at.jku.dke.etutor.task_app.data.entities.TaskGroup;
import at.jku.dke.etutor.task_app.data.repositories.SubmissionRepository;
import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.etutor.task_app.dto.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubmissionServiceTest {

    //#region -- getSubmissions ---
    @Test
    void testGetSubmissions() {
        // Arrange
        Page<SubmissionEntity> database = new PageImpl<>(List.of(new SubmissionEntity(UUID.randomUUID(), "k123", "quiz1", new TaskEntity(1L), "de", 1, SubmissionMode.DIAGNOSE, "solution")));
        var service = new SubmissionServiceImpl();
        when(service.getSubmissionRepository().findAll(ArgumentMatchers.<Specification<SubmissionEntity>>any(), any(Pageable.class))).thenReturn(database);

        // Act
        var result = service.getSubmissions(PageRequest.of(1, 10), "k123", 2L, "quiz_1", SubmissionMode.DIAGNOSE);

        // Assert
        assertEquals(1, result.getContent().size());
        var dto = result.getContent().getFirst();
        assertEquals("k123", dto.userId());
        assertEquals("quiz1", dto.assignmentId());
        assertEquals(1L, dto.taskId());
        assertEquals("de", dto.language());
        assertEquals(1, dto.feedbackLevel());
        assertEquals(SubmissionMode.DIAGNOSE, dto.mode());
        assertEquals("solution", dto.submission().solution());
    }
    //#endregion

    //#region --- getEvaluationResult ---
    @Test
    void testGetEvaluationResult() {
        // Arrange
        var service = new SubmissionServiceImpl();
        var grading = new GradingDto(BigDecimal.TWO, BigDecimal.ONE, "ok", Collections.emptyList());
        var entity = new SubmissionEntity(UUID.randomUUID(), "k123", "quiz1", new TaskEntity(1L), "de", 1, SubmissionMode.DIAGNOSE, "solution");
        entity.setEvaluationResult(grading);
        when(service.getSubmissionRepository().findById(any())).thenReturn(Optional.of(entity));

        // Act
        var result = service.getEvaluationResult(UUID.randomUUID());

        // Assert
        assertEquals(grading, result);
    }

    @Test
    void testGetEvaluationResultNotAvailable() {
        // Arrange
        var service = new SubmissionServiceImpl();
        when(service.getSubmissionRepository().findById(any(UUID.class))).thenReturn(Optional.of(new SubmissionEntity(UUID.randomUUID(), "k123", "quiz1", new TaskEntity(1L), "de", 1, SubmissionMode.DIAGNOSE, "solution")));

        // Act
        var result = service.getEvaluationResult(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    void testGetEvaluationResultNotExists() {
        // Arrange
        var service = new SubmissionServiceImpl();
        when(service.getSubmissionRepository().findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.getEvaluationResult(UUID.randomUUID()));
    }
    //#endregion

    //#region --- execute ---
    @Test
    void testExecutePersist() {
        // Arrange
        var service = new SubmissionServiceImpl();
        var dto = new SubmitSubmissionDto<>("k123", "quiz1", 1L, "de", SubmissionMode.DIAGNOSE, 1, new AdditionalData("solution"));
        when(service.getTaskRepository().getReferenceById(anyLong())).thenReturn(new TaskEntity(1L));
        when(service.getSubmissionRepository().save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = service.execute(dto, true);

        // Assert
        assertNotNull(result);
        verify(service.getSubmissionRepository(), never()).delete(any(SubmissionEntity.class));
    }

    @Test
    void testExecuteNotPersist() {
        // Arrange
        var service = new SubmissionServiceImpl();
        var dto = new SubmitSubmissionDto<>("k123", "quiz1", 1L, "de", SubmissionMode.DIAGNOSE, 1, new AdditionalData("solution"));
        when(service.getTaskRepository().getReferenceById(anyLong())).thenReturn(new TaskEntity(1L));
        when(service.getSubmissionRepository().save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = service.execute(dto, false);

        // Assert
        assertNotNull(result);
        verify(service.getSubmissionRepository(), never()).save(any());
    }
    //#endregion

    //#region --- enqueue ---
    @Test
    void testEnqueue() {
        // Arrange
        var service = new SubmissionServiceImpl();
        var dto = new SubmitSubmissionDto<>("k123", "quiz1", 1L, "de", SubmissionMode.DIAGNOSE, 1, new AdditionalData("solution"));
        var id = UUID.randomUUID();
        when(service.getTaskRepository().getReferenceById(anyLong())).thenReturn(new TaskEntity(1L));
        when(service.getSubmissionRepository().saveAndFlush(any())).thenAnswer(invocation -> {
            var entity = invocation.getArgument(0);
            ((SubmissionEntity) entity).setId(id);
            return entity;
        });

        // Act
        var result = service.enqueue(dto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result);
    }
    //#endregion

    //#region --- delete ---
    @Test
    void testDelete() {
        // Arrange
        var service = new SubmissionServiceImpl();
        var id = UUID.randomUUID();

        // Act
        service.delete(id);

        // Assert
        verify(service.getSubmissionRepository(), times(1)).deleteById(id);
    }
    //#endregion

    @Test
    void testAuthorities() throws NoSuchMethodException {
        var enqueue = BaseSubmissionService.class.getMethod("enqueue", SubmitSubmissionDto.class).getAnnotation(PreAuthorize.class);
        var execute = BaseSubmissionService.class.getMethod("execute", SubmitSubmissionDto.class, boolean.class).getAnnotation(PreAuthorize.class);
        var getEvaluationResult = BaseSubmissionService.class.getMethod("getEvaluationResult", UUID.class).getAnnotation(PreAuthorize.class);
        var getSubmissions = BaseSubmissionService.class.getMethod("getSubmissions", Pageable.class, String.class, Long.class, String.class, SubmissionMode.class).getAnnotation(PreAuthorize.class);

        assertEquals(AuthConstants.SUBMIT_AUTHORITY, enqueue.value());
        assertEquals(AuthConstants.SUBMIT_AUTHORITY, execute.value());
        assertEquals(AuthConstants.SUBMIT_AUTHORITY, getEvaluationResult.value());
        assertEquals(AuthConstants.READ_SUBMISSION_AUTHORITY, getSubmissions.value());
    }

    private static class SubmissionServiceImpl extends BaseSubmissionService<TaskEntity, SubmissionEntity, AdditionalData> {

        protected SubmissionServiceImpl() {
            //noinspection unchecked
            super(mock(SubmissionRepository.class), mock(TaskRepository.class));
        }

        public SubmissionRepository<SubmissionEntity> getSubmissionRepository() {
            return submissionRepository;
        }

        public TaskRepository<TaskEntity> getTaskRepository() {
            return taskRepository;
        }

        @Override
        protected SubmissionEntity createSubmissionEntity(SubmitSubmissionDto<AdditionalData> dto) {
            var submission = new SubmissionEntity();
            submission.setSolution(dto.submission().solution());
            return submission;
        }

        @Override
        protected GradingDto evaluate(SubmitSubmissionDto<AdditionalData> dto) {
            return new GradingDto(BigDecimal.TEN, BigDecimal.ONE, "ok", List.of(new CriterionDto("Syntax", BigDecimal.ONE, true, "ok")));
        }

        @Override
        protected AdditionalData mapSubmissionToSubmissionData(SubmissionEntity submission) {
            return new AdditionalData(submission.getSolution());
        }

    }

    private static class SubmissionEntity extends BaseSubmission<TaskEntity> {
        private String solution;

        public SubmissionEntity() {
        }

        public SubmissionEntity(UUID id, String userId, String assignmentId, TaskEntity task, String language, int feedbackLevel, SubmissionMode mode, String solution) {
            super(userId, assignmentId, task, language, feedbackLevel, mode);
            this.solution = solution;
            this.setId(id);
        }

        public String getSolution() {
            return solution;
        }

        public void setSolution(String solution) {
            this.solution = solution;
        }
    }

    private static class TaskEntity extends BaseTask<TaskGroup> {

        public TaskEntity(Long id) {
            super(id, BigDecimal.ZERO, TaskStatus.APPROVED, null);
        }

    }

    private record AdditionalData(String solution) {
    }
}
