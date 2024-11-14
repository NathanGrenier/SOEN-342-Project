package com.ngrenier.soen342;

import java.util.Scanner;

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
                System.out.println("4. Exit");
                break;
            case "Client":
                System.out.println("\n=== Select an Option Below by Entering its Number ===");
                System.out.println("1. View Offerings");
                System.out.println("2. Make a Booking");
                System.out.println("3. View My Bookings");
                System.out.println("4. Cancel a Booking");
                System.out.println("5. Logout");
                break;
            case "Instructor":
                System.out.println("\n=== Select an Option Below by Entering its Number ===");
                System.out.println("1. View Available Offerings to Take");
                System.out.println("2. View My Offerings");
                System.out.println("3. Logout");
                break;
            case "Admin":
                System.out.println("\n=== Select an Option Below by Entering its Number ===");
                System.out.println("1. View Bookings");
                System.out.println("2. Create New Offerings");
                System.out.println("3. View Accounts...");
                System.out.println("4. Delete User Account");
                System.out.println("5. Logout");
                break;
            default:
                System.out.println("Error: Invalid user type. Ending session.");
                System.exit(0);
                break;
        }
    }

    private static void handleClientOperation(int operation) {
        switch (operation) {
            case 1:
                // app.viewClientOfferings();
                break;
            case 2:
                System.out.println("\n=== Select an Offering by Entering its Number ===");
                // app.bookClientOffering();
                break;
            case 3:
                // app.viewClientBookings();
                break;
            case 4:
                // app.cancelClientBooking();
                break;
            case 5:
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
                // app.viewInstructorOfferings();
                break;
            case 2:
                // app.acceptInstructorLesson();
                break;
            case 3:
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
        String accountType;
        switch (operation) {
            case 1:
                // app.viewAllBookings();
                break;
            case 2:
                // app.createOffering();
                break;
            case 3:
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
            case 4:
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
                try {
                    app.deleteUser(accountType, username);
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 5:
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
        // TODO: 3. View Offerings

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
                // app.viewPublicOfferings();
                break;
            case 4:
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
