package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ngrenier.soen342.config.DatabaseConfig;

public class LocationRecords {
    private Map<Integer, Location> locations = new HashMap<>();
    private CityRecords cities = CityRecords.getInstance();

    private static LocationRecords instance = new LocationRecords();

    private LocationRecords() {
    }

    public static LocationRecords getInstance() {
        return instance;
    }

    public void fetchAllLocations() {
        Map<Integer, City> cityMap = cities.getCities();

        String sql = "SELECT * FROM Location";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int locationId = rs.getInt("L_ID");
                String locationFacility = rs.getString("L_FACILITY");
                String locationRoomName = rs.getString("L_ROOM_NAME");
                String locationType = rs.getString("L_TYPE");
                int locationCityId = rs.getInt("CI_ID");

                if (!locations.containsKey(locationId)) {
                    City city = cityMap.get(locationCityId);
                    Location location = new Location(locationId, locationFacility, locationRoomName, locationType,
                            city);
                    addLocation(location);
                } else {
                    Location location = locations.get(locationId);
                    location.setFacility(locationFacility);
                    location.setRoomName(locationRoomName);
                    location.setType(locationType);
                    location.setCity(cityMap.get(locationCityId));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addLocation(Location location) {
        locations.put(location.getId(), location);
    }

    public Map<Integer, Location> getLocations() {
        fetchAllLocations();
        return locations;
    }

    public void setLocations(Map<Integer, Location> locations) {
        this.locations = locations;
    }
    public void displayLocations() {
        fetchAllLocations();
        System.out.println("Locations:");
        for (Location location : locations.values()) {
            System.out.println(location.getId()+": " + location.getFacility() + " (" + location.getRoomName() + ")");
        }
    }
}
