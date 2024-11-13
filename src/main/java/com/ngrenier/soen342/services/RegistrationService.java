package com.ngrenier.soen342.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ngrenier.soen342.City;
import com.ngrenier.soen342.config.DatabaseConfig;
import com.ngrenier.soen342.users.Client;
import com.ngrenier.soen342.users.Instructor;
import com.ngrenier.soen342.users.InstructorRecords;
import com.ngrenier.soen342.users.Specialization;

public class RegistrationService {
    private static RegistrationService instance = new RegistrationService();

    private RegistrationService() {
    }

    public static RegistrationService getInstance() {
        return instance;
    }

    public Client registerClient(String name, int age, String username, String password, String guardianUserName,
            Map<Integer, Client> clients) throws IllegalStateException {

        Integer guardianId = null;
        if (!guardianUserName.isEmpty()) {
            guardianId = clients.values().stream().filter(client -> client.getUsername().equals(guardianUserName))
                    .findFirst().map(Client::getId).orElseThrow(() -> new IllegalStateException(
                            "Guardian with the username of '" + guardianUserName + "' was not found."));
        }

        String sql = "INSERT INTO Client (C_NAME, C_USERNAME, C_PASSWORD, C_AGE, GUARDIAN_ID) VALUES (?, ?, ?, ?, ?) RETURNING C_ID";

        Client newClient = null;
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setInt(4, age);
            if (guardianId == null) {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(5, guardianId);
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int clientId = rs.getInt("C_ID");
                newClient = new Client(clientId, name, username, password, age, guardianId,
                        guardianId != null ? clients.get(guardianId) : null);
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState().equals("U0001"));
            if (e.getSQLState().equals("U0001")) {
                throw new IllegalStateException("Username already exists.");
            }
            System.out.println(e.getMessage());
        }
        return newClient;
    }

    public Instructor registerInstructor(String name, String username, String password, String phoneNumber,
            String[] specializationNames, String[] cityNames, InstructorRecords instructorRecords)
            throws IllegalStateException {

        Map<Integer, Specialization> specializations = instructorRecords.getSpecializationRecords()
                .getSpecializations();
        Map<Integer, City> cities = instructorRecords.getCityRecords().getCities();

        String sql = "INSERT INTO Instructor (I_NAME, I_USERNAME, I_PASSWORD, I_PHONE) VALUES (?, ?, ?, ?) RETURNING I_ID";

        Instructor newInstructor = null;
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, phoneNumber);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int instructorId = rs.getInt("I_ID");
                newInstructor = new Instructor(instructorId, name, username, password, phoneNumber, new HashMap<>(),
                        new HashMap<>());
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState().equals("U0001"));
            if (e.getSQLState().equals("U0001")) {
                throw new IllegalStateException("Username already exists.");
            }
            System.out.println(e.getMessage());
        }

        for (String specializationName : specializationNames) {
            Specialization specialization = specializations.values().stream()
                    .filter(s -> s.getName().equals(specializationName)).findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "Specialization with the name of '" + specializationName + "' was not found."));

            newInstructor.getSpecializations().put(specialization.getId(), specialization);
        }

        for (String cityName : cityNames) {
            City city = cities.values().stream().filter(c -> c.getName().equals(cityName)).findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "City with the name of '" + cityName + "' was not found."));
            newInstructor.getCities().put(city.getId(), city);
        }

        return newInstructor;
    }

    public boolean validatePhone(String phone) throws IllegalStateException {
        boolean isValid = phone.matches("^([0-9]{3})\\-([0-9]{3})\\-([0-9]{4})$");
        if (!isValid) {
            throw new IllegalStateException("Phone number must be in the format of '###-###-####'.");
        }
        return isValid;
    }
}
