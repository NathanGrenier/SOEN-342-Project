package com.ngrenier.soen342;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Location location = (Location) obj;
        return facility.equals(location.facility) && roomName.equals(location.roomName) && type.equals(location.type)
                && city.equals(location.city);
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
