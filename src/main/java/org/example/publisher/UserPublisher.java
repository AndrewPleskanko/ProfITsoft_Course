package org.example.publisher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.example.model.User;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The UserPublisher class is responsible for reading User objects from a JSON file,
 * and putting them into a BlockingQueue in chunks. It implements the Runnable interface,
 * allowing it to be executed in a multithreaded environment.
 */
@Slf4j
@AllArgsConstructor
public class UserPublisher implements Runnable {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BlockingQueue<List<User>> queue;
    private final File jsonFile;
    private final CountDownLatch latch;
    private final Integer chunkSize;

    /**
     * This method is invoked when the Runnable is executed.
     * It reads User objects from the JSON file, and puts them into the queue in chunks.
     */
    @Override
    public void run() {
        log.info("Starting parsing file: {}", jsonFile.getName());
        JsonFactory factory = new JsonFactory();
        try (JsonParser parser = factory.createParser(jsonFile)) {
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                log.error("Invalid format: Expected the JSON file to start with an array. File: {}",
                        jsonFile.getName());
                throw new IllegalStateException("Expected an array");
            }

            List<User> chunk = new ArrayList<>(chunkSize);
            while (parser.nextToken() == JsonToken.START_OBJECT) {
                User user = objectMapper.readValue(parser, User.class);
                chunk.add(user);

                if (chunk.size() == chunkSize) {
                    queue.put(new ArrayList<>(chunk));
                    log.debug("Added chunk of size {} to queue", chunkSize);
                    chunk.clear();
                }
            }

            if (!chunk.isEmpty()) {
                queue.put(new ArrayList<>(chunk));
            }
        } catch (Exception e) {
            log.error("Error processing file {}: {}", jsonFile.getName(), e.getMessage(), e);
        } finally {
            latch.countDown();
            log.info("Finished parsing file: {}", jsonFile.getName());
        }
    }
}