package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ngrenier.soen342.config.DatabaseConfig;

public class Location {
    private int id;
    private String facility;
    private String roomName;
    private String type;
    private City city;

    public Location(int id, String facility, String roomName, String type, City city) {
        this.id = id;
        this.facility = facility;
        this.roomName = roomName;
        this.type = type;
        this.city = city;
    }

    public static List<Location> fetchAllLocations() {
        List<Location> locations = new ArrayList<>();

        String sql = "SELECT " +
                "l.L_ID, " +
                "l.L_FACILITY, " +
                "l.L_ROOM_NAME, " +
                "lt.LT_NAME, " +
                "c.CI_ID, " +
                "c.CI_NAME, " +
                "c.CI_PROVINCE " +
                "FROM location l " +
                "LEFT JOIN location_type lt ON l.LT_ID = lt.LT_ID " +
                "LEFT JOIN city c ON c.CI_ID = l.CI_ID " +
                "ORDER BY l.L_ID";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int locationId = rs.getInt("L_ID");
                String locationFacility = rs.getString("L_FACILITY");
                String locationRoomName = rs.getString("L_ROOM_NAME");
                String locationType = rs.getString("LT_NAME");
                int cityId = rs.getInt("CI_ID");
                String cityName = rs.getString("CI_NAME");
                String cityProvince = rs.getString("CI_PROVINCE");

                City city = new City(cityId, cityName, cityProvince);
                Location location = new Location(locationId, locationFacility, locationRoomName, locationType, city);
                locations.add(location);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return locations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
