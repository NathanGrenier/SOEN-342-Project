package com.ngrenier.soen342.users;

public class Specialization {
    int id;
    String name;

    public Specialization(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
