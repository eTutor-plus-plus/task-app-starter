/**
 * This package contains the base-classes for database entities used to store task groups, tasks and submissions.
 * <p>
 * Extend the base-classes to add additional fields to the entities.
 * The following PostgreSQL-script can be used to create the database tables:
 * <pre>
 * CREATE TYPE task_status AS ENUM ('draft', 'ready_for_approval', 'approved');
 * CREATE TYPE submission_mode AS ENUM ('run', 'diagnose', 'submit');
 *
 * CREATE CAST (CHARACTER VARYING as task_status) WITH INOUT AS IMPLICIT;
 * CREATE CAST (CHARACTER VARYING as submission_mode) WITH INOUT AS IMPLICIT;
 *
 * CREATE TABLE task_group
 * (
 *     id        BIGINT      NOT NULL,
 *     status    TASK_STATUS NOT NULL,
 *     -- ... your task group data
 *     CONSTRAINT task_group_pk PRIMARY KEY (id)
 * );
 *
 * CREATE TABLE task
 * (
 *     id            BIGINT        NOT NULL,
 *     max_points    NUMERIC(7, 2) NOT NULL,
 *     -- ... your task data
 *     status        TASK_STATUS   NOT NULL,
 *     task_group_id BIGINT,
 *     CONSTRAINT task_pk PRIMARY KEY (id),
 *     CONSTRAINT task_task_group_fk FOREIGN KEY (task_group_id) REFERENCES task_group (id)
 *         ON DELETE CASCADE
 * );
 *
 * CREATE TABLE submission
 * (
 *      id                 UUID                     DEFAULT gen_random_uuid(),
 *      user_id            VARCHAR(255),
 *      assignment_id      VARCHAR(255),
 *      task_id            BIGINT,
 *      submission_time    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
 *      language           VARCHAR(2)      NOT NULL DEFAULT 'en',
 *      mode               submission_mode NOT NULL,
 *      feedback_level     INT             NOT NULL,
 *      evaluation_result  JSONB,
 *     -- ... your submission data
 *      CONSTRAINT submission_pk PRIMARY KEY (id),
 *      CONSTRAINT submission_task_fk FOREIGN KEY (task_id) REFERENCES task (id)
 *          ON DELETE CASCADE
 );
 * </pre>
 */
package at.jku.dke.etutor.task_app.data.entities;
