package org.example.args;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CommandLineArgsTest {
    private static final String DIRECTORY_PATH = "directoryPath";
    private static final String ATTRIBUTE_NAME = "attributeName";

    @Test
    public void givenValidArgs_whenNewCommandLineArgs_thenCorrectAttributes() {
        // Given
        String[] args = {DIRECTORY_PATH, ATTRIBUTE_NAME};

        // When
        CommandLineArgs commandLineArgs = new CommandLineArgs(args);

        // Then
        assertEquals(DIRECTORY_PATH, commandLineArgs.getDirectoryPath());
        assertEquals(ATTRIBUTE_NAME, commandLineArgs.getAttributeName());
    }

    @Test
    public void givenSingleArg_whenNewCommandLineArgs_thenThrowsIllegalArgumentException() {
        // Given
        String[] args = {};

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new CommandLineArgs(args));
    }
}