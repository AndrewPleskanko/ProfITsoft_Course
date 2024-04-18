package org.example;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.example.args.CommandLineArgs;
import org.example.io.FileLoaderImpl;
import org.example.io.StatisticXmlFileWriterImpl;
import org.example.io.interfaces.FileLoader;
import org.example.io.interfaces.FileWriter;
import org.example.model.Item;
import org.example.model.PoisonPillUser;
import org.example.model.Statistics;
import org.example.model.User;
import org.example.publisher.UserPublisher;
import org.example.subscriber.UserSubscriber;
import org.example.util.CustomThreadFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) throws InterruptedException {

        CommandLineArgs commandLineArgs = new CommandLineArgs(args);
        String attributeName = commandLineArgs.getAttributeName();

        final FileLoader fileProcessor = new FileLoaderImpl();
        final File[] files = fileProcessor.loadFiles(commandLineArgs.getDirectoryPath());

        final int pubCount = commandLineArgs.getPublishersCount();
        final int subCount = commandLineArgs.getSubscribersCount();
        final int queueSize = commandLineArgs.getQueueSize();
        final int chunkSize = commandLineArgs.getQueueChunkSize();

        BlockingQueue<List<User>> queue = new ArrayBlockingQueue<>(queueSize);

        CountDownLatch fileLatch = new CountDownLatch(files.length);
        ExecutorService publishers = Executors.newFixedThreadPool(pubCount, new CustomThreadFactory("Pub"));

        for (File file : files) {
            publishers.submit(new UserPublisher(queue, file, fileLatch, chunkSize));
        }

        ExecutorService subscribers = Executors.newFixedThreadPool(subCount, new CustomThreadFactory("Sub"));
        CountDownLatch subLatch = new CountDownLatch(subCount);
        ConcurrentHashMap<String, Integer> stats = new ConcurrentHashMap<>();

        for (int i = 0; i < subCount; i++) {
            subscribers.submit(new UserSubscriber(queue, stats, subLatch, attributeName));
        }

        fileLatch.await();
        for (int i = 0; i < subCount; i++) {
            queue.put(new PoisonPillUser());
        }

        publishers.shutdown();
        if (!publishers.awaitTermination(1, TimeUnit.HOURS)) {
            log.error("Publishers did not terminate in the expected time");
        }

        subscribers.shutdown();
        subLatch.await();

        if (!subscribers.awaitTermination(1, TimeUnit.HOURS)) {
            log.error("Subscribers did not terminate in the expected time");
        }

        List<Item> items = stats.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(entry -> new Item(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        Statistics statistics = new Statistics(items);

        FileWriter writer = new StatisticXmlFileWriterImpl();
        try {
            writer.write(statistics, attributeName);
        } catch (IOException e) {
            log.error("Error writing statistics to XML file", e);
        }
    }
}


