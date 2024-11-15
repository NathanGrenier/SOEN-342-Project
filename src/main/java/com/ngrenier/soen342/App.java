package com.ngrenier.soen342;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.sql.Date;

import com.ngrenier.soen342.services.AuthenticationService;
import com.ngrenier.soen342.services.RegistrationService;

import com.ngrenier.soen342.users.Admin;
import com.ngrenier.soen342.users.AdminRecords;
import com.ngrenier.soen342.users.Client;
import com.ngrenier.soen342.users.ClientRecords;
import com.ngrenier.soen342.users.Instructor;
import com.ngrenier.soen342.users.InstructorRecords;
import com.ngrenier.soen342.users.Specialization;
import com.ngrenier.soen342.users.SpecializationRecords;
import com.ngrenier.soen342.users.User;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class App {
    private AuthenticationService authService = AuthenticationService.getInstance();
    private RegistrationService registrationService = RegistrationService.getInstance();
    private User currentUser = null;

    private AdminRecords adminRecords = AdminRecords.getInstance();
    private ClientRecords clientRecords = ClientRecords.getInstance();
    private InstructorRecords instructorRecords = InstructorRecords.getInstance();
    private LocationRecords locationRecords = LocationRecords.getInstance();
    private SpecializationRecords specializationRecords = SpecializationRecords.getInstance();
    private CityRecords cityRecords = CityRecords.getInstance();


    private OfferingRecords offeringRecords = OfferingRecords.getInstance();
    private BookingRecords bookingRecords = BookingRecords.getInstance();

    public Map<Integer, Offering> filterOfferings(String criteria) {
        return offeringRecords.getOfferings().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

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
                cityNames, instructorRecords, cityRecords, specializationRecords);

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
        specializationRecords.displaySpecializations();
    }
    public void displayCities() {
        cityRecords.displayCities();
    }

    public void clientViewPublicOfferings() {
        if (currentUser instanceof Client) {
            bookingRecords.clientDisplayPublicOfferings((Client) currentUser);
        } else {
            throw new IllegalStateException("User must be a client to view their annotated offerings.");
        }
    }

    public void createBooking(int offeringId) throws IllegalStateException {
        if (currentUser instanceof Client) {
            Offering offering = offeringRecords.getOfferings().get(offeringId);
            if (offering == null) {
                throw new IllegalStateException("Offering with the ID of '" + offeringId + "' was not found.");
            }

            bookingRecords.createBooking(offering, (Client) currentUser);
        } else {
            throw new IllegalStateException("User must be an instructor to create an offering.");
        }
    }

    public void viewClientBookings() {
        if (currentUser instanceof Client) {
            bookingRecords.displayClientBookings((Client) currentUser);
        } else {
            throw new IllegalStateException("User must be an client to view your bookings.");
        }
    }

    public void viewClientBookings(String username) throws IllegalStateException {
        Client client = clientRecords.getClients()
                .values().stream()
                .filter(c -> c.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Client with the username of '" + username + "' was not found."));

        bookingRecords.displayClientBookings((Client) client);
    }

    public void cancelBooking(int offeringId) throws IllegalStateException {
        if (currentUser instanceof Client) {
            Offering offering = offeringRecords.getOfferings().get(offeringId);
            if (offering == null) {
                throw new IllegalStateException("Offering with the ID of '" + offeringId + "' was not found.");
            }
            bookingRecords.cancelBooking(offering, (Client) currentUser);
        } else {
            throw new IllegalStateException("User must be a client to cancel a booking.");
        }
    }

    public void updateGuardian(String username) throws IllegalStateException {
        if (currentUser instanceof Client) {
            Client client = (Client) currentUser;
            Client guardian = clientRecords.getClients().values().stream()
                    .filter(c -> c.getUsername().equals(username))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "Client with the username of '" + username + "' was not found."));

            if (guardian.equals(client)) {
                throw new IllegalStateException("You cannot be your own guardian.");
            }

            if (guardian.getAge() < 18) {
                throw new IllegalStateException("A guardian must be 18 years or older.");
            }

            clientRecords.updateGuardian(client, guardian);
        } else {
            throw new IllegalStateException("User must be a client to add a guardian.");
        }
    }

    public void displayCurrentGuardian() throws IllegalStateException {
        if (currentUser instanceof Client) {
            Client client = (Client) currentUser;
            Client guardian = client.getGuardian();
            if (guardian != null) {
                System.out.println("Your current guardian is:");
                System.out.println("- Name: " + guardian.getName());
                System.out.println("- Username: " + guardian.getUsername());
                System.out.println("- Age: " + guardian.getAge());
            } else {
                System.out.println("No guardian assigned.");
            }
        } else {
            throw new IllegalStateException("User must be a client to view their guardian.");
        }
    }
    public void displayLocations() {
        locationRecords.displayLocations();
    }
    public void clientViewPublicOfferings() {
        if (currentUser instanceof Client) {
            bookingRecords.clientDisplayPublicOfferings((Client) currentUser);
        } else {
            throw new IllegalStateException("User must be a client to view their annotated offerings.");
        }
    }
    public void viewInstructorAvailableOfferings(){
        
        if (currentUser instanceof Instructor) {
            Instructor instructor = (Instructor) currentUser;
            HashMap<Integer,City> availableCities = instructor.getCities();
            HashMap<Integer,Specialization> instructorSpecializations = instructor.getSpecializations();
            Map<Integer, Offering> publicOfferings = offeringRecords.getOfferings();
            publicOfferings = publicOfferings.values().stream()
                .filter(offering -> offering.getInstructor() == null)
                .filter(offering -> availableCities.values().contains(offering.getLocation().getCity()))
                .filter(offering -> instructorSpecializations.values().stream()
                .anyMatch(spec -> offering.getLesson().contains(spec.getName()))).collect(Collectors.toMap(offering -> offering.getId(), offering -> offering));
            offeringRecords.displayOfferings(publicOfferings);
        } else {
            throw new IllegalStateException("User must be an instructor to view their offerings.");
        }
    }
    public void instructorViewAllOfferings() {
        if (currentUser instanceof Instructor) {
            Map<Integer, Offering> publicOfferings = offeringRecords.getOfferings();
            offeringRecords.displayOfferings(publicOfferings);
        } else {
            throw new IllegalStateException("User must be an instructor to view all offerings.");
        }
    }
    public void adminViewOfferings() {
        if (currentUser instanceof Admin) {
            Map<Integer, Offering> publicOfferings = offeringRecords.getOfferings();
            offeringRecords.displayOfferings(publicOfferings);
        } else {
            throw new IllegalStateException("User must be an Admin to view all offerings.");
        }
    }
    public void publicViewOfferings(){
        Map<Integer, Offering> publicOfferings = offeringRecords.getOfferings();
        publicOfferings = publicOfferings.values().stream()
                .filter(offering -> offering.getCurrentCapacity() < offering.getMaxCapacity())
                .collect(Collectors.toMap(offering -> offering.getId(), offering -> offering));
        offeringRecords.displayOfferings(publicOfferings);
    }
    public void acceptInstructorLesson(int offeringId) {
        // Retrieve the offering from the in-memory collection
        Offering selectedOffering = offeringRecords.getOfferings().get(offeringId);

        if (selectedOffering == null) {
            throw new IllegalStateException("Offering with ID '" + offeringId + "' does not exist.");
        }

        if (selectedOffering.getInstructor() != null) {
            throw new IllegalStateException("Offering with ID '" + offeringId + "' already has an instructor.");
        }

        // Assign the instructor to the offering
        selectedOffering.setInstructor((Instructor) currentUser);

        // Update the database
        offeringRecords.updateOfferingInstructor((Instructor) currentUser,offeringId);

        System.out.println("Instructor " + currentUser.getName() + " has been successfully assigned to Offering ID: " + offeringId);
    }
   
    public void adminCreateOffering(String lesson, int locationId, List<String> timeSlots, int capacity, boolean isPrivate,
                                String startDateString, String endDateString) {
    try {
        Location location = LocationRecords.getInstance().getLocations().get(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location with ID " + locationId + " not found.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate = LocalDate.parse(startDateString, formatter);
        LocalDate endLocalDate = LocalDate.parse(endDateString, formatter);

        if (endLocalDate.isBefore(startLocalDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        Date startDate = Date.valueOf(startLocalDate);
        Date endDate = Date.valueOf(endLocalDate);

        Map<Integer, TimeSlot> timeSlotMap = new HashMap<>();
        int timeSlotId = 1;
        for (String timeSlotInfo : timeSlots) {
            String[] parts = timeSlotInfo.split(",");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid time slot format. Expected: DAY,HH:mm,HH:mm");
            }

            // Use DayOfWeek enum directly without converting to String
            DayOfWeek day = DayOfWeek.valueOf(parts[0].toUpperCase());
            Time startTime = Time.valueOf(parts[1] + ":00");
            Time endTime = Time.valueOf(parts[2] + ":00");

            // Add the TimeSlot to the map
            timeSlotMap.put(timeSlotId, new TimeSlot(timeSlotId, day, startTime, endTime));
            timeSlotId++;
        }

        // Create a new Schedule object
        Schedule schedule = new Schedule(0, startDate, endDate, timeSlotMap);

        // Save schedule to the database and get its ID
        int scheduleId = ScheduleRecords.getInstance().saveScheduleToDatabase(schedule);
        schedule.setId(scheduleId);

        // Create and save the Offering
        Offering offering = offeringRecords.newOffering(lesson, location, schedule, capacity, isPrivate);
        boolean success = offeringRecords.addOffering(offering);
        if (!success) {
            throw new IllegalStateException("Failed to save the offering to the database.");
        }

        System.out.println("Offering successfully created.");
    } catch (Exception e) {
        System.out.println("Error creating offering: " + e.getMessage());
    }
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

    public void viewInstructorAvailableOfferings() {

        if (currentUser instanceof Instructor) {
            Instructor instructor = (Instructor) currentUser;
            HashMap<Integer, City> availableCities = instructor.getCities();
            HashMap<Integer, Specialization> instructorSpecializations = instructor.getSpecializations();
            Map<Integer, Offering> publicOfferings = offeringRecords.getOfferings();
            publicOfferings = publicOfferings.values().stream()
                    .filter(offering -> offering.getInstructor() == null)
                    .filter(offering -> availableCities.values().contains(offering.getLocation().getCity()))
                    .filter(offering -> instructorSpecializations.values().stream()
                            .anyMatch(spec -> offering.getLesson().contains(spec.getName())))
                    .collect(Collectors.toMap(offering -> offering.getId(), offering -> offering));
            offeringRecords.displayOfferings(publicOfferings);
        } else {
            throw new IllegalStateException("User must be an instructor to view their offerings.");
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
