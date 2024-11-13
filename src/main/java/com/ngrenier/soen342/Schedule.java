package com.ngrenier.soen342;

import java.sql.Date;
import java.util.List;

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
