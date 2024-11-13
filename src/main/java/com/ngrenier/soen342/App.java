package com.ngrenier.soen342;

import com.ngrenier.soen342.config.DatabaseConfig;
import com.ngrenier.soen342.services.AuthenticationService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;


public class App {
    public App() {
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

    }
    /* args contains choice of method and user type
     * args[0]: user type
     * args[1]: method
     * args[2+]: parameters (login info/object selection)
     */
    public String processInput(String[] args){
        return "";
    }
    

}

