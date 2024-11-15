package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import com.ngrenier.soen342.config.DatabaseConfig;

public class ScheduleRecords {
    private Map<Integer, Schedule> schedules = new HashMap<>();

    private static ScheduleRecords instance = new ScheduleRecords();

    private ScheduleRecords() {
    }

    public static ScheduleRecords getInstance() {
        return instance;
    }

    public void fetchAllSchedules() {
        String sql = "SELECT s.SC_ID, s.SC_START_DATE, s.SC_END_DATE, " +
                "ts.TS_ID, ts.TS_DAY, ts.TS_START_TIME, ts.TS_END_TIME " +
                "FROM Schedule s " +
                "LEFT JOIN Time_Slot ts ON s.SC_ID = ts.SC_ID " +
                "ORDER BY s.SC_ID, ts.TS_DAY";
    
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int scheduleId = rs.getInt("SC_ID");
                Date startDate = rs.getDate("SC_START_DATE");
                Date endDate = rs.getDate("SC_END_DATE");
    
                Schedule schedule = schedules.get(scheduleId);
                if (schedule == null) {
                    schedule = new Schedule(scheduleId, startDate, endDate, new HashMap<>());
                    schedules.put(scheduleId, schedule);
                } else {
                    schedule.setStartDate(startDate);
                    schedule.setEndDate(endDate);
                }
    
                int timeSlotId = rs.getInt("TS_ID");
                Map<Integer, TimeSlot> timeSlots = schedule.getTimeSlots();
    
                // Handle potential null for "TS_DAY" column
                String dayString = rs.getString("TS_DAY");
                if (dayString != null) {
                    DayOfWeek day = DayOfWeek.valueOf(dayString.toUpperCase());
                    Time startTime = rs.getTime("TS_START_TIME");
                    Time endTime = rs.getTime("TS_END_TIME");
    
                    // Ensure timeSlots is not null before putting values
                    if (!timeSlots.containsKey(timeSlotId)) {
                        TimeSlot newTimeSlot = new TimeSlot(timeSlotId, day, startTime, endTime);
                        timeSlots.put(timeSlotId, newTimeSlot);
                    } else {
                        TimeSlot timeSlot = timeSlots.get(timeSlotId);
                        timeSlot.setDay(day);
                        timeSlot.setStartTime(startTime);
                        timeSlot.setEndTime(endTime);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching schedules: " + e.getMessage());
        }
    }
    
    public int saveScheduleToDatabase(Schedule schedule) {
        String insertScheduleSQL = "INSERT INTO Schedule (sc_start_date, sc_end_date) VALUES (?, ?) RETURNING sc_id";
        String insertTimeSlotSQL = "INSERT INTO Time_Slot (ts_day, ts_start_time, ts_end_time, sc_id) VALUES (?::day_of_week, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection()) {
            // Insert Schedule and retrieve generated Schedule ID
            int scheduleId;
            try (PreparedStatement pstmt = conn.prepareStatement(insertScheduleSQL)) {
                pstmt.setDate(1, schedule.getStartDate());
                pstmt.setDate(2, schedule.getEndDate());
                
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    scheduleId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated Schedule ID.");
                }
            }
    
            // Insert TimeSlots with capitalized day format
            try (PreparedStatement statement = conn.prepareStatement(insertTimeSlotSQL)) {
                for (TimeSlot slot : schedule.getTimeSlots().values()) {
                    // Capitalize the day name to match database enum format
                    String formattedDay = slot.getDay().name().substring(0, 1).toUpperCase() + slot.getDay().name().substring(1).toLowerCase();
                    
                    statement.setString(1, formattedDay); // E.g., "Monday", "Wednesday"
                    statement.setTime(2, slot.getStartTime());
                    statement.setTime(3, slot.getEndTime());
                    statement.setInt(4, scheduleId); // Set schedule ID for foreign key
                    
                    statement.executeUpdate();
                }
            }
    
            return scheduleId;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save schedule to database: " + e.getMessage(), e);
        }
    }
    
    
    
    public void addSchedule(Schedule schedule) {
        schedules.put(schedule.getId(), schedule);
    }

    public Map<Integer, Schedule> getSchedules() {
        fetchAllSchedules();
        return schedules;
    }

    public void setSchedules(Map<Integer, Schedule> schedules) {
        this.schedules = schedules;
    }
}
