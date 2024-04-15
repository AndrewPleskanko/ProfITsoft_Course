package org.example.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.example.exception.InvalidDirectoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileProcessorImplTest {
    private static final int EXPECTED_NUMBER_OF_FILES = 4;
    private static final String TEST_JSON_FILE_PATH = "src/test/resources/json_test";
    private static final String INVALID_DIRECTORY_PATH = "/path/does/not/exist";
    private FileProcessorImpl fileProcessorImpl;

    @BeforeEach
    public void setUp() {
        fileProcessorImpl = new FileProcessorImpl();
    }

    @Test
    public void givenInvalidDirectory_whenProcessFiles_thenThrowsInvalidDirectoryException() {
        // When & Then
        assertThrows(InvalidDirectoryException.class, () -> fileProcessorImpl.processFiles(INVALID_DIRECTORY_PATH));
    }

    @Test
    public void givenDirectoryWithJsonFiles_whenProcessFiles_thenReturnsFiles() {
        // When
        File[] files = fileProcessorImpl.processFiles(TEST_JSON_FILE_PATH);

        // Then
        assertEquals(EXPECTED_NUMBER_OF_FILES, files.length);
        List<String> fileNames = Arrays.stream(files).map(File::getName).toList();
        assertTrue(fileNames.containsAll(Arrays.asList("invalid.json", "test1.json", "test2.json", "test3.json")));
    }
}
