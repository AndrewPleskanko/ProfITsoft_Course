package org.example.args;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CommandLineArgsTest {
    @Test
    public void givenValidArgs_whenNewCommandLineArgs_thenCorrectAttributes() {
        // Given
        String[] args = {"-p", "directoryPath", "-a", "attributeName", "-tc", "2"};

        // When
        CommandLineArgs commandLineArgs = new CommandLineArgs(args);

        // Then
        assertEquals("directoryPath", commandLineArgs.getDirectoryPath());
        assertEquals("attributeName", commandLineArgs.getAttributeName());
        assertEquals(2, commandLineArgs.getThreadCount());
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
        assertEquals(1, commandLineArgs.getThreadCount());
    }

    @Test
    public void givenInvalidThreadCount_whenNewCommandLineArgs_thenThrowsNumberFormatException() {
        // Given
        String[] args = {"-p", "directoryPath", "-a", "attributeName", "-tc", "invalid"};

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