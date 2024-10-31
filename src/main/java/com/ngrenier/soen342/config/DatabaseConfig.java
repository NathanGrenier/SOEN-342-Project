package com.ngrenier.soen342.config;

import org.flywaydb.core.Flyway;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties flywayProps = loadFlywayProperties();
    private static final String URL = flywayProps.getProperty("flyway.url");
    private static final String USER = flywayProps.getProperty("flyway.user");
    private static final String PASSWORD = flywayProps.getProperty("flyway.password");

    private static Properties loadFlywayProperties() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("flyway.conf")) {
            if (input == null) {
                throw new RuntimeException("Unable to find flyway.conf");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading flyway.conf", e);
        }
        return props;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void migrateDatabase() {
        Flyway flyway = Flyway.configure()
                .configuration(flywayProps)
                .load();
        flyway.migrate();
    }
}
