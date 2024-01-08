/**
 * This package contains all authentication-related classes.
 * <p>
 * In order to define clients, specify them in the application properties in following format:
 * <pre>
 * clients:
 *   api-keys:
 *     - name: task-administration
 *       key: my-secret-administration-key
 *       roles:
 *          - CRUD
 *          - SUBMIT
 *     - name: plag-check
 *       key: my-secret-plag-check-key
 *       role: READ_SUBMISSION
 *     - ...
 * </pre>
 */
package at.jku.dke.etutor.task_app.auth;
