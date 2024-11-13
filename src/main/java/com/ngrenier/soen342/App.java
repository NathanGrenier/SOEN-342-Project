package com.ngrenier.soen342;

import com.ngrenier.soen342.services.AuthenticationService;
import com.ngrenier.soen342.users.AdminRecords;
import com.ngrenier.soen342.users.ClientRecords;
import com.ngrenier.soen342.users.InstructorRecords;
import com.ngrenier.soen342.users.User;

public class App {
    private AuthenticationService authService = new AuthenticationService();
    private User currentUser = null;

    private AdminRecords adminRecords = AdminRecords.getInstance();
    private ClientRecords clientRecords = ClientRecords.getInstance();
    private InstructorRecords instructorRecords = InstructorRecords.getInstance();

    private OfferingRecords offeringRecords = OfferingRecords.getInstance();
    private BookingRecords bookingRecords = BookingRecords.getInstance();

    private static App instance = new App();
    public App(){
    }
    public static App getInstance() {
        return instance;
    }
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
    public void viewClientOfferings() {
        
        System.out.println("Viewing offerings as Client...");
    }

    public void bookClientOffering(String offering) {
        System.out.println("Booking offering: " + offering);
    }

    public void viewClientBookings() {
        System.out.println("Viewing Client bookings...");
    }

    public void cancelClientBooking(String booking) {
        System.out.println("Cancelling Client booking: " + booking);
    }

    public void viewInstructorOfferings() {
        System.out.println("Viewing offerings as Instructor...");
    }

    public void acceptInstructorLesson(String lesson) {
        System.out.println("Accepting lesson: " + lesson);
    }
    //admin methods
    public void viewAllBookings() {
        System.out.println("Viewing all bookings as Admin...");
    }

    public void editOfferings(String[] input) {
        System.out.println("Editing offerings as Admin...");
    }

    public void deleteUser(String user) {
        System.out.println("Deleting user: " + user);
    }
}
