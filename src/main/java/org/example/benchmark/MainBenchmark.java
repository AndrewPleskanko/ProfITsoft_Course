package org.example.benchmark;

import org.example.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * This class is used to benchmark the Main class.
 */
public class MainBenchmark {

    /**
     * This method is a JMH benchmark for the main method of the Main class.
     * It measures the average time it takes to run the main method with a specific set of arguments.
     *
     * @throws Exception if any error occurs during the execution of the main method.
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 5, batchSize = 1)
    public void testMainMethod() throws Exception {
        String[] args = {"-p", "src/main/resources/json", "-a", "categories", "-tc", "2"};
        Main.main(args);
    }

    /**
     * The main method to run the JMH benchmark.
     *
     * @param args the command line arguments.
     * @throws RunnerException if any error occurs during the benchmark.
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MainBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
