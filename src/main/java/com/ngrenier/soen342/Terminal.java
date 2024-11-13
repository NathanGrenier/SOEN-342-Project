package com.ngrenier.soen342;
import java.util.Scanner;

public class Terminal {
    public static void main(String[] args) {
        String[] input = new String[10];
        Scanner scanner = new Scanner(System.in);
        App app = new App();
        boolean running = true;
        while (running) {
            displayMenu(input[0]);
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (input[0].equals("")) {
                switch (choice) {
                    case 1:
                        input[1] = "login";
                        System.out.println("\n=== Select an account type ===");
                        System.out.println("1. Client");
                        System.out.println("2. Instructor");
                        System.out.println("3. Admin");
                        input[0] = scanner.nextLine();
                        System.out.println("\nUsername:");
                        input[2] = scanner.nextLine();
                        System.out.println("\nPassword:");
                        input[3] = scanner.nextLine();
                        app.processInput(input);
                        break;
                    case 2:
                        input[1] = "register";
                        System.out.println("\n=== Select an account type ===");
                        System.out.println("1. Client");
                        System.out.println("2. Instructor");
                        choice = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("\nUsername:");
                        input[2] = scanner.nextLine();
                        System.out.println("\nPassword:");
                        input[3] = scanner.nextLine();
                        switch (choice){
                            case 1:
                                app.processInput(input);
                                break;
                            case 2:
                                System.out.println("\nPhone number:");
                                input[4] = scanner.nextLine();
                                System.out.println("\nSpecializations:");
                                input[5] = scanner.nextLine();
                                System.out.println("\n=== Cities ==="); //add print for cities
                                System.out.println("\nAvailabilities:");
                                input[6] = scanner.nextLine();
                                app.processInput(input);
                                break;
                        }
                        break;
                    case 3:
                        running = false;
                        System.out.println("Exiting system. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } else if (input[0].equals("Client")) {
                switch (choice) {
                    case 1:
                        input[1] = "view";
                        app.processInput(input);
                        break;
                    case 2:
                        input[1] = "book";
                        System.out.println("=== Select an offering to book ===");
                        input[2] = scanner.nextLine();
                        app.processInput(input);
                        break;
                    case 3:
                        input[1] = "viewbooking";
                        app.processInput(input);
                        break;
                    case 4:
                        input[1] = "cancel";
                        System.out.println("=== Select a booking to cancel ===");
                        input[2] = scanner.nextLine();
                        app.processInput(input);
                        break;
                    case 5:
                        logout();
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } else if (input[0].equals("instructor")) {
                switch (choice) {
                    case 1:
                        input[1] = "view";
                        app.processInput(input);
                        break;
                    case 2:
                        input[1] = "accept";
                        System.out.println("=== Select a lesson to accept ===");
                        input[2] = scanner.nextLine();
                        app.processInput(input);
                        break;
                    case 3:
                        logout();
                        input[0] = null;
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } else if (input[0].equals("Admin")) {
                switch (choice) {
                    case 1:
                        input[1] = "view";
                        app.processInput(input);
                        break;
                    case 2:
                        input[1] = "edit";
                        app.processInput(input);
                        break;
                    case 3:
                        input[1] = "delete";
                        app.processInput(input);
                        break;
                    case 4:
                        logout();
                        input[0] = null;
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        }
    scanner.close();
    }

    public static void displayMenu(String user) {
        
        if (user.equals("")) {
            System.out.println("\n=== Welcome to the Lesson Booking System ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
        } 
        else if (user.equals("Client")) {
            // Options for Clients
            System.out.println("\n=== Enter an integer to choose an option ===");
            System.out.println("1. View Offerings");
            System.out.println("2. Make a Booking");
            System.out.println("3. View My Bookings");
            System.out.println("4. Cancel a Booking");
            System.out.println("5. Logout");
        } 
        else if (user.equals("Instructor")) {
            // Options for Instructors
            System.out.println("\n=== Enter an integer to choose an option ===");
            System.out.println("1. View Available Offerings to Take");
            System.out.println("2. View Accepted Offerings");
            System.out.println("3. Logout");
        } 
        else if (user.equals("Admin")) {
            // Options for Admins
            System.out.println("\n=== Enter an integer to choose an option ===");
            System.out.println("1. View All Bookings");
            System.out.println("2. Edit Offerings");
            System.out.println("3. Delete User Account");
            System.out.println("4. Logout");
        }
        System.out.print("Enter your choice: ");
    }

    public static void logout(){
        System.out.println("Logging out...");
    }  
}
