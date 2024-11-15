package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
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

                String dayString = rs.getString("TS_DAY");
                if (dayString != null) {
                    DayOfWeek day = DayOfWeek.valueOf(dayString.toUpperCase());
                    Time startTime = rs.getTime("TS_START_TIME");
                    Time endTime = rs.getTime("TS_END_TIME");

                    if (!timeSlots.containsKey(timeSlotId)) {
                        TimeSlot newTimeSlot = new TimeSlot(timeSlotId, day, startTime, endTime);
                        timeSlots.put(timeSlotId, newTimeSlot);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching schedules: " + e.getMessage());
        }
    }

    public int createSchedule(Date startDate, Date endDate, List<String> timeSlots) {
        int[] timeSlotIds = createTimeSlots(timeSlots);

        String sql = "INSERT INTO Schedule (SC_START_DATE, SC_END_DATE) VALUES (?, ?) RETURNING SC_ID";

        int scheduleId = 0;
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                scheduleId = rs.getInt("SC_ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Set the timeslot's schedule ids to the new schedule's id
        String updateSql = "UPDATE time_slot SET SC_ID = ? WHERE TS_ID = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            for (int timeSlotId : timeSlotIds) {
                pstmt.setInt(1, scheduleId);
                pstmt.setInt(2, timeSlotId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return scheduleId;
    }

    public int[] createTimeSlots(List<String> timeSlots) {

        String sql = "INSERT INTO time_slot (TS_DAY, TS_START_TIME, TS_END_TIME, SC_ID) VALUES (?, ?, ?, ?) RETURNING TS_ID";

        int[] timeSlotIds = new int[timeSlots.size()];
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int index = 0;
                for (String timeSlot : timeSlots) {
                    String[] parts = timeSlot.split(",");
                    String day = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1).toLowerCase();
                    Time startTime = Time.valueOf(parts[1] + ":00");
                    Time endTime = Time.valueOf(parts[2] + ":00");

                    pstmt.setObject(1, day, java.sql.Types.OTHER);
                    pstmt.setTime(2, startTime);
                    pstmt.setTime(3, endTime);
                    pstmt.setNull(4, java.sql.Types.INTEGER);

                    // Execute each insert individually and get its returned ID
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        timeSlotIds[index++] = rs.getInt(1);
                    }
                    rs.close();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Error creating time slots: " + e.getMessage());
        }
        return timeSlotIds;
    }

    public void deleteSchedule(int scheduleId) {
        String sql = "DELETE FROM Schedule WHERE SC_ID = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, scheduleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        schedules.remove(scheduleId);
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
