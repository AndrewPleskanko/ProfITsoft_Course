package org.example.scripts;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.IntStream;

import org.example.model.Role;
import org.example.model.User;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to generate fake user data and write it to a JSON file.
 */
@Slf4j
public class FakeDataGenerator {

    private static final List<String> CATEGORIES = Arrays.asList("rock", "pop", "jazz", "football", "basketball",
            "tennis", "drawing", "guitar", "cooking", "AI", "blockchain", "VR", "painting", "sculpture",
            "photography", "physics", "biology", "astronomy", "mountains", "beaches", "cities", "board games",
            "video games", "sports games", "yoga", "running", "swimming", "dogs", "cats", "birds");

    /**
     * The main method that generates fake user data and writes it to a JSON file.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Faker faker = new Faker();
        Random random = new Random();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter("usersList10.json")) {
            writer.write("[");
            IntStream.range(0, 600_000).forEach(i -> {
                String username = faker.name().fullName().replace(" ", "").toLowerCase(Locale.ROOT);

                Collections.shuffle(CATEGORIES);
                List<String> userCategories = CATEGORIES.subList(0, 4);

                User user = new User(
                        username,
                        faker.internet().emailAddress(),
                        faker.internet().password(),
                        random.nextBoolean() ? Role.ADMIN : Role.USER,
                        userCategories
                );
                String json = gson.toJson(user);
                try {
                    writer.write(json);
                    if (i < 600_000 - 1) {
                        writer.write(",");
                    }
                } catch (IOException e) {
                    log.error("Error writing to file", e);
                }
            });
            writer.write("]");
        } catch (IOException e) {
            log.error("Error opening file", e);
        }
    }
}