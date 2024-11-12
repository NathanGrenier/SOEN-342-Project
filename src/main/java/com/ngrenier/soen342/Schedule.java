package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ngrenier.soen342.config.DatabaseConfig;

public class Schedule {
    private int id;
    private Date startDate;
    private Date endDate;
    List<TimeSlot> timeSlots;

    public Schedule(int id, Date startDate, Date endDate, List<TimeSlot> timeSlots) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeSlots = timeSlots;

    }

    public static List<Schedule> fetchAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        Map<Integer, Schedule> scheduleMap = new HashMap<>();

        String sql = "SELECT s.SC_ID, s.SC_START_DATE, s.SC_END_DATE, " +
                "ts.TS_ID, ts.TS_DAY, ts.TS_START_TIME, ts.TS_END_TIME " +
                "FROM Schedule s " +
                "LEFT JOIN Time_Slot ts ON s.SC_ID = ts.SC_ID " +
                "ORDER BY s.SC_ID, ts.TS_ID";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int scheduleId = rs.getInt("SC_ID");
                Date startDate = rs.getDate("SC_START_DATE");
                Date endDate = rs.getDate("SC_END_DATE");

                Schedule schedule = scheduleMap.get(scheduleId);
                if (schedule == null) {
                    schedule = new Schedule(scheduleId, startDate, endDate, new ArrayList<>());
                    scheduleMap.put(scheduleId, schedule);
                    schedules.add(schedule);
                }

                int timeSlotId = rs.getInt("TS_ID");
                if (timeSlotId > 0) {
                    DayOfWeek day = DayOfWeek.valueOf(rs.getString("TS_DAY").toUpperCase());
                    Time startTime = rs.getTime("TS_START_TIME");
                    Time endTime = rs.getTime("TS_END_TIME");
                    TimeSlot timeSlot = new TimeSlot(timeSlotId, day, startTime, endTime);
                    schedule.timeSlots.add(timeSlot);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return schedules;
    }

    public int getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
