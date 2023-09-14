package com.joven.poller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Autowired
    private DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseHealthIndicator.class);

    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }

    @Override
    public Health health() {
        if (isDatabaseConnectionHealthy()) {
            logger.info("Success");
            return Health.up().build();
        } else {
            logger.error("Database connection is unhealthy");
            return Health.down().withDetail("Error", "Database connection failure").build();
        }
    }

    private boolean isDatabaseConnectionHealthy() {
        try (Connection connection = dataSource.getConnection()) {
            // If the connection is successful, return true
            return true;
        } catch (SQLException e) {
            // If an exception occurs, return false
            return false;
        }
    }
}
