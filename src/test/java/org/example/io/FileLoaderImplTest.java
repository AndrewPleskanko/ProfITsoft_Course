package org.example.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.example.exception.InvalidDirectoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileLoaderImplTest {
    private static final int EXPECTED_NUMBER_OF_FILES = 5;
    private static final String TEST_JSON_FILE_PATH = "src/test/resources/json_test";
    private static final String INVALID_DIRECTORY_PATH = "/path/does/not/exist";
    private static final String INVALID_JSON_FILE_PATH = "src/test/resources/invalid_json_test";
    private static final String EMPTY_JSON = "src/test/resources/empty/";
    private FileLoaderImpl fileLoaderImpl;

    @BeforeEach
    public void setUp() {
        fileLoaderImpl = new FileLoaderImpl();
    }

    @Test
    public void givenInvalidDirectory_whenProcessFiles_thenThrowsInvalidDirectoryException() {
        // When & Then
        assertThrows(InvalidDirectoryException.class, () -> fileLoaderImpl.loadFiles(INVALID_DIRECTORY_PATH));
    }

    @Test
    public void givenDirectoryWithJsonFiles_whenProcessFiles_thenReturnsFiles() {
        // When
        File[] files = fileLoaderImpl.loadFiles(TEST_JSON_FILE_PATH);
        List<String> fileNames = Arrays.stream(files).map(File::getName).toList();

        // Then
        assertEquals(EXPECTED_NUMBER_OF_FILES, files.length);
        assertTrue(fileNames.containsAll(Arrays.asList("invalid.json", "test1.json", "test2.json", "test3.json")));
    }

    @Test
    public void givenDirectoryWithNonJsonFiles_whenProcessFiles_thenReturnsNoFiles() {
        // Then
        assertThrows(InvalidDirectoryException.class, () -> fileLoaderImpl.loadFiles(EMPTY_JSON));
    }

    @Test
    public void givenDirectoryWithInvalidJsonFiles_whenProcessFiles_thenThrowsException() {
        // Then
        assertThrows(Exception.class, () -> fileLoaderImpl.loadFiles(INVALID_JSON_FILE_PATH));
    }
}
