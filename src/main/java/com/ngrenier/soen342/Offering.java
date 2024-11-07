package com.ngrenier.soen342;
import java.util.ArrayList;
import java.util.List;

public class Offering {

    private String type;
    private String instructor;
    private String location;
    private String schedule;

    // Static list to hold all offerings
    private static List<Offering> offerings = new ArrayList<>();
    //static method to get fetch offerings from DB
    public static void fetchOfferings(){

    }
    public Offering(String type, String instructor, String location, String schedule) {
        this.type = type;
        this.instructor = instructor;
        this.location = location;
        this.schedule = schedule;
        offerings.add(this);
    }

    
}

