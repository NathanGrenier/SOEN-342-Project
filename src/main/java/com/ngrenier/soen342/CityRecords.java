package com.ngrenier.soen342;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ngrenier.soen342.config.DatabaseConfig;

public class CityRecords {
    private Map<Integer, City> cities = new HashMap<>();

    private static CityRecords instance = new CityRecords();

    private CityRecords() {
    }

    public static CityRecords getInstance() {
        return instance;
    }

    public void fetchAllCities() {
        String sql = "SELECT * FROM City";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int cityId = rs.getInt("CI_ID");
                String cityName = rs.getString("CI_NAME");
                String cityProvince = rs.getString("CI_PROVINCE");

                if (!cities.containsKey(cityId)) {
                    City city = new City(cityId, cityName, cityProvince);
                    addCity(city);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCity(City city) {
        cities.put(city.getId(), city);
    }

    public City getCityById(int id) {
        return cities.get(id);
    }

    public Map<Integer, City> getCities() {
        fetchAllCities();
        return cities;
    }

    public void setCities(Map<Integer, City> cities) {
        this.cities = cities;
    }
}
