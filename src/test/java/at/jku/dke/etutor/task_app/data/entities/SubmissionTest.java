package at.jku.dke.etutor.task_app.data.entities;

import at.jku.dke.etutor.task_app.dto.GradingDto;
import at.jku.dke.etutor.task_app.dto.SubmissionMode;
import org.hibernate.proxy.AbstractLazyInitializer;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.junit.jupiter.api.Test;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionTest {

    @Test
    void testConstructor() {
        // Arrange
        final String userId = "userId";
        final String assignmentId = "assignmentId";
        final TaskTestEntity task = new TaskTestEntity();
        final String language = "language";
        final int feedbackLevel = 1;
        final SubmissionMode mode = SubmissionMode.DIAGNOSE;

        // Act
        var submission = new SubmissionTestEntity(userId, assignmentId, task, language, feedbackLevel, mode);

        // Assert
        assertEquals(userId, submission.getUserId());
        assertEquals(assignmentId, submission.getAssignmentId());
        assertEquals(task, submission.getTask());
        assertEquals(language, submission.getLanguage());
        assertEquals(feedbackLevel, submission.getFeedbackLevel());
        assertEquals(mode, submission.getMode());
    }

    @Test
    void testGetSetId() {
        // Arrange
        var submission = new SubmissionTestEntity();
        final UUID value = UUID.randomUUID();

        // Act
        submission.setId(value);
        var result = submission.getId();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testGetSetAssignmentId() {
        // Arrange
        var submission = new SubmissionTestEntity();
        final String value = "assignmentId";

        // Act
        submission.setAssignmentId(value);
        var result = submission.getAssignmentId();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testGetSetUserId() {
        // Arrange
        var submission = new SubmissionTestEntity();
        final String value = "userId";

        // Act
        submission.setUserId(value);
        var result = submission.getUserId();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testGetSetTask() {
        // Arrange
        var submission = new SubmissionTestEntity();
        final TaskTestEntity value = new TaskTestEntity();

        // Act
        submission.setTask(value);
        var result = submission.getTask();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testGetSetSubmissionTime() {
        // Arrange
        var submission = new SubmissionTestEntity();
        final Instant value = Instant.now();

        // Act
        submission.setSubmissionTime(value);
        var result = submission.getSubmissionTime();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testGetSetFeedbackLevel() {
        // Arrange
        var submission = new SubmissionTestEntity();
        final int value = 1;

        // Act
        submission.setFeedbackLevel(value);
        var result = submission.getFeedbackLevel();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testGetSetLanguage() {
        // Arrange
        var submission = new SubmissionTestEntity();
        final String value = "de";

        // Act
        submission.setLanguage(value);
        var result = submission.getLanguage();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testGetSetMode() {
        // Arrange
        var submission = new SubmissionTestEntity();
        final SubmissionMode value = SubmissionMode.DIAGNOSE;

        // Act
        submission.setMode(value);
        var result = submission.getMode();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testGetSetEvaluationResult() {
        // Arrange
        var submission = new SubmissionTestEntity();
        final GradingDto value = new GradingDto(BigDecimal.TWO, BigDecimal.ONE, "feedback", new ArrayList<>());

        // Act
        submission.setEvaluationResult(value);
        var result = submission.getEvaluationResult();

        // Assert
        assertEquals(value, result);
    }

    @Test
    void testEquals() {
        // Arrange
        var id = UUID.randomUUID();
        var submission1 = new SubmissionTestEntity();
        var submission2 = new SubmissionTestEntity();

        var submission3 = new SubmissionTestEntity();
        submission3.setId(id);

        var submission4 = new SubmissionTestEntity();
        submission4.setId(id);
        submission4.setAssignmentId("assignmentId");

        var submission5 = new SubmissionTestEntity();
        submission5.setId(id);
        submission5.setAssignmentId("assignmentId");
        submission5.setUserId("userId");

        var submission6 = new SubmissionTestEntity();
        submission6.setId(id);
        submission6.setAssignmentId("assignmentId");
        submission6.setUserId("userId");
        submission6.setTask(new TaskTestEntity());

        var submission7 = new SubmissionTestEntity();
        submission7.setId(id);
        submission7.setAssignmentId("assignmentId");
        submission7.setUserId("userId");
        submission7.setTask(new TaskTestEntity());
        submission7.setSubmissionTime(Instant.now());

        var submission8 = new SubmissionTestEntity();
        submission8.setId(id);
        submission8.setAssignmentId("assignmentId");
        submission8.setUserId("userId");
        submission8.setTask(new TaskTestEntity());
        submission8.setSubmissionTime(Instant.now());
        submission8.setFeedbackLevel(1);

        var submission9 = new SubmissionTestEntity();
        submission9.setId(id);
        submission9.setAssignmentId("assignmentId");
        submission9.setUserId("userId");
        submission9.setTask(new TaskTestEntity());
        submission9.setSubmissionTime(Instant.now());
        submission9.setFeedbackLevel(1);
        submission9.setLanguage("de");

        var submission10 = new SubmissionTestEntity();
        submission10.setId(id);
        submission10.setAssignmentId("assignmentId");
        submission10.setUserId("userId");
        submission10.setTask(new TaskTestEntity());
        submission10.setSubmissionTime(Instant.now());
        submission10.setFeedbackLevel(1);
        submission10.setLanguage("de");
        submission10.setMode(SubmissionMode.DIAGNOSE);

        var submission11 = new SubmissionTestEntity();
        submission11.setId(id);
        submission11.setAssignmentId("assignmentId");
        submission11.setUserId("userId");
        submission11.setTask(new TaskTestEntity());
        submission11.setSubmissionTime(Instant.now());
        submission11.setFeedbackLevel(1);
        submission11.setLanguage("de");
        submission11.setMode(SubmissionMode.DIAGNOSE);
        submission11.setEvaluationResult(new GradingDto(BigDecimal.TWO, BigDecimal.ONE, "feedback", new ArrayList<>()));

        // Act
        var result1 = submission1.equals(submission2);
        var result2 = submission3.equals(submission4);
        var result3 = submission4.equals(submission5);
        var result4 = submission5.equals(submission6);
        var result5 = submission6.equals(submission7);
        var result6 = submission7.equals(submission8);
        var result7 = submission8.equals(submission9);
        var result8 = submission9.equals(submission10);
        var result9 = submission10.equals(submission11);

        // Assert
        assertFalse(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertTrue(result5);
        assertTrue(result6);
        assertTrue(result7);
        assertTrue(result8);
        assertTrue(result9);
    }

    @Test
    void testEqualsIdNull() {
        // Arrange
        var submission1 = new SubmissionTestEntity();
        var submission2 = new SubmissionTestEntity();
        submission2.setId(UUID.randomUUID());

        // Act
        var result = submission1.equals(submission2);

        // Assert
        assertFalse(result);
    }

    @Test
    void testEqualsIdNotEqual() {
        // Arrange
        var submission1 = new SubmissionTestEntity();
        submission1.setId(UUID.randomUUID());
        var submission2 = new SubmissionTestEntity();
        submission2.setId(UUID.randomUUID());

        // Act
        var result = submission1.equals(submission2);

        // Assert
        assertFalse(result);
    }

    @Test
    void testEqualsSame() {
        // Arrange
        var submission = new SubmissionTestEntity();

        // Act
        @SuppressWarnings("EqualsWithItself") var result = submission.equals(submission);

        // Assert
        //noinspection ConstantValue
        assertTrue(result);
    }

    @Test
    void testEqualsNull() {
        // Arrange
        var submission = new SubmissionTestEntity();

        // Act
        @SuppressWarnings("ConstantValue") var result = submission.equals(null);

        // Assert
        //noinspection ConstantValue
        assertFalse(result);
    }

    @Test
    void testEqualsOtherClass() {
        // Arrange
        var submission = new SubmissionTestEntity();

        // Act
        var result = submission.equals(new Object());

        // Assert
        assertFalse(result);
    }

    @Test
    void testEqualsHibernateProxy() {
        // Arrange
        var task1 = new HibernateProxySubmissionTestEntity();
        task1.setId(UUID.randomUUID());
        var task2 = new SubmissionTestEntity();
        task2.setId(task1.getId());

        // Act
        var result = task1.equals(task2);

        // Assert
        assertTrue(result);
    }

    @Test
    void testEqualsOtherHibernateProxy() {
        // Arrange
        var task1 = new SubmissionTestEntity();
        task1.setId(UUID.randomUUID());
        var task2 = new HibernateProxySubmissionTestEntity();
        task2.setId(task1.getId());

        // Act
        var result = task1.equals(task2);

        // Assert
        assertTrue(result);
    }

    @Test
    void testHashCode() {
        // Arrange
        var submission1 = new SubmissionTestEntity();
        var submission2 = new SubmissionTestEntity();

        var submission3 = new SubmissionTestEntity();
        submission3.setId(UUID.randomUUID());

        var submission4 = new SubmissionTestEntity();
        submission4.setId(UUID.randomUUID());
        submission4.setAssignmentId("assignmentId");

        var submission5 = new SubmissionTestEntity();
        submission5.setId(UUID.randomUUID());
        submission5.setAssignmentId("assignmentId");
        submission5.setUserId("userId");

        var submission6 = new SubmissionTestEntity();
        submission6.setId(UUID.randomUUID());
        submission6.setAssignmentId("assignmentId");
        submission6.setUserId("userId");
        submission6.setTask(new TaskTestEntity());

        var submission7 = new SubmissionTestEntity();
        submission7.setId(UUID.randomUUID());
        submission7.setAssignmentId("assignmentId");
        submission7.setUserId("userId");
        submission7.setTask(new TaskTestEntity());
        submission7.setSubmissionTime(Instant.now());

        var submission8 = new SubmissionTestEntity();
        submission8.setId(UUID.randomUUID());
        submission8.setAssignmentId("assignmentId");
        submission8.setUserId("userId");
        submission8.setTask(new TaskTestEntity());
        submission8.setSubmissionTime(Instant.now());
        submission8.setFeedbackLevel(1);

        var submission9 = new SubmissionTestEntity();
        submission9.setId(UUID.randomUUID());
        submission9.setAssignmentId("assignmentId");
        submission9.setUserId("userId");
        submission9.setTask(new TaskTestEntity());
        submission9.setSubmissionTime(Instant.now());
        submission9.setFeedbackLevel(1);
        submission9.setLanguage("de");

        var submission10 = new SubmissionTestEntity();
        submission10.setId(UUID.randomUUID());
        submission10.setAssignmentId("assignmentId");
        submission10.setUserId("userId");
        submission10.setTask(new TaskTestEntity());
        submission10.setSubmissionTime(Instant.now());
        submission10.setFeedbackLevel(1);
        submission10.setLanguage("de");
        submission10.setMode(SubmissionMode.DIAGNOSE);

        var submission11 = new SubmissionTestEntity();
        submission11.setId(UUID.randomUUID());
        submission11.setAssignmentId("assignmentId");
        submission11.setUserId("userId");
        submission11.setTask(new TaskTestEntity());
        submission11.setSubmissionTime(Instant.now());
        submission11.setFeedbackLevel(1);
        submission11.setLanguage("de");
        submission11.setMode(SubmissionMode.DIAGNOSE);
        submission11.setEvaluationResult(new GradingDto(BigDecimal.TWO, BigDecimal.ONE, "feedback", new ArrayList<>()));

        // Act
        var result1 = submission1.hashCode() == submission2.hashCode();
        var result2 = submission3.hashCode() == submission4.hashCode();
        var result3 = submission4.hashCode() == submission5.hashCode();
        var result4 = submission5.hashCode() == submission6.hashCode();
        var result5 = submission6.hashCode() == submission7.hashCode();
        var result6 = submission7.hashCode() == submission8.hashCode();
        var result7 = submission8.hashCode() == submission9.hashCode();
        var result8 = submission9.hashCode() == submission10.hashCode();
        var result9 = submission10.hashCode() == submission11.hashCode();

        // Assert
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertTrue(result5);
        assertTrue(result6);
        assertTrue(result7);
        assertTrue(result8);
        assertTrue(result9);
    }

    @Test
    void testHashCodeHibernateProxy() {
        // Arrange
        var task1 = new HibernateProxySubmissionTestEntity();
        var task2 = new SubmissionTestEntity();

        // Act
        var result = task1.hashCode() == task2.hashCode();

        // Assert
        assertTrue(result);
    }

    @Test
    void testToString() {
        // Arrange
        var submission = new SubmissionTestEntity();
        submission.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        submission.setAssignmentId("quiz2");
        submission.setUserId("k2345");
        submission.setTask(new TaskTestEntity());
        submission.setSubmissionTime(Instant.now());
        submission.setFeedbackLevel(1);
        submission.setLanguage("de");
        submission.setMode(SubmissionMode.DIAGNOSE);
        submission.setEvaluationResult(new GradingDto(BigDecimal.TWO, BigDecimal.ONE, "feedback", new ArrayList<>()));

        // Act
        var result = submission.toString();

        // Assert
        assertEquals("SubmissionTestEntity[id='00000000-0000-0000-0000-000000000000', userId='k2345', assignmentId='quiz2']", result);
    }

    private static class SubmissionTestEntity extends BaseSubmission<TaskTestEntity> {
        public SubmissionTestEntity() {
        }

        public SubmissionTestEntity(String userId, String assignmentId, TaskTestEntity task, String language, int feedbackLevel, SubmissionMode mode) {
            super(userId, assignmentId, task, language, feedbackLevel, mode);
        }
    }

    private static class TaskTestEntity extends BaseTaskInGroup<TaskGroup> {
    }

    private static class HibernateProxySubmissionTestEntity extends SubmissionTestEntity implements HibernateProxy {
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
                    return SubmissionTestEntity.class;
                }

                @Override
                public Class<?> getImplementationClass() {
                    return null;
                }
            };
        }
    }
}
