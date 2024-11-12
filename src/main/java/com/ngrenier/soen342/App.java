package com.ngrenier.soen342;

import com.ngrenier.soen342.config.DatabaseConfig;
import com.ngrenier.soen342.services.AuthenticationService;
import com.ngrenier.soen342.users.Admin;
import com.ngrenier.soen342.users.Client;
import com.ngrenier.soen342.users.Instructor;
import com.ngrenier.soen342.users.User;

import java.util.List;
import java.util.ArrayList;

public class App {
    private Terminal terminal = new Terminal();
    private AuthenticationService authService = new AuthenticationService();
    private User currentUser = null;

    private static List<Offering> offeringRecords = new ArrayList<>();
    private static List<Booking> bookingRecords = new ArrayList<>();
    private static List<User> userRecords = new ArrayList<>();

    public static void main(String[] args) {
        // TODO: If the user is an Admin, maybe run the migrations.
        // try {
        // DatabaseConfig.migrateDatabase();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        fetchUserRecords();
        fetchOfferingRecords();
        fetchBookingRecords();

        // Ui ui = new Ui();
        // ui.run();
    }

    public static <T extends User> T getUserById(Class<T> userType, int id) {
        for (User user : userRecords) {
            if (userType.isInstance(user) && user.getId() == id) {
                return userType.cast(user);
            }
        }
        return null;
    }

    public static Offering getOfferingById(int id) {
        for (Offering offering : offeringRecords) {
            if (offering.getId() == id) {
                return offering;
            }
        }
        return null;
    }

    private static void fetchUserRecords() {
        ArrayList<Admin> admins = Admin.fetchAllAdmins();
        userRecords.addAll(admins);

        ArrayList<Instructor> instructors = Instructor.fetchAllInstructors();
        userRecords.addAll(instructors);

        ArrayList<Client> clients = Client.fetchAllClients();
        userRecords.addAll(clients);
    }

    private static void fetchOfferingRecords() {
        offeringRecords = Offering.fetchAllOfferings();
    }

    private static void fetchBookingRecords() {
        bookingRecords = Booking.fetchAllBookings();
    }
}
