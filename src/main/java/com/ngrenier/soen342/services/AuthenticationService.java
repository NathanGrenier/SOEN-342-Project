package com.ngrenier.soen342.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.ngrenier.soen342.config.DatabaseConfig;
import com.ngrenier.soen342.users.Admin;
import com.ngrenier.soen342.users.Client;
import com.ngrenier.soen342.users.Instructor;
import com.ngrenier.soen342.users.User;

public class AuthenticationService {
    private static AuthenticationService instance = new AuthenticationService();

    private AuthenticationService() {
    }

    public static AuthenticationService getInstance() {
        return instance;
    }

    public User login(String username, String password, Map<Integer, Client> clients,
            Map<Integer, Instructor> instructors,
            Map<Integer, Admin> admins) {
        for (Client client : clients.values()) {
            if (client.getUsername().equals(username)) {
                if (client.getPassword().equals(password)) {
                    return client;
                } else {
                    throw new IllegalStateException("Incorrect password.");
                }
            }
        }

        for (Instructor instructor : instructors.values()) {
            if (instructor.getUsername().equals(username)) {
                if (instructor.getPassword().equals(password)) {
                    return instructor;
                } else {
                    throw new IllegalStateException("Incorrect password.");
                }
            }
        }

        for (Admin admin : admins.values()) {
            if (isAdminLoggedIn()) {
                throw new IllegalStateException("An admin is already logged in.");
            }
            if (admin.getUsername().equals(username)) {
                if (admin.getPassword().equals(password)) {
                    updateAdminSession(admin);
                    return admin;
                } else {
                    throw new IllegalStateException("Incorrect password.");
                }
            }
        }

        throw new IllegalStateException("User not found.");
    }

    public void logout(User currentUser) throws IllegalStateException {
        if (currentUser == null) {
            throw new IllegalStateException("No user is logged in.");
        }

        if (currentUser instanceof Admin) {
            try {
                deleteAdminSession((Admin) currentUser);
            } catch (IllegalStateException e) {
                throw new IllegalStateException("Error while deleting admin session.");
            }
        }
    }

    private boolean isAdminLoggedIn() {
        String sql = "SELECT * FROM Admin_Session";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void updateAdminSession(Admin admin) {
        String sql = "INSERT INTO Admin_Session (A_ID) VALUES (?)";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, admin.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteAdminSession(Admin admin) {
        String sql = "DELETE FROM Admin_Session WHERE A_ID = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, admin.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
