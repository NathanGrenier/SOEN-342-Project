package com.ngrenier.soen342;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ngrenier.soen342.config.DatabaseConfig;
import com.ngrenier.soen342.users.Instructor;
import com.ngrenier.soen342.users.InstructorRecords;

public class OfferingRecords {
    private Map<Integer, Offering> offerings = new HashMap<>();
    private LocationRecords locations = LocationRecords.getInstance();
    private ScheduleRecords schedules = ScheduleRecords.getInstance();
    private InstructorRecords instructors = InstructorRecords.getInstance();

    private static OfferingRecords instance = new OfferingRecords();

    private OfferingRecords() {
    }

    public static OfferingRecords getInstance() {
        return instance;
    }

    private void pruneDeletedOfferings(ArrayList<Integer> ids) {
        ArrayList<Integer> deletedOfferings = new ArrayList<>();

        for (Integer offeringId : offerings.keySet()) {
            if (!ids.contains(offeringId)) {
                deletedOfferings.add(offeringId);
            }
        }

        for (Integer deletedOffering : deletedOfferings) {
            offerings.remove(deletedOffering);
        }
    }

    public void fetchAllOfferings() {
        locations.fetchAllLocations();
        schedules.fetchAllSchedules();
        instructors.fetchAllInstructors();

        ArrayList<Integer> offeringIds = new ArrayList<>();

        String sql = "SELECT * FROM Offering";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int oId = rs.getInt("O_ID");
                String oLesson = rs.getString("O_LESSON");
                int oMaxCapacity = rs.getInt("O_MAX_CAPACITY");
                int oCurrentCapacity = rs.getInt("O_CURRENT_CAPACITY");
                boolean oIsPrivate = rs.getBoolean("O_IS_PRIVATE");
                int oInstructorId = rs.getInt("I_ID");
                int oLocationId = rs.getInt("L_ID");
                int oScheduleId = rs.getInt("SC_ID");

                offeringIds.add(oId);

                Instructor instructor = instructors.getInstructorById(oInstructorId);
                Location location = locations.getLocationById(oLocationId);
                Schedule schedule = schedules.getScheduleById(oScheduleId);

                if (!offerings.containsKey(oId)) {
                    Offering offering = new Offering(oId, oLesson, oMaxCapacity, oCurrentCapacity, oIsPrivate, schedule,
                            location, instructor);
                    offerings.put(oId, offering);
                } else {
                    Offering offering = offerings.get(oId);
                    offering.setLesson(oLesson);
                    offering.setMaxCapacity(oMaxCapacity);
                    offering.setCurrentCapacity(oCurrentCapacity);
                    offering.setPrivate(oIsPrivate);
                    offering.setInstructor(instructor);
                    offering.setLocation(location);
                    offering.setSchedule(schedule);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        pruneDeletedOfferings(offeringIds);
    }

    public void addOffering(Offering offering) {
        offerings.put(offering.getId(), offering);
    }

    public Offering getOfferingById(int id) {
        return offerings.get(id);
    }

    public Map<Integer, Offering> getOfferings() {
        fetchAllOfferings();
        return offerings;
    }

    public void setOfferings(Map<Integer, Offering> offerings) {
        this.offerings = offerings;
    }
}