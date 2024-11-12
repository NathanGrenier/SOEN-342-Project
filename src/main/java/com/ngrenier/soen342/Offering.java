package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ngrenier.soen342.config.DatabaseConfig;
import com.ngrenier.soen342.users.Client;
import com.ngrenier.soen342.users.Instructor;

public class Offering {
    private int id;
    private String lesson;
    private int maxCapacity;
    private int currentCapacity;
    private boolean isPrivate;
    private Schedule schedule;
    private Location location;
    private Instructor instructor;

    public Offering(int id, String lesson, int maxCapacity, int currentCapacity, boolean isPrivate, Schedule schedule,
            Location location, Instructor instructor) {
        this.id = id;
        this.lesson = lesson;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.isPrivate = isPrivate;
        this.schedule = schedule;
        this.location = location;
        this.instructor = instructor;
    }

    public static List<Offering> fetchAllOfferings() {
        List<Offering> offerings = new ArrayList<>();

        List<Schedule> schedules = Schedule.fetchAllSchedules();
        List<Location> locations = Location.fetchAllLocations();

        String sql = "SELECT " +
                "o.O_ID, " +
                "o.O_LESSON, " +
                "o.O_MAX_CAPACITY, " +
                "o.O_CURRENT_CAPACITY, " +
                "o.O_IS_PRIVATE, " +
                "o.I_ID, " +
                "o.L_ID, " +
                "o.SC_ID " +
                "FROM offering o;";

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

                Instructor instructor = App.getUserById(Instructor.class, oInstructorId);
                Location location = locations.stream().filter(l -> l.getId() == oLocationId).findFirst().orElse(null);
                Schedule schedule = schedules.stream().filter(s -> s.getId() == oScheduleId).findFirst().orElse(null);

                Offering offering = new Offering(oId, oLesson, oMaxCapacity, oCurrentCapacity, oIsPrivate, schedule,
                        location, instructor);
                offerings.add(offering);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return offerings;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Offering offering = (Offering) obj;
        return id == offering.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
}
