# Setup and Running the Application

Before running the application, you need to build it using Maven. This can be done by executing the following command in
the root directory of your project:

```
mvn package
```

This command will compile your code, run any tests, and package the compiled code into a JAR file in the `target`
directory.

After successfully building the application, you can run it using the following command:

```
java -jar target\custom-name.jar -p src/main/resources/json -a categories -tc 4
```

where `src/main/resources/json` is the path to the directory with JSON files and `role` is the name of the attribute for
which you need to calculate statistics.

## Main Entities

- `User`: An object that represents a user. It has attributes `username`, `email`, `password`,`role` and `categories`.
- `Role`: An Enum that represents the role of a user. It can be `ADMIN` or `USER`.
- `Item`: An object that represents a statistic item. It has attributes `value` or `count`.
- `Statistics`: An object that represents the statistics for a given attribute.
- `UserProcessor`: A class that processes user files. It uses a thread pool for parallel processing of files.
- `StatisticsCalculator`: A class that calculates statistics based on a list of users and an attribute name.
- `JsonFileReader`: A class that reads JSON files and returns a list of users.
- `XmlFileWriter`: A class that writes a `Statistics` object to an XML file.
- `FileProcessor`: This class is responsible for processing files in a given directory. It filters out all files that do
  not end with `.json`. If the directory is empty or does not exist, it throws an `InvalidDirectoryException`.
- `InvalidDirectoryException`: This is a custom exception class that extends `IllegalArgumentException`. It is thrown
  when a directory is empty or does not exist.
- `MainBenchmark`: This class is used for benchmarking the main method of the application. It uses the JMH (Java
  Microbenchmark Harness) framework for benchmarking. The `testMainMethod` function is annotated with `@Benchmark`,
  which indicates that it is the method to be benchmarked.
- `CommandLineArgs`: This class is responsible for handling command-line arguments. It expects at least two arguments:
  the directory path and the attribute name. If the number of provided arguments is less than two, it throws
  an `IllegalArgumentException`.
- `FakeDataGenerator`: class is used to generate fake user data and write it to a JSON file.

## Examples of Input and Output Files

- Input files: JSON files, each containing an array of User objects. Each User has attributes username, email, password,
  and role. For example:

```json
{
  "username": "JohnDoe123",
  "email": "johndoe123@example.com",
  "password": "password123",
  "role": "ADMIN",
  "categories": [
    "Music",
    "Sports",
    "Books"
  ]
}
```

- Output files: XML files that contain statistics for the specified attribute.For example:

```xml

<statistics>
    <item>
        <value>ADMIN</value>
        <count>1</count>
    </item>
    <item>
        <value>USER</value>
        <count>1</count>
    </item>
</statistics>
   ```

# Benchmarking Results (for 1.2gb of data)

Our application was tested using different numbers of threads. Below are the results of these tests.

## 1 Thread

With 1 thread, the average time per operation was 29.186 seconds. The error margin was ±4.627 seconds, meaning the
actual
time per operation could vary between 28.133 and 30.557 seconds.

## 2 Threads

With 2 threads, the average time per operation was 23.982 seconds. The error margin was ±1.110 seconds, meaning the
actual time per operation could vary between 23.659 and 24.329 seconds.

## 4 Threads

With 4 threads, the average time per operation was 23.024 seconds. The error margin was ±4.691 seconds, meaning the
actual time per operation could vary between 21.412 and 24.728 seconds.

## 8 Threads

With 8 threads, the average time per operation was 22.775 seconds. The error margin was ±2.724 seconds, meaning the
actual time per operation could vary between 21.916 and 23.766 seconds.

The benchmarking results indicate that the performance of the application improves slightly as the number of threads
increases from 1 to 2, 4, and 8.
However, the improvement is quite small, and the actual impact on performance may vary depending on other factors such
as the specific workload and the capabilities of the hardware.