package com.gb.javacrud;

import com.gb.javacrud.controller.UserController;
import com.gb.javacrud.dao.UserDAO;
import com.gb.javacrud.db.DatabaseConfig;
import com.gb.javacrud.db.PostgresConnector;
import com.gb.javacrud.service.UserService;
import com.gb.javacrud.validation.UserValidator;

import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        DatabaseConfig config = new DatabaseConfig("db.properties");
        PostgresConnector connector = new PostgresConnector(config);
        UserDAO userDao = new UserDAO(connector);
        UserValidator userValidator = new UserValidator(userDao);
        UserService userService = new UserService(userDao, userValidator);
        UserController userController = new UserController(userService, userValidator);

        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        do {
            displayHeader();
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> userController.registerUser();
                case 2 -> userController.removeUser();
                case 3 -> userController.updateUser();
                case 4 -> userController.searchUser();
                case 5 -> userController.displayRegisteredUsers();
                case 6 -> {
                    System.out.print("Exiting Java CRUD app...");
                    running = false;
                }
                default -> {
                    System.out.print("invalid option, try again.");
                    return;
                }

            }
        } while (running);
    }

    public static void displayHeader() {
        System.out.println("-------------------------");
        System.out.println("      JAVA CRUD MENU     ");
        System.out.println("-------------------------");
    }

    public static void displayMenu() {
        System.out.println("1. Register User");
        System.out.println("2. Remove User");
        System.out.println("3. Update User");
        System.out.println("4. Search User");
        System.out.println("5. Display Registered User");
        System.out.println("6. Exit");
        System.out.println("-------------------------");
    }
}