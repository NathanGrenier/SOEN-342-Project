package com.ngrenier.soen342;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Terminal {
    static App app = new App();
    static Scanner scanner = new Scanner(System.in);
    static boolean running = true;

    public static void main(String[] args) {
        System.out.println("\n=== Welcome to the Lesson Booking System ===");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            running = false;
            try {
                if (app.getCurrentUserType() != "Public") {
                    app.logout();
                    System.out.println("\nLogged out successfully.");
                }
            } catch (IllegalStateException e) {
                System.out.println("\nError logging out: " + e.getMessage());
            }
            System.out.println("Exiting...");
        }));

        try {
            while (running) {
                String userType = app.getCurrentUserType();
                int operation = -1;
                while (operation == -1) {
                    displayMenu(userType);
                    System.out.print("Enter your choice: ");
                    if (scanner.hasNextInt()) {
                        operation = scanner.nextInt();
                        scanner.nextLine();
                    } else {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.next();
                    }
                }

                switch (userType) {
                    case "Public":
                        handlePublicOperation(operation);
                        break;
                    case "Client":
                        handleClientOperation(operation);
                        break;
                    case "Instructor":
                        handleInstructorOperation(operation);
                        break;
                    case "Admin":
                        handleAdminOperation(operation);
                        break;
                    case "Unknown":
                        System.out.println("Error: Invalid user type. Ending session.");
                        System.exit(0);
                        break;
                }
            }
        } catch (Exception e) {
            try {
                if (app.getCurrentUserType() != "Public") {
                    app.logout();
                    System.out.println("\nLogged out successfully.");
                }
            } catch (IllegalStateException error) {
                System.out.println("\nError logging out: " + error.getMessage());
            }
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void displayMenu(String userType) {

        switch (userType) {
            case "Public":
                System.out.println("\n=== Select an Option Below by Entering its Number ===");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. View Offerings");
                System.out.println("0. Exit");
                break;
            case "Client":
                System.out.println("\n=== Select an Option Below by Entering its Number ===");
                System.out.println("1. View Offerings");
                System.out.println("2. Make a Booking");
                System.out.println("3. View My Bookings");
                System.out.println("4. Cancel a Booking");
                System.out.println("5. Display My Guardian");
                System.out.println("6. Add a Guardian");
                System.out.println("0. Logout");
                break;
            case "Instructor":
                System.out.println("\n=== Select an Option Below by Entering its Number ===");
                System.out.println("1. View Available Offerings to Take");
                System.out.println("2. Take an Offering");
                System.out.println("0. Logout");
                break;
            case "Admin":
                System.out.println("\n=== Select an Option Below by Entering its Number ===");
                System.out.println("1. View Bookings");
                System.out.println("2. View Offerings");
                System.out.println("3. Create New Offerings");
                System.out.println("4. View Accounts...");
                System.out.println("5. Delete User Account");
                System.out.println("0. Logout");
                break;
            default:
                System.out.println("Error: Invalid user type. Ending session.");
                System.exit(0);
                break;
        }
    }

    private static void handleClientOperation(int operation) {
        int offeringId;

        switch (operation) {
            case 1:
                app.clientViewPublicOfferings();
                break;
            case 2:
                System.out.println("\n=== Select an Offering by its Number ===");
                app.clientViewPublicOfferings();
                System.out.print("Enter the offering ID: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid offering ID.");
                    scanner.next();
                }
                offeringId = scanner.nextInt();
                scanner.nextLine();
                try {
                    app.createBooking(offeringId);
                    System.out.println("Booking for offering (" + offeringId + ") created successfully.");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 3:
                System.out.println("\n=== Your Bookings ===");
                app.viewClientBookings();
                break;
            case 4:
                app.viewClientBookings();
                System.out.println("Enter the id of the booking you would like to cancel: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid booking ID.");
                    scanner.next();
                }
                int bookingId = scanner.nextInt();
                scanner.nextLine();
                try {
                    app.cancelBooking(bookingId);
                    System.out.println("Booking (" + bookingId + ") cancelled successfully.");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 5:
                try {
                    app.displayCurrentGuardian();
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 6:
                System.out.print(
                        "Enter your new guardian's username: ");
                String username = scanner.nextLine();
                username = username.trim();
                try {
                    app.updateGuardian(username);
                    System.out.println(username + " is now your guardian.");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 0:
                try {
                    app.logout();
                    System.out.println("Logged out successfully.");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                System.out.println("Invalid operation for Client. Please try again.");
        }
    }

    public static void handleInstructorOperation(int operation) {
        switch (operation) {
            case 1:
                app.instructorViewAllOfferings();
                break;
            case 2:
                int count = app.viewInstructorAvailableOfferings();
                if (count <= 0) {
                    break;
                }
                int offeringId;
                while (true) {
                    System.out.print("Enter the Offering ID to accept: ");
                    if (scanner.hasNextInt()) {
                        offeringId = scanner.nextInt();
                        scanner.nextLine();
                        break;
                    } else {
                        System.out.println("Invalid id. Please try again.");
                        app.viewInstructorAvailableOfferings();
                        scanner.next();
                    }
                }
                try {
                    app.acceptInstructorLesson(offeringId);
                    System.out.println("Offering (" + offeringId + ") accepted successfully.");
                    break;
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 0:
                try {
                    app.logout();
                    System.out.println("Logged out successfully.");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                System.out.println("Invalid operation for Instructor. Please try again.");
        }
    }

    public static void handleAdminOperation(int operation) {
        // System.out.println("\n=== Select an Option Below by Entering its Number
        // ===");
        // System.out.println("1. View Bookings");
        // System.out.println("2. View Offerings");
        // System.out.println("3. Create New Offerings");
        // System.out.println("4. View Accounts...");
        // System.out.println("5. Delete User Account");
        // System.out.println("0. Logout");

        String accountType;
        switch (operation) {
            case 1:
                app.displayUsers("C");
                System.out.print("Select a client by their username to view their bookings: ");
                String clientUsername = scanner.nextLine();
                clientUsername = clientUsername.trim();
                try {
                    app.viewClientBookings(clientUsername);
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                app.adminViewOfferings();
                break;
            case 3:
                System.out.println("\n=== Select a Location from the following ===");
                int location;
                while (true) {
                    app.displayLocations();
                    System.out.print("Enter the Location ID: ");
                    if (scanner.hasNextInt()) {
                        location = scanner.nextInt();
                        scanner.nextLine();
                        break;
                    } else {
                        System.out.println("Invalid id. Please try again.");
                        scanner.nextLine();
                        continue;
                    }
                }
                app.displaySpecializations();
                System.out.print("Type the kind of Lesson: ");
                String lesson = scanner.nextLine();
                int maxCapacity;
                while (true) {
                    System.out.print("Enter the max capacity: ");
                    if (scanner.hasNextInt()) {
                        maxCapacity = scanner.nextInt();
                        if (maxCapacity <= 0) {
                            System.out.println("Max capacity must be greater than 0. Please try again.");
                            continue;
                        }
                        scanner.nextLine();
                        break;
                    } else {
                        System.out.println("Max capacity must be an int. Please try again.");
                        scanner.next();
                    }
                }
                LocalDate start;
                while (true) {
                    System.out.print("Enter the Start Date (yyyy-MM-dd): ");
                    String startDate = scanner.nextLine();
                    try {
                        start = LocalDate.parse(startDate);
                        break;
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please enter the date in the format yyyy-MM-dd.");
                    }
                }

                LocalDate end;
                while (true) {
                    System.out.print("Enter the End Date (yyyy-MM-dd): ");
                    String endDate = scanner.nextLine();
                    try {
                        end = LocalDate.parse(endDate);
                        break;
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please enter the date in the format yyyy-MM-dd.");
                        endDate = scanner.nextLine();
                    }
                }

                List<String> timeSlots = new ArrayList<>();
                while (true) {
                    System.out.print(
                            "Enter Time Slot (DAY,HH:mm,HH:mm) [24 hour format]. Press Enter to finish: ");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("")) {
                        if (timeSlots.size() == 0) {
                            System.out.println(
                                    "At least one time slot is required.");
                            continue;
                        }
                        break;
                    }
                    if (input.matches("^[A-Za-z]+,\\d{2}:\\d{2},\\d{2}:\\d{2}$")) {
                        String[] parts = input.split(",");
                        parts[0] = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1).toLowerCase();
                        if (!Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                                .contains(parts[0])) {
                            System.out.println("Invalid day. Please enter a valid day of the week.");
                            continue;
                        }
                        timeSlots.add(input);
                    } else {
                        System.out.println("Invalid format. Please enter the time slot in the format DAY,HH:mm,HH:mm.");
                    }
                }
                try {
                    app.adminCreateOffering(lesson, location, timeSlots, maxCapacity, start, end);
                    System.out.println("Offering successfully created.");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 4:
                while (true) {
                    System.out.println("\n=== Select Account Type to View ===");
                    System.out.println("(C)lients");
                    System.out.println("(I)nstructors");
                    System.out.println("(A)dmins");
                    System.out.print("Enter account type: ");
                    accountType = scanner.next().toUpperCase();
                    if (accountType.equals("C") || accountType.equals("I") || accountType.equals("A")) {
                        break;
                    } else {
                        System.out.println("Invalid account type. Please try again.");
                    }
                }
                app.displayUsers(accountType);
                break;
            case 5:
                while (true) {
                    System.out.println("\n=== Select Account Type to Delete ===");
                    System.out.println("(C)lients");
                    System.out.println("(I)nstructors");
                    System.out.print("Enter account type: ");
                    accountType = scanner.next().toUpperCase();
                    if (accountType.equals("C") || accountType.equals("I")) {
                        break;
                    } else {
                        System.out.println("Invalid account type. Please try again.");
                    }
                }
                app.displayUsers(accountType);
                System.out.print("Enter the username of the account to delete: ");
                String username = scanner.next();
                username = username.trim();
                try {
                    app.deleteUser(accountType, username);
                    System.out.println("User (" + username + ") deleted successfully.");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 0:
                try {
                    app.logout();
                    System.out.println("Logged out successfully.");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                System.out.println("Invalid operation for Admin. Please try again.");
        }
    }

    public static void handlePublicOperation(int operation) {
        String username, password, name;
        switch (operation) {
            case 1:
                System.out.print("Username: ");
                username = scanner.nextLine();
                System.out.print("Password: ");
                password = scanner.nextLine();
                try {
                    app.login(username, password);
                    System.out.println("Successfully logged in as: " + app.getCurrentUserType());
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                int accountType;
                while (true) {
                    System.out.println("\n=== Register as a: ===");
                    System.out.println("1. Client");
                    System.out.println("2. Instructor");
                    System.out.print("Enter account type: ");
                    if (scanner.hasNextInt()) {
                        accountType = scanner.nextInt();
                        if (accountType == 1 || accountType == 2) {
                            break;
                        } else {
                            System.out.println("Invalid account type. Please try again.");
                        }
                    } else {
                        System.out.println("Invalid account type. Please try again.");
                        scanner.next();
                    }
                }
                scanner.nextLine();
                System.out.print("Enter name: ");
                name = scanner.nextLine();
                System.out.print("Username: ");
                username = scanner.nextLine();
                System.out.print("Password: ");
                password = scanner.nextLine();
                switch (accountType) {
                    case 1:
                        int age;
                        while (true) {
                            System.out.print("Enter age: ");
                            if (scanner.hasNextInt()) {
                                age = scanner.nextInt();
                                scanner.nextLine();
                            } else {
                                System.out.println("Invalid age. Please try again.");
                                scanner.nextLine();
                                continue;
                            }
                            try {
                                app.validateAge(age);
                                break;
                            } catch (IllegalStateException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        System.out.print("Enter guardian's username: ");
                        String guardianUsername = scanner.nextLine();
                        try {
                            app.registerClient(name, username, password, age, guardianUsername);
                        } catch (IllegalStateException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 2:
                        String phone = null;
                        while (phone == null) {
                            System.out.print("Enter your PhoneNumber: ");
                            phone = scanner.nextLine();
                            try {
                                app.validatePhone(phone);
                            } catch (IllegalStateException e) {
                                System.out.println(e.getMessage());
                                phone = null;
                            }
                        }
                        app.displaySpecializations();
                        System.out.print("Enter your specializations (comma separated): ");
                        String specializationNames = scanner.nextLine();
                        String[] specializations = specializationNames.split(",");
                        specializations = trimArray(specializations);
                        app.displayCities();
                        System.out.print("Enter the cities you will be available in (comma separated): ");
                        String cityNames = scanner.nextLine();
                        String[] cities = cityNames.split(",");
                        cities = trimArray(cities);
                        try {
                            app.registerInstructor(name, username, password, phone, specializations, cities);
                        } catch (IllegalStateException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Invalid account type. Please try again.");
                        break;
                }
                break;
            case 3:
                app.publicViewOfferings();
                break;
            case 0:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid operation for Public. Please try again.");
        }
    }

    private static String[] trimArray(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].trim();
        }
        return arr;
    }
}
