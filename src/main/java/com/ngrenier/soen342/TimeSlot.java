package com.ngrenier.soen342;

import java.sql.Time;
import java.util.Objects;

enum DayOfWeek {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
}

public class TimeSlot {
    private int id;
    private DayOfWeek day;
    private Time startTime;
    private Time endTime;

    public TimeSlot(int id, DayOfWeek day, Time startTime, Time endTime) {
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TimeSlot timeSlot = (TimeSlot) obj;
        return day.toString().equals(timeSlot.day.toString()) &&
                startTime.toString().equals(timeSlot.startTime.toString()) &&
                endTime.toString().equals(timeSlot.endTime.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(day.toString(), startTime.toString(), endTime.toString());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
