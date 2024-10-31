package com.ngrenier.soen342;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ngrenier.soen342.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfigTest {

    private static Connection connection;

    @BeforeAll
    public static void setUp() throws SQLException {
        DatabaseConfig.migrateDatabase();
        connection = DatabaseConfig.getConnection();
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testDatabaseConnection() throws SQLException {
        assertNotNull(connection, "Database connection should not be null");

        assertTrue(connection.isValid(2), "Database connection should be valid");
    }

    @Test
    public void testFlywayMigrations() throws SQLException {
        // Verify that the "users" table exists and contains data
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            assertTrue(rs.next(), "Result set should have at least one row");
            int count = rs.getInt(1);
            assertTrue(count > 0, "Users table should contain at least one row");
        }
    }
}