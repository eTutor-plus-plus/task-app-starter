package at.jku.dke.etutor.task_app.test;

/**
 * A record that holds the statistics of a request.
 *
 * @param type     The type of the request (sync or async).
 * @param duration The duration of the request in milliseconds.
 * @param failed   Whether the request failed or not.
 */
public record RequestStatistic(String type, long duration, boolean failed) {
}
