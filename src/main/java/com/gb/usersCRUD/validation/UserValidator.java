package com.gb.usersCRUD.validation;

import com.gb.usersCRUD.dao.IUserDAO;
import com.gb.usersCRUD.model.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UserValidator {
    private final IUserDAO userDAO;

    public UserValidator(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void validateName(String name, Notification notification) {
        if (name == null || name.isEmpty()) {
            notification.addError("name is required.");
        }

        if (nameIsInvalid(name)) {
            notification.addError("invalid name.");
        }
    }

    public void validateUser(User user, Notification notification) {
        validateName(user.getName(), notification);
        validateEmail(user.getEmail(), notification);
        validatePassword(user.getPassword(), notification);
    }

    private boolean nameIsInvalid(String name) {
        return containsDigit(name) || containsSpecialCharacter(name);
    }

    public void validateId(int id, Notification notification) {
        if (id <= 0) {
            notification.addError("invalid id.");
            return;
        }

        if (!this.userDAO.idExists(id)) {
            notification.addError("user id not found.");
        }
    }

    public void validateEmail(String email, Notification notification) {
        if (email == null || email.isEmpty()) {
            notification.addError("e-mail is required.");
            return;
        }


        if (!emailIsValid(email)) {
            notification.addError("invalid e-mail format.");
        }

        if (emailInUse(email)) {
            notification.addError("e-mail already in use.");
        }
    }

    private boolean emailIsValid(String email) {
        return email.matches("^[a-zA-Z0-9._'-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean emailInUse(String email) {
        return userDAO.emailRegistered(email);
    }

    public void validatePassword(String password, Notification notification) {
        if (password.length() < 8) {
            notification.addError("\npassword must have at least 8 characters.\n");
        }

        if (!containsUpperCase(password)) {
            notification.addError("\npassword must have at least one upper case character.\n");
        }

        if (!containsDigit(password)) {
            notification.addError("\npassword must have at least one digit.\n");
        }

        if (!containsSpecialCharacter(password)) {
            notification.addError("\npassword must have at least one special character.\n");
        }
    }

    private boolean containsUpperCase(String password) {
        return password.matches("(?=.*[A-Z]).*");
    }

    private boolean containsDigit(String password) {
        return password.matches("(?=.*[0-9]).*");
    }

    private boolean containsSpecialCharacter(String password) {
        return password.matches("(?=.*[!@#$%^&*()\\-+]).*");
    }
}