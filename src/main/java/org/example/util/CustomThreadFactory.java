package org.example.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CustomThreadFactory is a custom implementation of the ThreadFactory interface.
 * It allows for customization of the created threads, such as setting a name prefix.
 */
@Slf4j
@AllArgsConstructor
public class CustomThreadFactory implements ThreadFactory {
    private final String prefix;
    private final AtomicInteger counter = new AtomicInteger(1);

    /**
     * Constructs a new Thread for a given Runnable object.
     * The name of the new thread will be a combination of the specified prefix and an incrementing counter.
     *
     * @param r the Runnable object to be executed by the new Thread
     * @return the newly created Thread
     */
    @Override
    public Thread newThread(Runnable r) {
        String threadName = prefix + "-" + counter.getAndIncrement();
        log.info("Creating new thread with name: {}", threadName);
        return new Thread(r, threadName);
    }
}