package org.example.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;

import org.example.model.User;
import org.junit.jupiter.api.Test;

public class UserProcessorImplTest {
    private static final int EXPECTED_NUMBER_OF_USERS = 6;
    private static final int NUMBER_OF_THREADS = 4;
    private static final String TEST_JSON_FILE_PATH_1 = "src/test/resources/json_test/test1.json";
    private static final String TEST_JSON_FILE_PATH_2 = "src/test/resources/json_test/test2.json";


    @Test
    public void processFiles_whenMultipleFilesProvided_shouldReturnExpectedNumberOfUsers() throws Exception {
        // Given
        UserProcessorImpl userProcessorImpl = new UserProcessorImpl(NUMBER_OF_THREADS);
        File[] files = new File[]{
                new File(TEST_JSON_FILE_PATH_1),
                new File(TEST_JSON_FILE_PATH_2),
        };

        // When
        List<User> users = userProcessorImpl.processFiles(files);

        // Then
        assertEquals(EXPECTED_NUMBER_OF_USERS, users.size());
    }
}