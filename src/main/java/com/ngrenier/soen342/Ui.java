package com.ngrenier.soen342;
import java.util.List;
import java.util.Scanner;

public class Ui {

    private User user;
    private List<Offering> offerings;
    private Scanner scanner = new Scanner(System.in);

    public Ui() {
        Offering.fetchOfferings();
        User.fetchUsers();
    }

    public void displayMenu() {
        
        if (user == null) {
            System.out.println("\n=== Welcome to the Lesson Booking System ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
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
            System.out.println("2. View Accepted Offerings");
            System.out.println("3. Logout");
        } else if (user instanceof Admin) {
            // Options for Admins
            System.out.println("\n=== Enter an integer to choose an option ===");
            System.out.println("1. View All Bookings");
            System.out.println("2. Edit Offerings");
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
                        login();
                        break;
                    case 2:
                        user = register(user);
                        break;    
                    case 3:
                        running = false;
                        System.out.println("Exiting system. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } else if (user instanceof Client) {
                switch (choice) {
                    case 1:
                        user.viewOfferings();
                        break;
                    case 2:
                        user.bookOffering();
                        break;
                    case 3:
                        user.viewBookings();
                        break;
                    case 4:
                        user.cancelBooking();
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
                        user.viewOfferings();
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
                        user.viewOfferings();
                        break;
                    case 2:
                        user.editOffering();
                        break;
                    case 3:
                        user.deleteOffering();
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

    private void login() {
        System.out.println("Logging in as client...");
        user = new User();
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
                user = new Client(name,pass);
                //add to array of clients (to implement)
                //add to DB (to implement)
                break;
            case 2:
                user = new Instructor(name,pass);
                //add to array of clients (to implement)
                //add to DB (to implement)
                break;
            case 3:
                user = new Admin(name,pass);
                //add to array of clients (to implement)
                //add to DB (to implement)
                break;
            default:
            System.out.println("Invalid choice, please try again.");
        }
        return user;
    }
    private void logout() {
        System.out.println("Logging out...");
        user = null;
    }
}
