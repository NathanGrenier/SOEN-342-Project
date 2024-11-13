package com.ngrenier.soen342;

import java.util.Scanner;

public class Terminal {
    static App app = new App();

    public static void main(String[] args) {
        System.out.println("\n=== Welcome to the Lesson Booking System ===");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            String userType = app.getCurrentUserType();
            displayMenu(userType);
            int operation = scanner.nextInt();
            scanner.nextLine();

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
        scanner.close();
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
                System.out.println("2. View Accepted Offerings");
                System.out.println("3. Logout");
                break;
            case "Admin":
                System.out.println("\n=== Select an Option Below by Entering its Number ===");
                System.out.println("1. View All Bookings");
                System.out.println("2. Edit Offerings");
                System.out.println("3. Delete User Account");
                System.out.println("4. Logout");
                break;
            default:
                System.out.println("Error: Invalid user type. Ending session.");
                System.exit(0);
                break;
        }
        System.out.print("Enter your choice: ");
    }

    private static void handleClientOperation(int operation) {
        switch (operation) {
            case 1:
                app.viewClientOfferings();
                break;
            case 2:
                System.out.println("\n=== Select an Offering by Entering its Number ===");
                app.bookClientOffering();
                break;
            case 3:
                app.viewClientBookings();
                break;
            case 4:
                app.cancelClientBooking();
                break;
            default:
                System.out.println("Invalid operation for Client. Please try again.");
        }
    }

    public static void handleInstructorOperation(int operation) {
        switch (operation) {
            case 1:
                app.viewInstructorOfferings();
                break;
            case 2:
                app.acceptInstructorLesson();
                break;
            default:
                System.out.println("Invalid operation for Instructor. Please try again.");
        }
    }

    public static void handleAdminOperation(int operation) {
        switch (operation) {
            case 1:
                app.viewAllBookings();
                break;
            case 2:
                app.editOfferings();
                break;
            case 3:
                app.deleteUser();
                break;
            default:
                System.out.println("Invalid operation for Admin. Please try again.");
        }
    }

    public static void handlePublicOperation(int operation) {
        Scanner scanner = new Scanner(System.in);
        String username, password;
        switch (operation) {
            case 1:
                System.out.println("\nUsername:");
                username = scanner.nextLine();
                System.out.println("\nPassword:");
                password = scanner.nextLine();
                break;
            case 2:
                System.out.println("\n=== Select an account type ===");
                System.out.println("1. Client");
                System.out.println("2. Instructor");
                int accountType;
                while (true) {
                    accountType = scanner.nextInt();
                    if (accountType == 1 || accountType == 2) {
                        break;
                    } else {
                        System.out.println("Invalid account type. Please try again.");
                    }
                }
                scanner.nextLine();
                System.out.println("\nUsername:");
                username = scanner.nextLine();
                System.out.println("\nPassword:");
                password = scanner.nextLine();
                switch (scanner.nextInt()) {
                    case 1:
                        // app.registerClient();
                        break;
                    case 2:
                        // app.registerInstructor();
                        break;
                    default:
                        System.out.println("Invalid account type. Please try again.");
                        break;
                }
                break;
            default:
                System.out.println("Invalid operation for Public. Please try again.");
        }
        scanner.close();
    }
}
