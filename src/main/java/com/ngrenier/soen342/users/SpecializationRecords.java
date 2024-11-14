package com.ngrenier.soen342.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ngrenier.soen342.config.DatabaseConfig;

public class SpecializationRecords {
    private Map<Integer, Specialization> specializations = new HashMap<>();

    private static SpecializationRecords instance = new SpecializationRecords();

    private SpecializationRecords() {
    }

    public static SpecializationRecords getInstance() {
        return instance;
    }

    public void fetchAllSpecializations() {
        String sql = "SELECT * FROM Specialization";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int sId = rs.getInt("S_ID");
                String sName = rs.getString("S_NAME");

                if (!specializations.containsKey(sId)) {
                    specializations.put(sId, new Specialization(sId, sName));
                } else {
                    specializations.get(sId).setName(sName);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Specialization createSpecialization(String name) {
        String sql = "INSERT INTO Specialization (S_NAME) VALUES (?) RETURNING S_ID";

        Specialization newSpecialization = null;
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int specializationId = rs.getInt("S_ID");
                newSpecialization = new Specialization(specializationId, name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return newSpecialization;
    }

    public void displaySpecializations() {
        fetchAllSpecializations();
        System.out.println("Specializations:");
        for (Specialization specialization : specializations.values()) {
            System.out.println("- " + specialization.getName());
        }
    }

    public void addSpecialization(Specialization specialization) {
        specializations.put(specialization.getId(), specialization);
    }

    public Map<Integer, Specialization> getSpecializations() {
        fetchAllSpecializations();
        return specializations;
    }

    public void setSpecializations(Map<Integer, Specialization> specializations) {
        this.specializations = specializations;
    }
}
