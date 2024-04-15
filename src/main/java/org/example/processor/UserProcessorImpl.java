package org.example.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.example.io.JsonFileReader;
import org.example.model.User;
import org.example.processor.interfaces.UserProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible for processing a list of files using multiple threads.
 */
@Slf4j
@RequiredArgsConstructor
public class UserProcessorImpl implements UserProcessor {
    private final int threadCount;

    /**
     * Processes a list of files using multiple threads.
     *
     * @param files the files to process
     * @return the list of User objects
     * @throws Exception if an error occurs during processing
     */
    @Override
    public List<User> processFiles(File[] files) throws Exception {
        log.info("Starting processing of {} files with {} threads", files.length, threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<User> users = Collections.synchronizedList(new ArrayList<>());

        List<Future<?>> futures = new ArrayList<>();
        for (File file : files) {
            JsonFileReader jsonFileReader = new JsonFileReader(file);
            Future<?> future = executorService.submit(() -> {
                try {
                    List<User> usersFromFile = jsonFileReader.call();
                    users.addAll(usersFromFile);
                } catch (Exception e) {
                    log.error("Error processing file {}", file, e);
                }
            });
            futures.add(future);
        }

        for (Future<?> future : futures) {
            future.get();
        }

        executorService.shutdown();
        log.info("Finished processing files. Total users processed: {}", users.size());
        return users;
    }
}

