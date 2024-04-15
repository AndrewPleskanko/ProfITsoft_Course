package org.example.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.example.model.User;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible for reading a JSON file and converting it into a list of User objects.
 */
@Slf4j
public class JsonFileReader implements Callable<List<User>> {
    private final File file;
    private final int chunkSize;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a new JsonFileReader with the specified file.
     *
     * @param file the file to read
     */

    public JsonFileReader(File file) {
        this(file, 10);
    }

    public JsonFileReader(File file, int chunkSize) {
        this.file = file;
        this.chunkSize = chunkSize;
    }

    /**
     * Reads the JSON file and converts it into a list of User objects.
     *
     * @return the list of User objects
     * @throws Exception if an error occurs during reading or conversion
     */

    @Override
    public List<User> call() throws Exception {
        log.info("Starting parsing file: {}", file.getName());

        List<User> users = new ArrayList<>();
        JsonFactory factory = new JsonFactory();


        try (JsonParser parser = factory.createParser(file)) {
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                log.error("Invalid format: Expected the JSON file to start with an array. File: {}", file.getName());
                throw new IllegalStateException("Expected an array");
            }

            List<User> chunk = new ArrayList<>(chunkSize);
            while (parser.nextToken() == JsonToken.START_OBJECT) {
                User user = objectMapper.readValue(parser, User.class);
                chunk.add(user);
                log.debug("Parsed user: {}", user);

                if (chunk.size() >= chunkSize) {
                    log.debug("Adding users from file {} to list", file);
                    users.addAll(chunk);
                    chunk.clear();
                }
            }

            if (!chunk.isEmpty()) {
                log.debug("Adding remaining users from file {} to list", file);
                users.addAll(chunk);
            }
        }

        log.info("Finished parsing file: {}", file.getName());
        return users;
    }
}