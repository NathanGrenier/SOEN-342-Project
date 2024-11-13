package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
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
                    schedule = new Schedule(scheduleId, startDate, endDate, new ArrayList<>());
                    schedules.put(scheduleId, schedule);
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
    }

    public void addSchedule(Schedule schedule) {
        schedules.put(schedule.getId(), schedule);
    }

    public Schedule getScheduleById(int id) {
        return schedules.get(id);
    }

    public Map<Integer, Schedule> getSchedules() {
        fetchAllSchedules();
        return schedules;
    }

    public void setSchedules(Map<Integer, Schedule> schedules) {
        this.schedules = schedules;
    }
}
