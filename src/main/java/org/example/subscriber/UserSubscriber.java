package org.example.subscriber;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.example.model.PoisonPillUser;
import org.example.model.User;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The UserSubscriber class is responsible for processing a list of users retrieved from a blocking queue.
 * This class implements the Runnable interface, allowing it to be executed in a multithreaded environment.
 */
@Slf4j
@AllArgsConstructor
public class UserSubscriber implements Runnable {
    private final BlockingQueue<List<User>> queue;
    private final ConcurrentHashMap<String, Integer> statistics;
    private final CountDownLatch latch;
    private final String attributeName;

    /**
     * The run() method carries out the main logic of processing users.
     * It continues to read users from the queue until it encounters a PoisonPillUser.
     * For each user, it processes the specified attribute and updates the statistics.
     */
    @Override
    public void run() {
        try {
            while (true) {
                List<User> users = queue.take();

                Map<String, Integer> tmpStats = new HashMap<>();
                if (users instanceof PoisonPillUser) {
                    log.info("Received poison pill, terminating...");
                    break;
                }

                users.forEach(user -> {
                    List<String> attributeValues = switch (attributeName) {
                        case "username" -> Collections.singletonList(user.getUsername());
                        case "email" -> Collections.singletonList(user.getEmail());
                        case "password" -> Collections.singletonList(user.getPassword());
                        case "role" -> Collections.singletonList(user.getRole().toString());
                        case "categories" -> Arrays.asList(user.getCategories().split(",\\s*"));
                        default -> {
                            log.warn("Unknown attribute name: {}", attributeName);
                            yield Collections.emptyList();
                        }
                    };
                    attributeValues.forEach(value -> tmpStats.merge(value, 1, Integer::sum));
                });
                tmpStats.forEach((attr, count) -> statistics.merge(attr, count, Integer::sum));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Subscriber interrupted", e);
        } finally {
            latch.countDown();
            log.info("Subscriber finished");
        }
    }
}

