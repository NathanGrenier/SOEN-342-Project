package com.ngrenier.soen342;

import com.ngrenier.soen342.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {
        try {
            DatabaseConfig.migrateDatabase();

            // Test connection
            try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT version()");
                if (rs.next()) {
                    System.out.println("\nConnected to PostgreSQL: " + rs.getString(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection conn = DatabaseConfig.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println("User ID: " + rs.getInt("id") + ", Username: " + rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //initialize ui
        Ui ui = new Ui();
        ui.run();

    }
}
