package org.example;

import java.io.File;
import java.util.List;

import org.example.args.CommandLineArgs;
import org.example.io.FileWriterImpl;
import org.example.model.Statistics;
import org.example.model.User;
import org.example.processor.FileProcessorImpl;
import org.example.processor.UserProcessorImpl;
import org.example.service.StatisticsServiceImpl;

public class Main {
    private static final int THREAD_COUNT = 8;

    public static void main(String[] args) throws Exception {
        CommandLineArgs commandLineArgs = new CommandLineArgs(args);

        FileProcessorImpl fileProcessorImpl = new FileProcessorImpl();
        File[] files = fileProcessorImpl.processFiles(commandLineArgs.getDirectoryPath());

        UserProcessorImpl userProcessorImpl = new UserProcessorImpl(THREAD_COUNT);
        List<User> users = userProcessorImpl.processFiles(files);

        StatisticsServiceImpl statisticsServiceImpl = new StatisticsServiceImpl();
        Statistics statistics = statisticsServiceImpl.calculate(users, commandLineArgs.getAttributeName());

        FileWriterImpl xmlFileWriterImpl = new FileWriterImpl();
        xmlFileWriterImpl.write(statistics, commandLineArgs.getAttributeName());
    }
}