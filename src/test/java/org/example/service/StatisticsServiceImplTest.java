package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.example.model.Item;
import org.example.model.Role;
import org.example.model.Statistics;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StatisticsServiceImplTest {
    private static final String CATEGORIES = "categories";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String ROLE = "role";
    private static final String EMAIL = "email";
    private static final String UNKNOWN = "unknown";

    private List<User> users;
    private StatisticsServiceImpl calculator;

    @BeforeEach
    public void setup() {
        // Given
        User user1 = createUser("user1", "user1@example.com",
                "password1", Role.ADMIN, Arrays.asList("category1", "category2"));
        User user2 = createUser("user2", "user2@example.com",
                "password2", Role.USER, Arrays.asList("category2", "category3", "category4"));
        User user3 = createUser("user3", "user3@example.com",
                "password3", Role.USER, Arrays.asList("category1", "category3"));

        users = Arrays.asList(user1, user2, user3);

        calculator = new StatisticsServiceImpl();
    }

    private User createUser(String username, String email, String password, Role role, List<String> categories) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setCategories(categories);
        return user;
    }

    @Test
    public void calculate_WithCategoriesAttribute_ReturnsCorrectStatistics() {

        // When
        Statistics result = calculator.calculate(users, CATEGORIES);

        List<Item> expectedItems = Arrays.asList(
                new Item("category1", 2),
                new Item("category2", 2),
                new Item("category3", 2),
                new Item("category4", 1)
        );

        // Then
        assertStatisticsEquals(expectedItems, result);
    }

    @Test
    public void calculate_WithPasswordAttribute_ReturnsCorrectStatistics() {

        // When
        Statistics result = calculator.calculate(users, PASSWORD);

        List<Item> expectedItems = Arrays.asList(
                new Item("password1", 1),
                new Item("password2", 1),
                new Item("password3", 1)
        );

        // Then
        assertStatisticsEquals(expectedItems, result);
    }

    @Test
    public void calculate_WithRoleAttribute_ReturnsCorrectStatistics() {

        // When
        Statistics result = calculator.calculate(users, ROLE);

        List<Item> expectedItems = Arrays.asList(
                new Item(Role.ADMIN.toString(), 1),
                new Item(Role.USER.toString(), 2)
        );

        // Then
        assertStatisticsEquals(expectedItems, result);
    }

    @Test
    public void calculate_WithUsernameAttribute_ReturnsCorrectStatistics() {

        // When
        Statistics result = calculator.calculate(users, USERNAME);

        List<Item> expectedItems = Arrays.asList(
                new Item("user1", 1),
                new Item("user2", 1),
                new Item("user3", 1)
        );

        // Then
        assertStatisticsEquals(expectedItems, result);
    }

    @Test
    public void calculate_WithEmailAttribute_ReturnsCorrectStatistics() {

        // When
        Statistics result = calculator.calculate(users, EMAIL);

        List<Item> expectedItems = Arrays.asList(
                new Item("user1@example.com", 1),
                new Item("user2@example.com", 1),
                new Item("user3@example.com", 1)
        );

        // Then
        assertStatisticsEquals(expectedItems, result);
    }

    @Test
    public void calculate_WithEmptyUsers_ReturnsEmptyStatistics() {
        // Given
        List<User> users = Collections.emptyList();

        StatisticsServiceImpl calculator = new StatisticsServiceImpl();

        // When
        Statistics result = calculator.calculate(users, EMAIL);

        // Then
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void calculate_WithUnknownAttribute_ReturnsEmptyStatistics() {

        // When
        Statistics result = calculator.calculate(users, UNKNOWN);

        // Then
        assertTrue(result.getItems().isEmpty());
    }

    private void assertStatisticsEquals(List<Item> expectedItems, Statistics actual) {
        Statistics expected = new Statistics(expectedItems);
        expected.getItems().sort(Comparator.comparing(Item::getValue));
        actual.getItems().sort(Comparator.comparing(Item::getValue));
        assertEquals(expected, actual);
    }
}