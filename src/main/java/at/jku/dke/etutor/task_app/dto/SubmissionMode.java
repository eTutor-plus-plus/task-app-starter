package at.jku.dke.etutor.task_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Provides the supported submission modes.
 */
@Schema(description = "The supported submission modes. Might be expanded in future releases.")
public enum SubmissionMode {
    /**
     * Run submission (using diagnose) and only return syntax errors but no semantic errors.
     */
    @Schema(description = "Run submission (using diagnose) and only return syntax errors but no semantic errors.")
    RUN,

    /**
     * Check submission and return feedback.
     */
    @Schema(description = "Check submission and return feedback.")
    DIAGNOSE,

    /**
     * Check submission without feedback (submission that is counted).
     */
    @Schema(description = "Check submission without feedback (submission that is counted).")
    SUBMIT
}
