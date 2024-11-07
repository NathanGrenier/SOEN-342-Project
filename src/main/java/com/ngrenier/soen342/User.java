package com.ngrenier.soen342;

import java.util.ArrayList;
import java.util.List;

public class User {
    int userId;
    String name;
    int phone;
    String userType;
    // Static list to hold all users
    private static List<User> users = new ArrayList<>();
    //static method to fetch users from DB
    public static void fetchUsers(){

    }
    //return user instance
    public String findUser(String name, String pass){
        //get the user type to create an instance of that type
        String userType = "";
        User user = null;
        return userType;
    }
    //methods to be implemented by subclasses (for polymorphism)
    public void viewBookings(){}
    public void viewOfferings(){}
    public void bookOffering(){}
    public void cancelBooking(){}
    public void editOffering(){}
    public void deleteOffering(){}
    
}
