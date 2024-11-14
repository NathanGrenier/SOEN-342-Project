package com.ngrenier.soen342;

import com.ngrenier.soen342.services.AuthenticationService;
import com.ngrenier.soen342.services.RegistrationService;

import com.ngrenier.soen342.users.Admin;
import com.ngrenier.soen342.users.AdminRecords;
import com.ngrenier.soen342.users.Client;
import com.ngrenier.soen342.users.ClientRecords;
import com.ngrenier.soen342.users.Instructor;
import com.ngrenier.soen342.users.InstructorRecords;
import com.ngrenier.soen342.users.User;

public class App {
    private AuthenticationService authService = AuthenticationService.getInstance();
    private RegistrationService registrationService = RegistrationService.getInstance();
    private User currentUser = null;

    private AdminRecords adminRecords = AdminRecords.getInstance();
    private ClientRecords clientRecords = ClientRecords.getInstance();
    private InstructorRecords instructorRecords = InstructorRecords.getInstance();

    private OfferingRecords offeringRecords = OfferingRecords.getInstance();
    private BookingRecords bookingRecords = BookingRecords.getInstance();

    private static App instance = new App();

    public App() {
    }

    public static App getInstance() {
        return instance;
    }

    public void login(String username, String password) throws IllegalStateException {
        if (currentUser != null) {
            return;
        }

        User user = null;

        user = authService.login(username, password, clientRecords.getClients(),
                instructorRecords.getInstructors(), adminRecords.getAdmins());

        if (user != null) {
            currentUser = user;
        }
    }

    public void logout() throws IllegalStateException {
        try {
            authService.logout(currentUser);
        } catch (IllegalStateException e) {
            throw e;
        } finally {
            currentUser = null;
        }
    }

    public void registerClient(String name, String username, String password, int age, String guardianUserName)
            throws IllegalStateException {
        Client newClient = registrationService.registerClient(name, age, username, password, guardianUserName,
                clientRecords.getClients());

        clientRecords.addClient(newClient);
    }

    public boolean validateAge(int age) throws IllegalStateException {
        return registrationService.validateAge(age);
    }

    public void registerInstructor(String name, String username, String password, String phone,
            String[] specializationNames,
            String[] cityNames) throws IllegalStateException {
        Instructor newInstructor = registrationService.registerInstructor(name, username, password, phone,
                specializationNames,
                cityNames, instructorRecords);

        instructorRecords.addInstructor(newInstructor);
    }

    public boolean validatePhone(String phone) throws IllegalStateException {
        return registrationService.validatePhone(phone);
    }

    public void displayUsers(String userType) {
        switch (userType) {
            case "A":
                adminRecords.displayAdmins();
                break;
            case "I":
                instructorRecords.displayInstructors();
                break;
            case "C":
                clientRecords.displayClients();
                break;
            default:
                System.out.println("Invalid user type.");
        }
    }

    public void displaySpecializations() {
        instructorRecords.getSpecializationRecords().displaySpecializations();
    }

    public void displayCities() {
        instructorRecords.getCityRecords().displayCities();
    }

    public void deleteUser(String userType, String username) {
        switch (userType) {
            case "I":
                instructorRecords.deleteInstructor(username);
                bookingRecords.pruneBookingsWithoutInstructor();
                break;
            case "C":
                clientRecords.deleteClient(username);
                break;
            default:
                System.out.println("Invalid user type.");
        }
    }

    public String getCurrentUserType() {
        if (currentUser instanceof Admin) {
            return "Admin";
        } else if (currentUser instanceof Instructor) {
            return "Instructor";
        } else if (currentUser instanceof Client) {
            return "Client";
        } else if (currentUser == null) {
            return "Public";
        } else {
            return "Unknown";
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
