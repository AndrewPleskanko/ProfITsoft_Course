package org.example.args;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible for handling command line arguments.
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class CommandLineArgs {
    private static final int MIN_ARGS = 2;
    private String directoryPath;
    private String attributeName;

    /**
     * Constructs a new CommandLineArgs object with the specified arguments.
     *
     * @param args the command line arguments
     * @throws IllegalArgumentException if the number of arguments is less than the minimum required
     */
    public CommandLineArgs(String[] args) {
        if (args.length < MIN_ARGS) {
            log.error("Invalid number of arguments provided. Expected at least {} but got {}", MIN_ARGS, args.length);
            throw new IllegalArgumentException("Invalid number of arguments");
        }
        this.directoryPath = args[0];
        this.attributeName = args[1];
    }
}
