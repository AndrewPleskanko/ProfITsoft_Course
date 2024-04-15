package org.example.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.example.model.User;
import org.junit.jupiter.api.Test;

public class JsonFileReaderTest {
    private static final int EXPECTED_NUMBER_OF_USERS = 3;
    private static final String VALID_JSON_FILE_PATH = "src/test/resources/json_test/test1.json";
    private static final String INVALID_JSON_FILE_PATH = "src/test/resources/json_test/invalid.json";

    @Test
    public void call_whenFileExists_shouldReturnExpectedNumberOfUsers() throws Exception {
        // Given
        File file = new File(VALID_JSON_FILE_PATH);
        JsonFileReader jsonFileReader = new JsonFileReader(file);

        // When
        List<User> users = jsonFileReader.call();

        // Then
        assertEquals(EXPECTED_NUMBER_OF_USERS, users.size());
    }

    @Test
    public void call_whenInvalidJsonFile_shouldThrowException() {
        // Given
        File file = new File(INVALID_JSON_FILE_PATH);
        JsonFileReader jsonFileReader = new JsonFileReader(file);

        // When & Then
        assertThrows(IllegalStateException.class, jsonFileReader::call);
    }
}
