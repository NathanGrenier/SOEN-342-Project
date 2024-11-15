package com.ngrenier.soen342;

import java.sql.Date;
import java.util.Map;

public class Schedule {
    private int id;
    private Date startDate;
    private Date endDate;
    Map<Integer, TimeSlot> timeSlots;

    public Schedule(int id, Date startDate, Date endDate, Map<Integer, TimeSlot> timeSlots) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeSlots = timeSlots;
    }

    public static void displaySchedule(Schedule schedule) {
        String scheduleDates = String.format("From: %s to %s",
                schedule.getStartDate().toString(),
                schedule.getEndDate().toString());
        System.out.println(scheduleDates);

        printTimeSlotsTable(schedule.getTimeSlots());
    }

    public static void printTimeSlotsTable(Map<Integer, TimeSlot> timeSlots) {
        int maxLength = timeSlots.values().stream()
                .mapToInt(timeSlot -> timeSlot.getDay().toString().length())
                .max()
                .orElse(0);

        timeSlots.values().forEach(timeSlot -> {
            String day = timeSlot.getDay().toString();
            String startTime = timeSlot.getStartTime().toString().substring(0, 5);
            String endTime = timeSlot.getEndTime().toString().substring(0, 5);
            System.out.printf("- %-" + maxLength + "s: %s-%s%n", day, startTime, endTime);
        });
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Schedule schedule = (Schedule) obj;
        return startDate.equals(schedule.startDate) && endDate.equals(schedule.endDate)
                && timeSlots.equals(schedule.timeSlots);
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

    public Map<Integer, TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(Map<Integer, TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
