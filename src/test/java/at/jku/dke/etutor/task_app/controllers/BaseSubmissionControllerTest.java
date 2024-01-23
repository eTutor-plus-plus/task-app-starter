package at.jku.dke.etutor.task_app.controllers;

import at.jku.dke.etutor.task_app.data.entities.BaseTask;
import at.jku.dke.etutor.task_app.data.entities.BaseTaskInGroup;
import at.jku.dke.etutor.task_app.data.entities.TaskGroup;
import at.jku.dke.etutor.task_app.dto.*;
import at.jku.dke.etutor.task_app.services.BaseSubmissionService;
import at.jku.dke.etutor.task_app.services.SubmissionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.*;

class BaseSubmissionControllerTest {

    @Test
    void submitEnqueue() {
        // Arrange
        var id = UUID.randomUUID();
        SubmissionController controller = new SubmissionController();
        when(controller.getSubmissionService().enqueue(any())).thenReturn(id);

        // Act
        var result = controller.submit(new SubmitSubmissionDto<>("user", "assignment", 1L, "en", SubmissionMode.SUBMIT, 1, "juhu"),
            true, false);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(id.toString(), result.getBody());
        assertEquals(MediaType.TEXT_PLAIN, result.getHeaders().getContentType());
    }

    @Test
    void submitExecute() {
        // Arrange
        SubmissionController controller = new SubmissionController();
        when(controller.getSubmissionService().execute(any(), booleanThat(persist -> !persist)))
            .thenReturn(new GradingResultDto(null, new GradingDto(BigDecimal.TWO, BigDecimal.ZERO, "bad", Collections.emptyList())));

        // Act
        var result = controller.submit(new SubmitSubmissionDto<>("user", "assignment", 1L, "en", SubmissionMode.SUBMIT, 1, "juhu"),
            false, false);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(GradingResultDto.class, result.getBody().getClass());
    }

    @Test
    void submitExecutePersist() {
        // Arrange
        SubmissionController controller = new SubmissionController();
        when(controller.getSubmissionService().execute(any(), booleanThat(persist -> persist)))
            .thenReturn(new GradingResultDto(UUID.randomUUID(), new GradingDto(BigDecimal.TWO, BigDecimal.ZERO, "bad", Collections.emptyList())));

        // Act
        var result = controller.submit(new SubmitSubmissionDto<>("user", "assignment", 1L, "en", SubmissionMode.SUBMIT, 1, "juhu"),
            false, true);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(GradingResultDto.class, result.getBody().getClass());
    }

    @Test
    void getResult() {
        // Arrange
        SubmissionController controller = new SubmissionController();
        when(controller.getSubmissionService().getEvaluationResult(any()))
            .thenReturn(new GradingDto(BigDecimal.TWO, BigDecimal.ZERO, "bad", Collections.emptyList()));

        // Act
        var result = controller.getResult(UUID.randomUUID(), 1, false);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(GradingDto.class, result.getBody().getClass());
    }


    @Test
    void getResultDelete() {
        // Arrange
        var id = UUID.randomUUID();
        SubmissionController controller = new SubmissionController();
        when(controller.getSubmissionService().getEvaluationResult(any()))
            .thenReturn(new GradingDto(BigDecimal.TWO, BigDecimal.ZERO, "bad", Collections.emptyList()));

        // Act
        var result = controller.getResult(id, 1, true);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(GradingDto.class, result.getBody().getClass());
        verify(controller.getSubmissionService(), times(1)).delete(id);
    }

    @Test
    void getResultWait() {
        // Arrange
        SubmissionController controller = new SubmissionController();
        when(controller.getSubmissionService().getEvaluationResult(any())).thenReturn(
            null,
            new GradingDto(BigDecimal.TWO, BigDecimal.ZERO, "bad", Collections.emptyList())
        );

        // Act
        long start = System.currentTimeMillis();
        var result = controller.getResult(UUID.randomUUID(), 5, false);
        var end = System.currentTimeMillis();
        var diff = end - start;

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(GradingDto.class, result.getBody().getClass());
        assertTrue(diff >= 900, "Execution was too fast");
        assertTrue(diff <= 900 * 3, "Execution was too slow");
    }

    @Test
    void getResultTimeout() {
        // Arrange
        SubmissionController controller = new SubmissionController();
        when(controller.getSubmissionService().getEvaluationResult(any())).thenReturn(null);

        // Act
        long start = System.currentTimeMillis();
        var result = controller.getResult(UUID.randomUUID(), 3, false);
        var end = System.currentTimeMillis();
        var diff = end - start;

        // Assert
        assertEquals(HttpStatus.REQUEST_TIMEOUT, result.getStatusCode());
        assertNull(result.getBody());
        assertTrue(diff >= 900 * 2, "Execution was too fast");
        assertTrue(diff <= 900 * 4, "Execution was too slow");
    }

    @Test
    void getResultNegativeTimeout() {
        // Arrange
        SubmissionController controller = new SubmissionController();
        when(controller.getSubmissionService().getEvaluationResult(any())).thenReturn(null);

        // Act
        long start = System.currentTimeMillis();
        var result = controller.getResult(UUID.randomUUID(), -1, false);
        var end = System.currentTimeMillis();
        var diff = end - start;

        // Assert
        assertEquals(HttpStatus.REQUEST_TIMEOUT, result.getStatusCode());
        assertNull(result.getBody());
        assertTrue(diff <= 1000, "Execution was too slow");
    }

    @Test
    void getResultInterrupted() throws InterruptedException {
        // Arrange
        Thread thread = new Thread(() -> {
            SubmissionController controller = new SubmissionController();
            when(controller.getSubmissionService().getEvaluationResult(any())).thenReturn(null);

            // Act
            var result = controller.getResult(UUID.randomUUID(), 20, false);

            // Assert
            assertEquals(HttpStatus.REQUEST_TIMEOUT, result.getStatusCode());
            assertNull(result.getBody());
        });

        // Act
        thread.start();
        Thread.sleep(2000);
        thread.interrupt();
    }

    @Test
    void getResultNotExistingSubmission() {
        // Arrange
        SubmissionController controller = new SubmissionController();
        when(controller.getSubmissionService().getEvaluationResult(any())).thenThrow(new EntityNotFoundException());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> controller.getResult(UUID.randomUUID(), 1, false));
    }

    @Test
    void getSubmissions() {
        // Arrange
        SubmissionController controller = new SubmissionController();
        when(controller.getSubmissionService().getSubmissions(null, null, null, null, null))
            .thenReturn(new PageImpl<>(List.of(new SubmissionDto<>(UUID.randomUUID(), "user", "assignment", 1L, null, "de", 0, null, "submission", null))));

        // Act
        var result = controller.getSubmissions(null, null, null, null, null);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().getContent().size());
    }

    private static class SubmissionController extends BaseSubmissionController<TestTask, String> {
        public SubmissionController() {
            //noinspection unchecked
            super(mock(BaseSubmissionService.class));
        }

        public SubmissionService<String> getSubmissionService() {
            return submissionService;
        }
    }

    private static class TestTask extends BaseTaskInGroup<TaskGroup> {
        public TestTask(Long id) {
            super(id, BigDecimal.ZERO, TaskStatus.APPROVED, null);
        }
    }
}
