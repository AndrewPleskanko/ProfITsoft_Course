package org.example.args;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible for handling command line arguments.
 * It parses the command line arguments and stores the values of the specified options.
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class CommandLineArgs {
    private String directoryPath;
    private String attributeName;
    private int threadCount;

    /**
     * Constructor that parses the command line arguments.
     * @param args The command line arguments.
     * @throws IllegalArgumentException If the command line arguments are invalid.
     */
    public CommandLineArgs(String[] args) {
        Options options = createOptions();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            log.error("Error: Invalid command line arguments", e);
            formatter.printHelp("utility-name", options);
            throw new IllegalArgumentException("Invalid command line arguments", e);
        }

        directoryPath = cmd.getOptionValue("path");
        attributeName = cmd.getOptionValue("attribute");
        threadCount = Integer.parseInt(cmd.getOptionValue("threadCount", "1"));
    }

    /**
     * Creates and returns the command line options.
     * @return The command line options.
     */
    private Options createOptions() {
        Options options = new Options();

        Option pathOption = new Option("p", "path", true, "directory path");
        pathOption.setRequired(true);
        options.addOption(pathOption);

        Option attributeOption = new Option("a", "attribute", true, "attribute name");
        attributeOption.setRequired(true);
        options.addOption(attributeOption);

        Option threadCountOption = new Option("tc", "threadCount", true, "thread count");
        threadCountOption.setRequired(false);
        options.addOption(threadCountOption);

        return options;
    }
}