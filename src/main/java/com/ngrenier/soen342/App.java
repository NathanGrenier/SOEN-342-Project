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

    // TODO: Terminal should handle the case when a user was not found.
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

    // TODO: Terminal should handle the case when a user can't logout.
    public void logout() throws IllegalStateException {
        try {
            authService.logout(currentUser);
        } catch (IllegalStateException e) {
            throw e;
        } finally {
            currentUser = null;
        }
    }

    public void registerClient(String name, int age, String username, String password, String guardianUserName)
            throws IllegalStateException {
        Client newClient = registrationService.registerClient(name, age, username, password, guardianUserName,
                clientRecords.getClients());

        clientRecords.addClient(newClient);
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

    public void displayInstructors() {
        instructorRecords.getInstructors().forEach((k, v) -> {
            System.out.println(v.getId() + " " + v.getName() + " " + v.getUsername() + " " + v.getPhoneNumber());
            System.out.println("Specializations: ");
            v.getSpecializations().forEach((k1, v1) -> {
                System.out.println("\t" + v1.getId() + " " + v1.getName());
            });
            System.out.println("Cities: ");
            v.getCities().forEach((k1, v1) -> {
                System.out.println("\t" + v1.getId() + " " + v1.getName());
            });
        });
    }

    public void displayClients() {
        for (Client client : clientRecords.getClients().values()) {
            System.out.println(
                    client.getId() + " " + client.getName() + " " + client.getAge() + " " + client.getUsername()
                            + " " + client.getPassword() + " " + client.getGuardianId());
        }
    }

    public void displaySpecializations() {
        instructorRecords.getSpecializationRecords().displaySpecializations();
    }

    public void displayCities() {
        instructorRecords.getCityRecords().displayCities();
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
