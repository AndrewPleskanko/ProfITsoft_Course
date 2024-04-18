package org.example.subscriber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import org.example.model.PoisonPillUser;
import org.example.model.Role;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserSubscriberTest {
    private BlockingQueue<List<User>> queue;
    private ConcurrentHashMap<String, Integer> statistics;
    private CountDownLatch latch;
    private String attributeName;
    private static final String CATEGORIES = "categories";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String ROLE = "role";
    private static final String EMAIL = "email";
    private static final String UNKNOWN = "unknown";
    private List<User> users;

    @BeforeEach
    public void setup() {
        // Given
        User user1 = createUser("user1", "user1@example.com",
                "password1", Role.ADMIN, "category1,category2");
        User user2 = createUser("user2", "user2@example.com",
                "password2", Role.USER, "category2,category3,category4");
        User user3 = createUser("user3", "user3@example.com",
                "password3", Role.USER, "category1,category3");

        users = Arrays.asList(user1, user2, user3);

    }

    private User createUser(String username, String email, String password, Role role, String categories) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setCategories(categories);
        return user;
    }

    @BeforeEach
    void setUp() {
        // Given
        queue = new LinkedBlockingQueue<>();
        statistics = new ConcurrentHashMap<>();
        latch = new CountDownLatch(1);
        attributeName = "username";
    }

    @Test
    void testUsernameAttribute() {
        // Given
        attributeName = USERNAME;
        queue.add(users);
        queue.add(new PoisonPillUser());

        // When
        UserSubscriber subscriber = new UserSubscriber(queue, statistics, latch, attributeName);
        subscriber.run();

        // Then
        assertEquals(1, statistics.get("user1"));
        assertEquals(1, statistics.get("user2"));
        assertEquals(1, statistics.get("user3"));
    }

    @Test
    void testEmailAttribute() {
        // Given
        attributeName = EMAIL;
        queue.add(users);
        queue.add(new PoisonPillUser());

        // When
        UserSubscriber subscriber = new UserSubscriber(queue, statistics, latch, attributeName);
        subscriber.run();

        // Then
        assertEquals(1, statistics.get("user1@example.com"));
        assertEquals(1, statistics.get("user2@example.com"));
        assertEquals(1, statistics.get("user3@example.com"));
    }

    @Test
    void testPasswordAttribute() {
        // Given
        attributeName = PASSWORD;
        queue.add(users);
        queue.add(new PoisonPillUser());

        // When
        UserSubscriber subscriber = new UserSubscriber(queue, statistics, latch, attributeName);
        subscriber.run();

        // Then
        assertEquals(1, statistics.get("password1"));
        assertEquals(1, statistics.get("password2"));
        assertEquals(1, statistics.get("password3"));
    }

    @Test
    void testRoleAttribute() {
        // Given
        attributeName = ROLE;
        queue.add(users);
        queue.add(new PoisonPillUser());

        // When
        UserSubscriber subscriber = new UserSubscriber(queue, statistics, latch, attributeName);
        subscriber.run();

        // Then
        assertEquals(2, statistics.get(Role.USER.toString()));
        assertEquals(1, statistics.get(Role.ADMIN.toString()));
    }

    @Test
    void testCategoriesAttribute() {
        // Given
        attributeName = CATEGORIES;
        queue.add(users);
        queue.add(new PoisonPillUser());

        // When
        UserSubscriber subscriber = new UserSubscriber(queue, statistics, latch, attributeName);
        subscriber.run();

        // Then
        assertEquals(2, statistics.get("category1"));
        assertEquals(2, statistics.get("category2"));
        assertEquals(2, statistics.get("category3"));
        assertEquals(1, statistics.get("category4"));
    }

    @Test
    void testUnknownAttribute() {
        // Given
        attributeName = UNKNOWN;
        queue.add(users);
        queue.add(new PoisonPillUser());

        // When
        UserSubscriber subscriber = new UserSubscriber(queue, statistics, latch, attributeName);
        subscriber.run();

        // Then
        assertTrue(statistics.isEmpty());
    }
}