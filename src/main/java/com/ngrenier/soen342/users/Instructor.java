package com.ngrenier.soen342.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ngrenier.soen342.City;
import com.ngrenier.soen342.config.DatabaseConfig;

public class Instructor extends User {
    private String phoneNumber;
    private ArrayList<Specialization> specializations;
    private ArrayList<City> cities;

    public Instructor(int id, String name, String username, String password, String phoneNumber) {
        super(id, name, username, password);

        this.phoneNumber = phoneNumber;
        this.specializations = fetchSpecializations();
        this.cities = fetchCities();
    }

    private ArrayList<Specialization> fetchSpecializations() {
        ArrayList<Specialization> userSpecializations = new ArrayList<>();

        String sql = "SELECT s.S_ID, s.S_NAME FROM Specialization s " +
                "JOIN Instructor_Specialization i_s ON s.S_ID = i_s.S_ID " +
                "WHERE i_s.I_ID = ?";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, getId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int specializationId = rs.getInt("S_ID");
                String specializationName = rs.getString("S_NAME");
                userSpecializations.add(new Specialization(specializationId, specializationName));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userSpecializations;
    }

    private ArrayList<City> fetchCities() {
        ArrayList<City> userCities = new ArrayList<>();

        String sql = "SELECT ci.CI_ID, ci.CI_NAME, ci.CI_PROVINCE FROM City AS ci " +
                "JOIN Instructor_City AS i_c ON ci.CI_ID = i_c.CI_ID " +
                "WHERE i_c.I_ID = ?";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, getId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int cityId = rs.getInt("CI_ID");
                String cityName = rs.getString("CI_NAME");
                String cityProvince = rs.getString("CI_PROVINCE");
                userCities.add(new City(cityId, cityName, cityProvince));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userCities;
    }

    public static ArrayList<Instructor> fetchAllInstructors() {
        ArrayList<Instructor> instructors = new ArrayList<>();

        String sql = "SELECT * FROM Instructor";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("I_ID");
                String name = rs.getString("I_NAME");
                String username = rs.getString("I_USERNAME");
                String password = rs.getString("I_PASSWORD");
                String phoneNumber = rs.getString("I_PHONE");
                instructors.add(new Instructor(id, name, username, password, phoneNumber));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return instructors;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(ArrayList<Specialization> specializations) {
        this.specializations = specializations;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }
}
