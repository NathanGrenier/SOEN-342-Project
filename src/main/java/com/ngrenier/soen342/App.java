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
    public void processInput(String[] input) {
        String userType = input[0];
        switch (userType) {
            case "1":
                processClientOperation(input);
                break;

            case "2":
                processInstructorOperation(input);
                break;

            case "3":
                processAdminOperation(input);
                break;

            case "":
                processPublicOperation(input);
                break;

            default:
                System.out.println("Invalid user type. Please try again.");
        }
    }

    // input[2] contains an integer which represents the offering to book or cancel (chosen from a list)
    private void processClientOperation(String[] input) {
        String operation = input[1];
        switch (operation) {
            case "view":
                viewClientOfferings();
                break;
            case "book":
                bookClientOffering(input[2]);
                break;
            case "viewbooking":
                viewClientBookings();
                break;
            case "cancel":
                cancelClientBooking(input[2]);
                break;
            default:
                System.out.println("Invalid operation for Client. Please try again.");
        }
    }

    // input[2] contains an integer which represents the lesson to accept (chosen from a list)
    private void processInstructorOperation(String[] input) {
        String operation = input[1];
        switch (operation) {
            case "view":
                viewInstructorOfferings();
                break;
            case "accept":
                acceptInstructorLesson(input[2]);
                break;
            default:
                System.out.println("Invalid operation for Instructor. Please try again.");
        }
    }

    // input[2] contains an integer which represents the user to delete (chosen from a list)
    private void processAdminOperation(String[] input) {
        String operation = input[1];
        switch (operation) {
            case "view":
                viewAllBookings();
                break;
            case "edit":
                editOfferings(input);
                break;
            case "delete":
                deleteUser(input[2]);
                break;
            default:
                System.out.println("Invalid operation for Admin. Please try again.");
        }
    }

    private void processPublicOperation(String[] input) {
        String operation = input[1];
        switch (operation) {
            case "login":
                break;
            case "register":
                break;
            default:
                System.out.println("Invalid operation for Public. Please try again.");
        }
    }

    private void viewClientOfferings() {
        
        System.out.println("Viewing offerings as Client...");
    }

    private void bookClientOffering(String offering) {
        System.out.println("Booking offering: " + offering);
    }

    private void viewClientBookings() {
        System.out.println("Viewing Client bookings...");
    }

    private void cancelClientBooking(String booking) {
        System.out.println("Cancelling Client booking: " + booking);
    }

    private void viewInstructorOfferings() {
        System.out.println("Viewing offerings as Instructor...");
    }

    private void acceptInstructorLesson(String lesson) {
        System.out.println("Accepting lesson: " + lesson);
    }
    //admin methods
    private void viewAllBookings() {
        System.out.println("Viewing all bookings as Admin...");
    }

    private void editOfferings(String[] input) {
        System.out.println("Editing offerings as Admin...");
    }

    private void deleteUser(String user) {
        System.out.println("Deleting user: " + user);
    }
}
