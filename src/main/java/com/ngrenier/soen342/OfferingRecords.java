package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ngrenier.soen342.config.DatabaseConfig;
import com.ngrenier.soen342.users.Instructor;
import com.ngrenier.soen342.users.InstructorRecords;
import com.ngrenier.soen342.users.Specialization;

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
        Map<Integer, Location> locationMap = locations.getLocations();
        Map<Integer, Schedule> scheduleMap = schedules.getSchedules();
        Map<Integer, Instructor> instructorMap = instructors.getInstructors();

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

                Instructor instructor = instructorMap.get(oInstructorId);
                Location location = locationMap.get(oLocationId);
                Schedule schedule = scheduleMap.get(oScheduleId);

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
    public boolean addOffering(Offering offering) throws SQLException {
        for (Offering existingOffering : offerings.values()) {
            if (existingOffering.getLocation().getId() == offering.getLocation().getId()) {
                // Check if the time slots overlap
                if (existingOffering.getSchedule().getTimeSlots().equals(offering.getSchedule().getTimeSlots())) {
                    throw new SQLException("[TIME SLOT CONFLICT] The offering at conflicts without another.");
                }
            }
        }
        offerings.put(offering.getId(), offering);
        String sql = "INSERT INTO Offering (o_lesson, l_id, sc_id, o_max_capacity,o_current_capacity, o_is_private) VALUES (?, ?, ?, ?, 0, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
    
            // Set parameters for the SQL query
            pstmt.setString(1, offering.getLesson());
            pstmt.setInt(2, offering.getLocation().getId());
            pstmt.setInt(3, offering.getSchedule().getId());
            pstmt.setInt(4, offering.getMaxCapacity());
            pstmt.setBoolean(5, offering.isPrivate());
    
            // Execute the query
            int affectedRows = pstmt.executeUpdate();
    
            // Check if the offering was successfully inserted
            if (affectedRows == 0) {
                throw new SQLException("Creating offering failed, no rows affected.");
            }
    
            // Retrieve the generated ID for the new offering
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    offering.setId(generatedKeys.getInt(1)); // Set the ID for the offering
                    offerings.put(offering.getId(), offering); // Add to in-memory map
                    return true;
                } else {
                    throw new SQLException("Creating offering failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding offering: " + e.getMessage());
            return false;
        }
    }
    public Offering newOffering(String lesson, Location location, Schedule schedule, int capacity, boolean isPrivate){
        int lessonId = 0;
        Offering offering = new Offering(lessonId,lesson,capacity,0,isPrivate,schedule,location,null);
        return offering;
    }
    public Map<Integer, Offering> getOfferings() {
        fetchAllOfferings();
        return offerings;
    }

    public void setOfferings(Map<Integer, Offering> offerings) {
        this.offerings = offerings;
    }
    public void updateOfferingInstructor(Instructor instructor, int offeringId){
        String sql = "UPDATE Offering SET I_ID = ? WHERE O_ID = ?";
        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, instructor.getId());
            pstmt.setInt(2, offeringId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to update the offering in the database.");
        }
    }
    public void displayOfferings(Map<Integer,Offering> offerings) {
                offerings.values().stream().forEach(offering -> {
                    String offeringTitle = String.format(
                            "\n%s: %s%s%s at %s (%s) in %s, %s. Current Capacity: %d/%d",
                            offering.getId(),
                            offering.isPrivate() ? "Private " : "",
                            offering.getLesson(),
                            offering.getInstructor()!=null ? " taught by "+offering.getInstructor().getName() : "",
                            offering.getLocation().getFacility(),
                            offering.getLocation().getRoomName(),
                            offering.getLocation().getCity().getName(),
                            offering.getLocation().getCity().getProvince(),
                            offering.getCurrentCapacity(),
                            offering.getMaxCapacity());
                    System.out.println(offeringTitle);

                    Schedule.displaySchedule(offering.getSchedule());
                });
    }
}
