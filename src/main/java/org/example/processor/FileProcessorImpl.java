package org.example.processor;

import java.io.File;

import org.example.exception.InvalidDirectoryException;
import org.example.processor.interfaces.FileProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible for processing files in a specified directory.
 */
@Slf4j
public class FileProcessorImpl implements FileProcessor {

    /**
     * Processes files in the specified directory and returns an array of Files.
     * Only files with the ".json" extension are processed.
     *
     * @param directoryPath the path to the directory
     * @return an array of Files in the directory
     * @throws InvalidDirectoryException if the directory is empty or does not exist
     */
    @Override
    public File[] processFiles(String directoryPath) {
        log.info("Processing files in directory: {}", directoryPath);
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null || files.length == 0) {
            log.error("Directory is empty or does not exist: {}", directoryPath);
            throw new InvalidDirectoryException("Directory is empty or does not exist");
        }
        log.info("Processed {} files in directory: {}", files.length, directoryPath);
        return files;
    }
}
