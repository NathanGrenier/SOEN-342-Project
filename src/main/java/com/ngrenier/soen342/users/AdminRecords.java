package com.ngrenier.soen342.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ngrenier.soen342.config.DatabaseConfig;

public class AdminRecords {
    private Map<Integer, Admin> admins = new HashMap<>();

    private static AdminRecords instance = new AdminRecords();

    private AdminRecords() {
    }

    public static AdminRecords getInstance() {
        return instance;
    }

    private void pruneDeletedAdmins(ArrayList<Integer> ids) {
        ArrayList<Integer> deletedAdmins = new ArrayList<>();

        for (Integer adminId : admins.keySet()) {
            if (!ids.contains(adminId)) {
                deletedAdmins.add(adminId);
            }
        }

        for (Integer deletedAdmin : deletedAdmins) {
            admins.remove(deletedAdmin);
        }
    }

    public void fetchAllAdmins() {
        ArrayList<Integer> adminIds = new ArrayList<>();

        String sql = "SELECT * FROM Admin";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("A_ID");
                String name = rs.getString("A_NAME");
                String username = rs.getString("A_USERNAME");
                String password = rs.getString("A_PASSWORD");

                adminIds.add(id);

                if (!admins.containsKey(id)) {
                    admins.put(id, new Admin(id, name, username, password));
                } else {
                    admins.get(id).setName(name);
                    admins.get(id).setUsername(username);
                    admins.get(id).setPassword(password);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        pruneDeletedAdmins(adminIds);
    }

    public void addAdmin(Admin admin) {
        admins.put(admin.getId(), admin);
    }

    public Admin getAdminById(int id) {
        return admins.get(id);
    }

    public Map<Integer, Admin> getAdmins() {
        fetchAllAdmins();
        return admins;
    }

    public void setAdmins(Map<Integer, Admin> admins) {
        this.admins = admins;
    }
}
