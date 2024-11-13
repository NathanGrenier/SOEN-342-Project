package com.ngrenier.soen342;

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
