package org.example.args;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CommandLineArgsTest {
    private static final String DIRECTORY_PATH = "directoryPath";
    private static final String ATTRIBUTE_NAME = "attributeName";
    private static final int PUBLISHERS_COUNT = 2;
    private static final int SUBSCRIBERS_COUNT = 1;
    private static final int QUEUE_CHUNK_SIZE = 1000;
    private static final int QUEUE_SIZE = 100;

    @Test
    public void givenValidArgs_whenNewCommandLineArgs_thenCorrectAttributes() {
        // Given
        String[] args = {"-p", "directoryPath", "-a", "attributeName", "-pubs", "2"};

        // When
        CommandLineArgs commandLineArgs = new CommandLineArgs(args);

        // Then
        assertEquals(DIRECTORY_PATH, commandLineArgs.getDirectoryPath());
        assertEquals(ATTRIBUTE_NAME, commandLineArgs.getAttributeName());
        assertEquals(PUBLISHERS_COUNT, commandLineArgs.getPublishersCount());
        assertEquals(SUBSCRIBERS_COUNT, commandLineArgs.getSubscribersCount());
        assertEquals(QUEUE_CHUNK_SIZE, commandLineArgs.getQueueChunkSize());
        assertEquals(QUEUE_SIZE, commandLineArgs.getQueueSize());
    }

    @Test
    public void givenMissingPathArg_whenNewCommandLineArgs_thenThrowsIllegalArgumentException() {
        // Given
        String[] args = {"-a", "attributeName", "-tc", "2"};

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new CommandLineArgs(args));
    }

    @Test
    public void givenMissingAttributeArg_whenNewCommandLineArgs_thenThrowsIllegalArgumentException() {
        // Given
        String[] args = {"-p", "directoryPath", "-tc", "2"};

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new CommandLineArgs(args));
    }

    @Test
    public void givenMissingThreadCountArg_whenNewCommandLineArgs_thenDefaultThreadCountIsOne() {
        // Given
        String[] args = {"-p", "directoryPath", "-a", "attributeName"};

        // When
        CommandLineArgs commandLineArgs = new CommandLineArgs(args);

        // Then
        assertEquals(SUBSCRIBERS_COUNT, commandLineArgs.getSubscribersCount());
    }

    @Test
    public void givenInvalidThreadCount_whenNewCommandLineArgs_thenThrowsNumberFormatException() {
        // Given
        String[] args = {"-p", "directoryPath", "-a", "attributeName", "-pubs", "invalid"};

        // When & Then
        assertThrows(NumberFormatException.class, () -> new CommandLineArgs(args));
    }

    @Test
    public void givenSingleArg_whenNewCommandLineArgs_thenThrowsIllegalArgumentException() {
        // Given
        String[] args = {};

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new CommandLineArgs(args));
    }
}