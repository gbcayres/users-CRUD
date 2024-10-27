package com.gb.usersCRUD.validation;

import com.gb.usersCRUD.dao.UserDAO;
import com.gb.usersCRUD.model.User;

public class UserValidator {
    private final UserDAO userDAO;

    public UserValidator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void validateUser(User user, Notification notification) {
        validateName(user.getName(), notification);
        validateEmail(user.getEmail(), notification);
        validatePassword(user.getPassword(), notification);
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

    public void validateName(String name, Notification notification) {
        if (name == null || name.isBlank()) {
            notification.addError("name is required.");
            return;
        }

        if (nameIsInvalid(name)) {
            notification.addError("invalid name.");
        }
    }

    private boolean nameIsInvalid(String name) {
        return containsDigit(name) || containsSpecialCharacter(name);
    }

    public void validateEmail(String email, Notification notification) {
        if (email == null || email.isBlank()) {
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
            notification.addError("password must have at least 8 characters.");
        }

        if (!containsUpperCase(password)) {
            notification.addError("password must have at least one upper case character.");
        }

        if (!containsDigit(password)) {
            notification.addError("password must have at least one digit.");
        }

        if (!containsSpecialCharacter(password)) {
            notification.addError("password must have at least one special character.");
        }
    }

    private boolean containsUpperCase(String password) {
        return password.matches("(?=.*[A-Z]).*");
    }

    private boolean containsDigit(String password) {
        return password.matches("(?=.*[0-9]).*");
    }

    private boolean containsSpecialCharacter(String password) {
        return password.matches(".*[!@#$%^&*()\\-+].*");
    }
}