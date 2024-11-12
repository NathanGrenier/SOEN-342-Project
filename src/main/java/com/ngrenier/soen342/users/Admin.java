package com.ngrenier.soen342.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ngrenier.soen342.config.DatabaseConfig;

public class Admin extends User {
    public Admin(int id, String name, String username, String password) {
        super(id, name, username, password);
    }

    public static ArrayList<Admin> fetchAllAdmins() {
        ArrayList<Admin> admins = new ArrayList<>();

        String sql = "SELECT * FROM Admin";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("A_ID");
                String name = rs.getString("A_NAME");
                String username = rs.getString("A_USERNAME");
                String password = rs.getString("A_PASSWORD");
                admins.add(new Admin(id, name, username, password));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return admins;
    }
}
