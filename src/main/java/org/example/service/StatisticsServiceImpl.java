package org.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.model.Item;
import org.example.model.Statistics;
import org.example.model.User;
import org.example.service.interfaces.StatisticsService;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible for calculating statistics based on a list of users and an attribute name.
 */
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    /**
     * Calculates statistics based on a list of users and an attribute name.
     *
     * @param users         the list of users
     * @param attributeName the attribute name
     * @return the statistics
     */
    @Override
    public Statistics calculate(List<User> users, String attributeName) {
        log.info("Calculating statistics for attribute: {}", attributeName);
        Map<String, Integer> statistics = new HashMap<>();

        for (User user : users) {
            List<String> attributeValues = new ArrayList<>();
            switch (attributeName) {
                case "username" -> attributeValues.add(user.getUsername());
                case "email" -> attributeValues.add(user.getEmail());
                case "password" -> attributeValues.add(user.getPassword());
                case "role" -> attributeValues.add(user.getRole().toString());
                case "categories" -> attributeValues.addAll(user.getCategories());
                default -> {
                }
            }

            for (String attributeValue : attributeValues) {
                if (attributeValue != null) {
                    statistics.put(attributeValue, statistics.getOrDefault(attributeValue, 0) + 1);
                    log.debug("Updated count for {}: {}", attributeValue, statistics.get(attributeValue));
                }
            }
        }

        List<Item> items = statistics.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(entry -> new Item(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        log.info("Finished calculating statistics for attribute: {}", attributeName);
        return new Statistics(items);
    }
}