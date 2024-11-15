package com.ngrenier.soen342;

public class City {
    private int id;
    private String name;
    private String province;

    public City(int id, String name, String province) {
        this.id = id;
        this.name = name;
        this.province = province;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        City city = (City) obj;
        return name.equals(city.getName()) && province.equals(city.getProvince());
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
