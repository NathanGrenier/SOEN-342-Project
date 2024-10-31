package com.ngrenier.soen342;
import java.util.Scanner;

public class Ui {

    private User user;
    private Offerings offer;
    private Scanner scanner = new Scanner(System.in);

    public Ui() {
        offer = new Offerings();
    }

    public void displayMenu() {
        
        if (user == null) {
            System.out.println("\n=== Welcome to the Lesson Booking System ===");
            System.out.println("1. Login as Client");
            System.out.println("2. Login as Instructor");
            System.out.println("3. Login as Administrator");
            System.out.println("4. Register");
            System.out.println("5. Exit");
        } else if (user instanceof Client) {
            // Options for Clients
            System.out.println("\n=== Enter an integer to choose an option ===");
            System.out.println("1. View Offerings");
            System.out.println("2. Make a Booking");
            System.out.println("3. View My Bookings");
            System.out.println("4. Cancel a Booking");
            System.out.println("5. Logout");
        } else if (user instanceof Instructor) {
            // Options for Instructors
            System.out.println("\n=== Enter an integer to choose an option ===");
            System.out.println("1. View Available Offerings to Take");
            System.out.println("2. Logout");
        } else if (user instanceof Admin) {
            // Options for Admins
            System.out.println("\n=== Enter an integer to choose an option ===");
            System.out.println("1. View All Bookings");
            System.out.println("2. Manage Offerings");
            System.out.println("3. Delete User Account");
            System.out.println("4. Logout");
        }
        
        System.out.print("Enter your choice: ");
    }

    public void run() {
        boolean running = true;
        
        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (user == null) {
                switch (choice) {
                    case 1:
                        loginClient();
                        break;
                    case 2:
                        loginInstructor();
                        break;
                    case 3:
                        loginAdmin();
                        break;
                    case 4:
                        user = register(user);
                        break;    
                    case 5:
                        running = false;
                        System.out.println("Exiting system. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } else if (user instanceof Client) {
                switch (choice) {
                    case 1:
                        viewOfferings();
                        break;
                    case 2:
                        makeBooking();
                        break;
                    case 3:
                        viewBookings();
                        break;
                    case 4:
                        cancelBooking();
                        break;
                    case 5:
                        logout();
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } else if (user instanceof Instructor) {
                switch (choice) {
                    case 1:
                        viewAvailableOfferings();
                        break;
                    case 2:
                        logout();
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } else if (user instanceof Admin) {
                switch (choice) {
                    case 1:
                        viewAllBookings();
                        break;
                    case 2:
                        manageOfferings();
                        break;
                    case 3:
                        deleteUserAccount();
                        break;
                    case 4:
                        logout();
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        }
        
        scanner.close();
    }

    private void loginClient() {
        System.out.println("Loggin in as client...");
        user = new Client();  // Assume this initializes a new Client
    }

    private void loginInstructor() {
        System.out.println("Logging in as instructor...");
        user = new Instructor();  // Assume this initializes a new Instructor
    }

    private void loginAdmin() {
        System.out.println("Logging in as admin...");
        user = new Admin();  // Assume this initializes an Admin after validation
    }
    private User register(User user) {
        System.out.println("\n=== Choose what type of account ===");
        System.out.println("1. Client");
        System.out.println("2. Instructor");
        System.out.println("3. Admin");
        int type = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Please enter a username:");
        String name = scanner.nextLine();
        System.out.print("Please enter a password:");
        String pass = scanner.nextLine();
        switch (type){
            case 1:
                user = new Client();
                break;
            case 2:
            user = new Instructor();
                break;
            case 3:
                user = new Admin
                ();
                break;
            default:
            System.out.println("Invalid choice, please try again.");
        }
        return user;
    }

    private void viewOfferings() {
        System.out.println("Viewing available offerings...");
    }

    private void makeBooking() {
        System.out.println("Making a new booking...");
    }

    private void viewBookings() {
        System.out.println("Viewing your bookings...");
    }

    private void cancelBooking() {
        System.out.println("Cancelling a booking...");
    }

    private void viewAvailableOfferings() {
        System.out.println("Viewing offerings available to take as an instructor...");
    }

    private void viewAllBookings() {
        System.out.println("Viewing all bookings as Admin...");
    }

    private void manageOfferings() {
        System.out.println("Managing offerings...");
    }

    private void deleteUserAccount() {
        System.out.println("Deleting a user account...");
    }

    private void logout() {
        System.out.println("Logging out...");
        user = null;
    }
}
