package com.gb.javacrud.controller;

import com.gb.javacrud.model.User;
import com.gb.javacrud.notification.Notification;
import com.gb.javacrud.service.UserService;
import com.gb.javacrud.validation.UserValidator;

import java.util.List;
import java.util.Scanner;

public class UserController  implements IUserController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final Scanner scanner;

    public UserController(UserService userService, UserValidator validator) {
        this.userService = userService;
        this.userValidator = validator;
        this.scanner = new Scanner(System.in);
    }

    public void registerUser() {
                System.out.println("-------------------");
                System.out.println(" User Registration ");
                System.out.println("-------------------");

                String name = askUserName();
                String email = askUserEmail();
                String password = askUserPassword();

            try {
                userService.createUser(name, email, password);
                System.out.println("User created successfully!");
                waitEnter(scanner);
            } catch (IllegalArgumentException e) {
                System.err.println("error registering user: \n" + e.getMessage());
            }
    }

    public void removeUser() {
        System.out.println("-------------------");
        System.out.println("    User Removal   ");
        System.out.println("-------------------");

        int userId = askUserId();

        try {
            userService.removeUser(userId);
            System.out.println("User removed successfully!");
            waitEnter(scanner);
        } catch (IllegalArgumentException e) {
            System.err.println("error removing user: \n" + e.getMessage());
        }
    }

    public void updateUser() {
        System.out.println("-------------------");
        System.out.println("    User Update    ");
        System.out.println("-------------------");

        int userId = askUserId();
        String field = askUpdateField();
        String newValue = askUpdateValue(field);

        try {
            userService.updateUser(userId, field, newValue);
            System.out.println("User updated successfully!");
            waitEnter(scanner);
        } catch (IllegalArgumentException e) {
            System.err.println("error updating user: \n" + e.getMessage());
        }
    }

    public void searchUser() {
        System.out.println("-------------------");
        System.out.println("    User Search    ");
        System.out.println("-------------------");

        int userId = askUserId();

        try {
            User user = userService.getUserbyId(userId);
            System.out.println("User found!");
            displayUser(user);
            waitEnter(scanner);
        } catch (IllegalArgumentException e) {
            System.err.println("error updating user: \n" + e.getMessage());
        }
    }

    public void displayRegisteredUsers() {
        System.out.println("------------------------");
        System.out.println("    Registered Users    ");
        System.out.println("------------------------");

        try {
            List<User> users = userService.getAllUsers();
            users.forEach(this::displayUser);
            waitEnter(scanner);
        } catch (IllegalArgumentException e) {
            System.err.println("error updating user: \n" + e.getMessage());
        }
    }

    private void displayUser(User user) {
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("E-mail: " + user.getEmail());
        System.out.println("---------------------");
    }
    private int askUserId() {
        int id;
        Notification notification;

        do {
            notification = new Notification();

            System.out.print("ID: ");
            id = scanner.nextInt();
            scanner.nextLine();

            this.userValidator.validateId(id, notification);

            if (notification.hasErrors()) {
                System.out.println("error:\n" + notification.errorMessage() + "\nplease, try again.\n");
            }
        } while (notification.hasErrors());

        return id;
    }

    private String askUserName() {
        String name;
        Notification notification;

        do {
            notification = new Notification();

            System.out.print("Name: ");
            name = scanner.nextLine();

            this.userValidator.validateName(name, notification);

            if (notification.hasErrors()) {
                System.out.println("error:\n" + notification.errorMessage() + "\nplease, try again.\n");
            }
        } while (notification.hasErrors());

        return name;
    }

    private String askUserEmail() {
        String email;
        Notification notification;

        do {
            notification = new Notification();

            System.out.print("E-mail: ");
            email = scanner.nextLine();

            this.userValidator.validateEmail(email, notification);

            if (notification.hasErrors()) {
                System.out.println("error:\n" + notification.errorMessage() + "\nplease, try again.\n");
            }
        } while (notification.hasErrors());

        return email;
    }

    private String askUserPassword() {
        String password;
        Notification notification;

        do {
            notification = new Notification();

            System.out.print("Password: ");
            password = scanner.nextLine();

            this.userValidator.validatePassword(password, notification);

            if (notification.hasErrors()) {
                System.out.println("error:\n" + notification.errorMessage() + "\nplease, try again.\n");
            }
        } while (notification.hasErrors());

        return password;
    }

    private String askUpdateField() {
        String field;
        Notification notification;

        do {
            int choice;
            notification = new Notification();

            showUpdateOptions();
            choice = scanner.nextInt();
            scanner.nextLine();
            field = handleFieldChoice(choice);

            this.userValidator.validateFieldName(field, notification);

            if (notification.hasErrors()) {
                System.out.println("error:\n" + notification.errorMessage() + "\nplease, try again.\n");
            }

        } while (notification.hasErrors());

        return field;
    }

    private void showUpdateOptions() {
        System.out.println("Which field we will update?");
        System.out.println("---------------------------");
        System.out.println("1.Name");
        System.out.println("2.E-mail");
        System.out.println("3.Password");
    }

    private String handleFieldChoice(int choice) {
        return switch (choice) {
            case 1 -> "name";
            case 2 -> "email";
            case 3 -> "password";
            default -> null;
        };
    }

    private String askUpdateValue(String updateField) {
        String value;
        Notification notification;

        do {
            notification = new Notification();

            System.out.println("New " + updateField + ": ");
            value = scanner.nextLine();

            this.userValidator.validateFieldValue(updateField, value, notification);

            if (notification.hasErrors()) {
                System.out.println("error:\n" + notification.errorMessage() + "\nplease, try again.\n");
            }

        } while (notification.hasErrors());

        return value;
    }

    private void waitEnter(Scanner scanner) {
        System.out.println("Press ENTER to continue...");
        scanner.nextLine();
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }

}
