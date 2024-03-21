package at.jku.dke.etutor.task_app.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A thread that simulates a user submitting a request to the task app.
 */
public abstract class SubmissionThread extends Thread {

    /**
     * The statistic type for synchronous submissions.
     */
    public static final String STATISTIC_SYNC = "sync";

    /**
     * The statistic type for asynchronous submissions.
     */
    public static final String STATISTIC_ASYNC = "async";

    /**
     * The logger.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(SubmissionThread.class);

    private static final String[] MODES = new String[]{
        "RUN",
        "DIAGNOSE",
        "SUBMIT"
    };

    /**
     * The URL of the task app.
     */
    protected final String url;

    /**
     * The API key to use for the requests.
     */
    protected final String apiKey;

    /**
     * The random generator.
     */
    protected final Random random;

    private final int initialSleep;
    private final int totalAmountOfRequests;
    private final int pauseBetweenRequestsInMs;
    private final List<RequestStatistic> statisticList;

    /**
     * Creates a new instance of the load test thread.
     *
     * @param name                     The name of the thread. This is used for logging.
     * @param url                      The URL of the task app. This should not contain a trailing slash.
     * @param apiKey                   The API key to use for the requests.
     * @param initialSleep             The initial sleep time in milliseconds.
     * @param totalAmountOfRequests    The total amount of requests to submit.
     * @param pauseBetweenRequestsInMs The pause between requests in milliseconds.
     */
    protected SubmissionThread(String name, String url, String apiKey, int initialSleep, int totalAmountOfRequests, int pauseBetweenRequestsInMs) {
        super(name);
        this.url = url;
        this.apiKey = apiKey;
        this.initialSleep = initialSleep;
        this.totalAmountOfRequests = totalAmountOfRequests;
        this.pauseBetweenRequestsInMs = pauseBetweenRequestsInMs;
        this.statisticList = new ArrayList<>();
        this.random = new SecureRandom();
    }

    /**
     * Submits requests to the task app.
     */
    @Override
    public void run() {
        // Initial sleep
        try {
            LOG.debug("Thread {} is sleeping for {} ms", this.getName(), initialSleep);
            if (this.initialSleep > 0)
                Thread.sleep(this.initialSleep);
        } catch (InterruptedException e) {
            LOG.error("Thread {} was interrupted while sleeping", this.getName());
            return;
        }

        // Submit requests
        var random = new SecureRandom();
        for (int i = 0; i < totalAmountOfRequests; i++) {
            if (i % 10 == 0)
                LOG.info("Iteration {} of {} for thread {}", i, totalAmountOfRequests, this.getName());

            this.statisticList.add(random.nextBoolean() ? submitSyncRequest(i) : submitAsyncRequest(i));

            try {
                Thread.sleep(this.pauseBetweenRequestsInMs);
            } catch (InterruptedException ex) {
                LOG.warn("Thread {} was interrupted while sleeping", this.getName());
            }
        }

        LOG.info("Thread {} has finished", this.getName());
    }

    /**
     * Returns the list of request statistics.
     *
     * @return The list of request statistics.
     */
    public List<RequestStatistic> getStatisticList() {
        return Collections.unmodifiableList(statisticList);
    }

    /**
     * Prints the statistics of the thread.
     */
    public void printStatistics() {
        LOG.info("""
                ========================================
                Statistics for thread {}
                ========================================
                Total amount of requests:       {}
                Total amount of sync requests:  {}
                Total amount of async requests: {}
                Average response time for sync requests:  {} ms
                Average response time for async requests: {} ms
                Amount of failed sync requests:  {}
                Amount of failed async requests: {}
                ========================================""",
            this.getName(),
            this.totalAmountOfRequests,
            this.statisticList.stream().filter(s -> s.type().equals(STATISTIC_SYNC)).count(),
            this.statisticList.stream().filter(s -> s.type().equals(STATISTIC_ASYNC)).count(),
            this.statisticList.stream().filter(s -> s.type().equals(STATISTIC_SYNC)).mapToLong(RequestStatistic::duration).average().orElse(0),
            this.statisticList.stream().filter(s -> s.type().equals(STATISTIC_ASYNC)).mapToLong(RequestStatistic::duration).average().orElse(0),
            this.statisticList.stream().filter(s -> s.type().equals(STATISTIC_SYNC)).filter(RequestStatistic::failed).count(),
            this.statisticList.stream().filter(s -> s.type().equals(STATISTIC_ASYNC)).filter(RequestStatistic::failed).count());
    }

    //#region --- Submission Body ---

    /**
     * Builds the request body for the submit request.
     *
     * @param iteration The iteration number.
     * @return The request body.
     */
    protected String buildSubmissionRequestBody(int iteration) {
        long taskId = this.getTaskId(iteration);
        return new StringBuilder("{ ")
            .append("\"userId\": \"").append(this.getName()).append("\", ")
            .append("\"assignmentId\": \"").append(iteration).append("\", ")
            .append("\"taskId\": ").append(taskId).append(", ")
            .append("\"language\": ").append(this.random.nextBoolean() ? "\"de\"" : "\"en\"").append(", ")
            .append("\"mode\": \"").append(MODES[this.random.nextInt(MODES.length)]).append("\", ")
            .append("\"feedbackLevel\": ").append(this.random.nextInt(0, 4)).append(", ")
            .append("\"submission\": {")
            .append(this.buildAdditionalDataJson(iteration, taskId))
            .append("}}")
            .toString();
    }

    /**
     * Builds the type-specific submission data JSON for the submit request.
     *
     * @param iteration The iteration number.
     * @param taskId    The task identifier.
     * @return The additional data JSON.
     */
    protected abstract String buildAdditionalDataJson(int iteration, long taskId);

    /**
     * Returns the task ID for the given iteration.
     *
     * @param iteration The iteration number.
     * @return The task ID.
     */
    protected abstract long getTaskId(int iteration);

    //#endregion

    /**
     * Submits a synchronous request to the task app.
     *
     * @param iteration The iteration number.
     * @return The request statistic.
     */
    protected RequestStatistic submitSyncRequest(int iteration) {
        LOG.debug("Submitting sync request {} ...", iteration);
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder(URI.create(this.url + "/api/submission?runInBackground=false"))
                .header("X-API-KEY", this.apiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(this.buildSubmissionRequestBody(iteration)));

            final long start = System.currentTimeMillis();
            var response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
            final long end = System.currentTimeMillis();

            return new RequestStatistic(STATISTIC_SYNC, end - start, response.statusCode() != 200);
        } catch (IOException | InterruptedException ex) {
            LOG.error("An error occurred while submitting a synchronous submission", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Submits an asynchronous request to the task app.
     *
     * @param iteration The iteration number.
     * @return The request statistic.
     */
    protected RequestStatistic submitAsyncRequest(int iteration) {
        LOG.debug("Submitting async request {} ...", iteration);
        try (var client = HttpClient.newHttpClient()) {
            // Submit
            var request = HttpRequest.newBuilder(URI.create(this.url + "/api/submission?runInBackground=true&persist=true"))
                .header("X-API-KEY", this.apiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json,text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(this.buildSubmissionRequestBody(iteration)));

            final long start1 = System.currentTimeMillis();
            var response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
            final long end1 = System.currentTimeMillis();

            if (response.statusCode() != 202)
                return new RequestStatistic(STATISTIC_ASYNC, end1 - start1, false);

            // Get result
            var request2 = HttpRequest.newBuilder(URI.create(this.url + "/api/submission/" + response.body() + "/result"))
                .header("X-API-KEY", this.apiKey)
                .header("X-API-TIMEOUT", "30")
                .header("Accept", "application/json")
                .GET();

            final long start2 = System.currentTimeMillis();
            var response2 = client.send(request2.build(), HttpResponse.BodyHandlers.ofString());
            final long end2 = System.currentTimeMillis();

            return new RequestStatistic(STATISTIC_ASYNC, end1 - start1 + end2 - start2, response2.statusCode() != 200);
        } catch (IOException | InterruptedException ex) {
            LOG.error("An error occurred while submitting an asynchronous submission", ex);
            throw new RuntimeException(ex);
        }
    }
}
