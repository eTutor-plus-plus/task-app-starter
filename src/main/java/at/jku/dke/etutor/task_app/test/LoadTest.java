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
import java.util.List;

/**
 * Application that can be used to run a load test on the task app.
 */
public abstract class LoadTest {
    /**
     * The logger.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(LoadTest.class);

    private final String url;
    private final String apiKey;

    /**
     * Creates a new instance of the load test.
     *
     * @param url    The URL of the task app (without trailing slash!).
     * @param apiKey The api key to use for the requests.
     */
    protected LoadTest(String url, String apiKey) {
        this.url = url;
        this.apiKey = apiKey;
    }

    /**
     * Runs the load test.
     *
     * @param totalAmountOfRequests       The total amount of requests to submit.
     * @param threadCount                 The amount of threads to use.
     * @param requestFactor               The factor to use for the calculation of the minimum and maximum amount of requests per thread
     *                                    (min = avg_amount_of_requests - (avg_amount_of_requests * factor)),
     *                                    (min = avg_amount_of_requests + (avg_amount_of_requests * factor)).
     * @param maxInitialSleep             The maximum initial sleep time in ms.
     *                                    The initial sleep time will be a random number between 0 and maxInitialSleep.
     * @param maxPauseBetweenRequestsInMs The maximum pause between requests in ms.
     *                                    The pause between requests will be a random number between 100 and maxPauseBetweenRequestsInMs.
     */
    public void run(final int totalAmountOfRequests, final int threadCount, final double requestFactor,
                    final int maxInitialSleep, final int maxPauseBetweenRequestsInMs) {
        // Create threads
        var threads = this.createThreads(totalAmountOfRequests, threadCount, requestFactor, maxInitialSleep, maxPauseBetweenRequestsInMs);

        try {
            // Create tasks
            this.createTasks();

            // Run threads
            for (var thread : threads) {
                LOG.info("Starting thread {}", thread.getName());
                thread.start();
            }

            // Wait for threads
            LOG.info("Waiting for threads to finish ...");
            for (var thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    LOG.error("Thread {} was interrupted while waiting for it to finish", thread.getName());
                }
            }
            LOG.info("All threads finished");
        } finally {
            this.deleteTasks();
        }

        // Print statistics
        threads.forEach(SubmissionThread::printStatistics);

        // Overall statistics
        var requests = threads.stream().map(SubmissionThread::getStatisticList).flatMap(List::stream).toList();
        var totalRequests = requests.size();
        var totalErrors = requests.stream().filter(RequestStatistic::failed).count();
        var avgSync = requests.stream().filter(s -> s.type().equals(SubmissionThread.STATISTIC_SYNC)).mapToLong(RequestStatistic::duration).average().orElse(0);
        var avgAsync = requests.stream().filter(s -> s.type().equals(SubmissionThread.STATISTIC_ASYNC)).mapToLong(RequestStatistic::duration).average().orElse(0);
        var avgDuration = requests.stream().mapToLong(RequestStatistic::duration).average().orElse(0);
        LOG.info("Overall statistics:");
        LOG.info("Total requests: {}", totalRequests);
        LOG.info("Total errors: {}", totalErrors);
        LOG.info("Average duration: {} ms", avgDuration);
        LOG.info("Average sync duration: {} ms", avgSync);
        LOG.info("Average async duration: {} ms", avgAsync);
    }

    /**
     * Creates the threads for the load test.
     * <p>
     * Randomly assigns the initial sleep time and the pause between requests as well as the total amount of requests.
     * The sum of the amount of requests of all threads will be equal to the total amount of requests.
     *
     * @param totalAmountOfRequests       The total amount of requests to submit.
     * @param threadCount                 The amount of threads to use.
     * @param requestFactor               The factor to use for the calculation of the minimum and maximum amount of requests per thread
     *                                    (min = avg_amount_of_requests - (avg_amount_of_requests * factor)),
     *                                    (min = avg_amount_of_requests + (avg_amount_of_requests * factor)).
     * @param maxInitialSleep             The maximum initial sleep time in ms.
     *                                    The initial sleep time will be a random number between 0 and maxInitialSleep.
     * @param maxPauseBetweenRequestsInMs The maximum pause between requests in ms.
     *                                    The pause between requests will be a random number between 100 and maxPauseBetweenRequestsInMs.
     * @return The threads.
     */
    protected final List<SubmissionThread> createThreads(final int totalAmountOfRequests, final int threadCount, final double requestFactor,
                                                         final int maxInitialSleep, final int maxPauseBetweenRequestsInMs) {
        if (totalAmountOfRequests < 1)
            throw new IllegalArgumentException("totalAmountOfRequests must be greater than 0");
        if (threadCount < 1)
            throw new IllegalArgumentException("threadCount must be greater than 0");
        if (requestFactor < 0 || requestFactor > 1)
            throw new IllegalArgumentException("requestFactor must be between 0 and 1");
        if (maxInitialSleep < 0)
            throw new IllegalArgumentException("maxInitialSleep must be greater than 0");
        if (maxPauseBetweenRequestsInMs < 100)
            throw new IllegalArgumentException("maxPauseBetweenRequestsInMs must be greater than 100");

        LOG.info("Initializing load test with {} threads", threadCount);
        var threads = new ArrayList<SubmissionThread>(threadCount);
        var random = new SecureRandom();
        var noOfAssignedRequests = 0;
        var avgAmountOfRequests = totalAmountOfRequests / threadCount;

        final int minAmountOfRequests = (int) (avgAmountOfRequests - (avgAmountOfRequests * requestFactor));
        final int maxAmountOfRequests = (int) (avgAmountOfRequests + (avgAmountOfRequests * requestFactor));

        for (int i = 0; i < threadCount; i++) {
            var initialSleep = random.nextInt(maxInitialSleep);
            var pauseBetweenRequestsInMs = random.nextInt(100, maxPauseBetweenRequestsInMs);
            var amountOfRequests = random.nextInt(minAmountOfRequests, maxAmountOfRequests);
            if (i == threadCount - 1)
                amountOfRequests = totalAmountOfRequests - noOfAssignedRequests;
            if (amountOfRequests <= 0) {
                LOG.warn("Thread {} has no requests to submit. Consider reducing requestFactor.", i);
                break;
            }

            noOfAssignedRequests += amountOfRequests;

            var thread = this.createThread("Submission_" + i, this.url, this.apiKey, initialSleep, amountOfRequests, pauseBetweenRequestsInMs);
            threads.add(thread);
        }
        return threads;
    }

    /**
     * Creates a new thread for the load test.
     *
     * @param name                     The name of the thread. This is used for logging.
     * @param url                      The URL of the task app. This should not contain a trailing slash.
     * @param apiKey                   The API key to use for the requests.
     * @param initialSleep             The initial sleep time in milliseconds.
     * @param totalAmountOfRequests    The total amount of requests to submit.
     * @param pauseBetweenRequestsInMs The pause between requests in milliseconds.
     * @return The thread.
     */
    protected abstract SubmissionThread createThread(String name, String url, String apiKey, int initialSleep, int totalAmountOfRequests, int pauseBetweenRequestsInMs);

    /**
     * Creates the tasks for the load test.
     */
    protected abstract void createTasks();

    /**
     * Deletes the tasks created for the load test.
     */
    protected abstract void deleteTasks();

    //#region --- Helper ---

    /**
     * Creates a task group.
     *
     * @param id   The id of the task group.
     * @param body The request body.
     */
    protected final void createTaskGroup(long id, String body) {
        LOG.info("Creating task group with id {}", id);
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder(URI.create(this.url + "/api/taskGroup/" + id))
                .header("X-API-KEY", this.apiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body));
            var response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201) {
                LOG.error("Error while creating task group with id " + id + ". Status code: " + response.statusCode());
                throw new RuntimeException("Error while creating task group with id " + id + ". Status code: " + response.statusCode() + ". Body: " + response.body());
            }
        } catch (IOException | InterruptedException ex) {
            LOG.error("Error while creating task group with id " + id, ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates a task.
     *
     * @param id   The id of the task.
     * @param body The request body.
     */
    protected final void createTask(long id, String body) {
        LOG.info("Creating task with id {}", id);
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder(URI.create(this.url + "/api/task/" + id))
                .header("X-API-KEY", this.apiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body));
            var response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201) {
                LOG.error("Error while creating task with id " + id + ". Status code: " + response.statusCode());
                throw new RuntimeException("Error while creating task with id " + id + ". Status code: " + response.statusCode() + ". Body: " + response.body());
            }
        } catch (IOException | InterruptedException ex) {
            LOG.error("Error while creating task with id " + id, ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates a task group.
     *
     * @param id The id of the task group.
     */
    protected final void deleteTaskGroup(long id) {
        LOG.info("Deleting task group with id {}", id);
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder(URI.create(this.url + "/api/taskGroup/" + id))
                .header("X-API-KEY", this.apiKey)
                .header("Accept", "application/json")
                .DELETE();
            var response = client.send(request.build(), HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() != 204) {
                LOG.error("Error while deleting task group with id " + id + ". Status code: " + response.statusCode());
                throw new RuntimeException("Error while deleting task group with id " + id + ". Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException ex) {
            LOG.error("Error while deleting task group with id " + id, ex);
            throw new RuntimeException(ex);
        }
    }

    //#endregion
}
