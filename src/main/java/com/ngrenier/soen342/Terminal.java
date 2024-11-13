package com.ngrenier.soen342;
import java.util.Scanner;

public class Terminal {
    static App app = new App();
    public static void main(String[] args) {
        String[] input = new String[10];
        input[0] = "";
        Scanner scanner = new Scanner(System.in);
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
                        processInput(input);
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
                                processInput(input);
                                break;
                            case 2:
                                System.out.println("\nPhone number:");
                                input[4] = scanner.nextLine();
                                System.out.println("\nSpecializations:");
                                input[5] = scanner.nextLine();
                                System.out.println("\n=== Cities ==="); //add print for cities
                                System.out.println("\nAvailabilities:");
                                input[6] = scanner.nextLine();
                                processInput(input);
                                break;
                        }
                        break;
                    case 3:
                        input[1] = "view";
                        processInput(input);
                        break;
                    case 4:
                        running = false;
                        System.out.println("Exiting system. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } else if (input[0].equals("1")) {
                switch (choice) {
                    case 1:
                        input[1] = "view";
                        processInput(input);
                        break;
                    case 2:
                        input[1] = "book";
                        System.out.println("=== Select an offering to book ===");
                        input[2] = scanner.nextLine();
                        processInput(input);
                        break;
                    case 3:
                        input[1] = "viewbooking";
                        processInput(input);
                        break;
                    case 4:
                        input[1] = "cancel";
                        System.out.println("=== Select a booking to cancel ===");
                        input[2] = scanner.nextLine();
                        processInput(input);
                        break;
                    case 5:
                        logout();
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } else if (input[0].equals("2")) {
                switch (choice) {
                    case 1:
                        input[1] = "view";
                        processInput(input);
                        break;
                    case 2:
                        input[1] = "accept";
                        System.out.println("=== Select a lesson to accept ===");
                        input[2] = scanner.nextLine();
                        processInput(input);
                        break;
                    case 3:
                        logout();
                        input[0] = null;
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } else if (input[0].equals("3")) {
                switch (choice) {
                    case 1:
                        input[1] = "view";
                        processInput(input);
                        break;
                    case 2:
                        input[1] = "edit";
                        processInput(input);
                        break;
                    case 3:
                        input[1] = "delete";
                        processInput(input);
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
            System.out.println("3. View Offerings");
            System.out.println("4. Exit");
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
    public static void processInput(String[] input) {
        String userType = input[0];
        switch (userType) {
            case "1":
                processClientOperation(input);
                break;

            case "2":
                processInstructorOperation(input);
                break;

            case "3":
                processAdminOperation(input);
                break;

            case "":
                processPublicOperation(input);
                break;

            default:
                System.out.println("Invalid user type. Please try again.");
        }
    }

    // input[2] contains an integer which represents the offering to book or cancel (chosen from a list)
    public static void processClientOperation(String[] input) {
        String operation = input[1];
        switch (operation) {
            case "view":
                app.viewClientOfferings();
                break;
            case "book":
                app.bookClientOffering(input[2]);
                break;
            case "viewbooking":
                app.viewClientBookings();
                break;
            case "cancel":
                app.cancelClientBooking(input[2]);
                break;
            default:
                System.out.println("Invalid operation for Client. Please try again.");
        }
    }

    // input[2] contains an integer which represents the lesson to accept (chosen from a list)
    public static void processInstructorOperation(String[] input) {
        String operation = input[1];
        switch (operation) {
            case "view":
                app.viewInstructorOfferings();
                break;
            case "accept":
                app.acceptInstructorLesson(input[2]);
                break;
            default:
                System.out.println("Invalid operation for Instructor. Please try again.");
        }
    }

    // input[2] contains an integer which represents the user to delete (chosen from a list)
    public static void processAdminOperation(String[] input) {
        String operation = input[1];
        switch (operation) {
            case "view":
                app.viewAllBookings();
                break;
            case "edit":
                app.editOfferings(input);
                break;
            case "delete":
                app.deleteUser(input[2]);
                break;
            default:
                System.out.println("Invalid operation for Admin. Please try again.");
        }
    }

    public static void processPublicOperation(String[] input) {
        String operation = input[1];
        switch (operation) {
            case "login":
                break;
            case "register":
                break;
            default:
                System.out.println("Invalid operation for Public. Please try again.");
        }
    }

    public static void logout(){
        System.out.println("Logging out...");
    }  
}
