package org.example.service.interfaces;

import java.util.List;

import org.example.model.Statistics;
import org.example.model.User;

public interface StatisticsService {
    Statistics calculate(List<User> users, String attributeName);
}
