package com.ngrenier.soen342.users;

import java.util.HashMap;

import com.ngrenier.soen342.City;

public class Instructor extends User {
    private String phoneNumber;
    private HashMap<Integer, Specialization> specializations;
    private HashMap<Integer, City> cities;

    public Instructor(int id, String name, String username, String password, String phoneNumber,
            HashMap<Integer, Specialization> specializations, HashMap<Integer, City> cities) {
        super(id, name, username, password);

        this.phoneNumber = phoneNumber;
        this.specializations = specializations;
        this.cities = cities;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Instructor instructor = (Instructor) obj;
        return getUsername() == instructor.getUsername();
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public HashMap<Integer, Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(HashMap<Integer, Specialization> specializations) {
        this.specializations = specializations;
    }

    public HashMap<Integer, City> getCities() {
        return cities;
    }

    public void setCities(HashMap<Integer, City> cities) {
        this.cities = cities;
    }
}
