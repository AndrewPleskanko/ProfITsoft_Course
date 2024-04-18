package org.example.publisher;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserPublisherTest {
    private static final String VALID_JSON = "src/test/resources/json_test/test1.json";
    private static final String EMPTY_JSON = "src/test/resources/empty.json";

    private BlockingQueue<List<User>> queue;
    private CountDownLatch latch;
    private Integer chunkSize;

    @BeforeEach
    void setUp() {
        queue = new LinkedBlockingQueue<>();
        latch = new CountDownLatch(1);
        chunkSize = 10;
    }

    @Test
    void testValidJsonFile() {
        // Given
        File jsonFile = new File(VALID_JSON);

        // When
        UserPublisher publisher = new UserPublisher(queue, jsonFile, latch, chunkSize);
        publisher.run();

        // Then
        assertFalse(queue.isEmpty());
    }

    @Test
    void testEmptyJsonFile() {
        // Given
        File jsonFile = new File(EMPTY_JSON);
        // When

        UserPublisher publisher = new UserPublisher(queue, jsonFile, latch, chunkSize);
        publisher.run();

        // Then
        assertTrue(queue.isEmpty());
    }
}