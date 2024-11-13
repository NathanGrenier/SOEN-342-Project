package com.ngrenier.soen342;

import com.ngrenier.soen342.config.DatabaseConfig;
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

    private App() {
    }

    public static App getInstance() {
        return instance;
    }

    public void runMigrations() {
        if (currentUser == null || !currentUser.isAdmin()) {
            return;
        }

        try {
            DatabaseConfig.migrateDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void display() {
        // offeringRecords.getOfferings().forEach((k, v) -> {
        // System.out.println(v.getLesson());
        // System.out.println(v.getInstructor() != null ? v.getInstructor().getName() :
        // "No Instructor");
        // Location location = v.getLocation();
        // System.out.println("Location:");
        // System.out.println("\t" + location.getFacility());
        // System.out.println("\t" + location.getRoomName());
        // System.out.println("\t" + location.getType());
        // System.out.println("\t" + location.getCity().getName());

        // Schedule schedule = v.getSchedule();
        // System.out.println("Schedule:");
        // System.out.println("\t" + schedule.getStartDate());
        // System.out.println("\t" + schedule.getEndDate());
        // List<TimeSlot> timeSlot = schedule.getTimeSlots();
        // timeSlot.forEach(ts -> {
        // System.out.println("\t\t" + ts.getDay());
        // System.out.println("\t\t" + ts.getStartTime());
        // System.out.println("\t\t" + ts.getEndTime());
        // });
        // });

        // bookingRecords.getBookings().forEach((k, v) -> {
        // System.out.println("Booking: " + k);
        // System.out.println(v.getClient() != null ? v.getClient().getName() : "No
        // Client");
        // System.out.println(v.getOffering().getLesson() + " with " +
        // (v.getOffering().getInstructor() != null
        // ? v.getOffering().getInstructor().getName()
        // : "No Instructor"));
        // });
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
