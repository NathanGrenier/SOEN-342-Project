package com.ngrenier.soen342.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ngrenier.soen342.City;
import com.ngrenier.soen342.CityRecords;
import com.ngrenier.soen342.config.DatabaseConfig;
import com.ngrenier.soen342.users.Client;
import com.ngrenier.soen342.users.Instructor;
import com.ngrenier.soen342.users.InstructorRecords;
import com.ngrenier.soen342.users.Specialization;
import com.ngrenier.soen342.users.SpecializationRecords;

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

            if (clients.get(guardianId).getAge() < 18) {
                throw new IllegalStateException("Guardian must be at least 18 years old.");
            }
        }

        if (age < 18 && guardianId == null) {
            throw new IllegalStateException("Clients under 18 must have a guardian.");
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
                newClient = new Client(
                        clientId,
                        name,
                        username,
                        password,
                        age,
                        null,
                        null);

                if (guardianId != null) {
                    newClient.setGuardianId(guardianId);
                    newClient.setGuardian(clients.get(guardianId));
                }
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

    public boolean validateAge(int age) throws IllegalStateException {
        if (age < 0 || age > 100) {
            throw new IllegalStateException("Age must be between 1-100.");
        }
        return true;
    }

    public Instructor registerInstructor(String name, String username, String password, String phoneNumber,
            String[] specializationNames, String[] cityNames, InstructorRecords instructorRecords,
            CityRecords cityRecords, SpecializationRecords specializationRecords)
            throws IllegalStateException {

        Map<Integer, Specialization> specializations = specializationRecords.getSpecializations();
        Map<Integer, City> cities = cityRecords.getCities();

        ArrayList<Integer> newInstructorSpecializations = new ArrayList<>();
        ArrayList<Integer> newInstructorCities = new ArrayList<>();

        for (String specializationName : specializationNames) {
            if (specializationName.isEmpty()) {
                continue;
            }
            Specialization specialization = specializations.values().stream()
                    .filter(s -> s.getName().toLowerCase().equals(specializationName.toLowerCase())).findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "Specialization with the name of '" + specializationName + "' was not found."));

            newInstructorSpecializations.add(specialization.getId());
        }

        for (String cityName : cityNames) {
            if (cityName.isEmpty()) {
                continue;
            }
            City city = cities.values().stream().filter(c -> c.getName().toLowerCase().equals(cityName.toLowerCase()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "City with the name of '" + cityName + "' was not found."));

            newInstructorCities.add(city.getId());
        }

        String sql = "INSERT INTO Instructor (I_NAME, I_USERNAME, I_PASSWORD, I_PHONE) VALUES (?, ?, ?, ?) RETURNING I_ID";

        Instructor newInstructor = null;
        int instructorId = 0;
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, phoneNumber);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                instructorId = rs.getInt("I_ID");
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

        for (Integer specializationId : newInstructorSpecializations) {
            newInstructor.getSpecializations().put(specializationId, specializations.get(specializationId));
        }
        for (Integer cityId : newInstructorCities) {
            newInstructor.getCities().put(cityId, cities.get(cityId));
        }

        sql = "INSERT INTO Instructor_Specialization (I_ID, S_ID) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Integer specializationId : newInstructorSpecializations) {
                pstmt.setInt(1, instructorId);
                pstmt.setInt(2, specializationId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "INSERT INTO Instructor_City (I_ID, CI_ID) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Integer cityId : newInstructorCities) {
                pstmt.setInt(1, instructorId);
                pstmt.setInt(2, cityId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
