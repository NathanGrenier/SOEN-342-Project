package com.ngrenier.soen342.users;

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

public class InstructorRecords {
    private Map<Integer, Instructor> instructors = new HashMap<>();
    private CityRecords cities = CityRecords.getInstance();
    private SpecializationRecords specializations = SpecializationRecords.getInstance();

    private static InstructorRecords instance = new InstructorRecords();

    private InstructorRecords() {
    }

    public static InstructorRecords getInstance() {
        return instance;
    }

    private void pruneDeletedInstructors(ArrayList<Integer> ids) {
        ArrayList<Integer> deletedInstructors = new ArrayList<>();

        for (Integer instructorId : instructors.keySet()) {
            if (!ids.contains(instructorId)) {
                deletedInstructors.add(instructorId);
            }
        }

        for (Integer deletedInstructor : deletedInstructors) {
            instructors.remove(deletedInstructor);
        }
    }

    public void fetchAllInstructors() {
        Map<Integer, City> cityMap = cities.getCities();
        Map<Integer, Specialization> specializationMap = specializations.getSpecializations();

        ArrayList<Integer> instructorIds = new ArrayList<>();

        String sql = """
                    SELECT
                        i.I_ID,
                        i.I_NAME,
                        i.I_USERNAME,
                        i.I_PASSWORD,
                        i.I_PHONE,
                        STRING_AGG(DISTINCT s.S_ID::text, ', ') AS I_specialization_ids,
                        STRING_AGG(DISTINCT c.CI_ID::text, ', ') AS I_city_ids
                    FROM
                        Instructor i
                    LEFT JOIN
                        Instructor_Specialization i_s ON i_s.I_ID = i.I_ID
                    LEFT JOIN
                        Specialization s ON s.S_ID = i_s.S_ID
                    LEFT JOIN
                        Instructor_City ic ON ic.I_ID = i.I_ID
                    LEFT JOIN
                        City c ON c.CI_ID = ic.CI_ID
                    GROUP BY
                        i.I_ID, i.I_NAME, i.I_USERNAME, i.I_PASSWORD, i.I_PHONE
                    ORDER BY
                        i.I_ID;
                """;

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int iId = rs.getInt("I_ID");
                String iName = rs.getString("I_NAME");
                String iUserName = rs.getString("I_USERNAME");
                String iPassword = rs.getString("I_PASSWORD");
                String iPhone = rs.getString("I_PHONE");
                String iSpecializationIds = rs.getString("I_SPECIALIZATION_IDS");
                String iCityIds = rs.getString("I_CITY_IDS");

                instructorIds.add(iId);

                HashMap<Integer, Specialization> userSpecializations = new HashMap<>();
                HashMap<Integer, City> userCities = new HashMap<>();

                for (String iSpecializationId : iSpecializationIds.split(", ")) {
                    Specialization specialization = specializationMap.get(Integer.parseInt(iSpecializationId));
                    userSpecializations.put(specialization.getId(), specialization);
                }

                for (String iCityId : iCityIds.split(", ")) {
                    City city = cityMap.get(Integer.parseInt(iCityId));
                    userCities.put(city.getId(), city);
                }

                if (!instructors.containsKey(iId)) {
                    instructors.put(iId, new Instructor(iId, iName, iUserName, iPassword, iPhone, userSpecializations,
                            userCities));
                } else {
                    Instructor instructor = instructors.get(iId);
                    instructor.setName(iName);
                    instructor.setUsername(iUserName);
                    instructor.setPassword(iPassword);
                    instructor.setPhoneNumber(iPhone);

                    instructor.setSpecializations(userSpecializations);
                    instructor.setCities(userCities);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        pruneDeletedInstructors(instructorIds);
    }

    public void addInstructor(Instructor instructor) {
        instructors.put(instructor.getId(), instructor);
    }

    public Map<Integer, Instructor> getInstructors() {
        fetchAllInstructors();
        return instructors;
    }

    public void setInstructors(Map<Integer, Instructor> instructors) {
        this.instructors = instructors;
    }

    public CityRecords getCityRecords() {
        return cities;
    }

    public void setCityRecords(CityRecords cities) {
        this.cities = cities;
    }

    public SpecializationRecords getSpecializationRecords() {
        return specializations;
    }

    public void setSpecializationRecords(SpecializationRecords specializations) {
        this.specializations = specializations;
    }
}
