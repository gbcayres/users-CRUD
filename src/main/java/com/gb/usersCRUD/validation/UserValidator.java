package com.gb.usersCRUD.validation;

import com.gb.usersCRUD.repository.UserRepository;
import com.gb.usersCRUD.dto.UserDTO;

public class UserValidator {
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUser(UserDTO user, Notification notification) {
        validateName(user.name(), notification);
        validateEmail(user.email(), notification);
        validatePassword(user.password(), notification);
    }

    public void validateName(String name, Notification notification) {
        if (name == null || name.isBlank()) {
            notification.addError("Name is required.");
            return;
        }

        if (nameIsInvalid(name)) {
            notification.addError("Invalid name.");
        }
    }

    private boolean nameIsInvalid(String name) {
        return containsDigit(name) || containsSpecialCharacter(name);
    }

    public void validateEmail(String email, Notification notification) {
        if (email == null) {
            notification.addError("E-mail is required.");
            return;
        }

        if (!emailIsValid(email)) {
            notification.addError("Invalid e-mail format.");
        }

        if (userRepository.emailInUse(email)) {
            notification.addError("E-mail already in use.");
        }
    }

    private boolean emailIsValid(String email) {
        return email.matches("^[a-zA-Z0-9._'-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    public void validatePassword(String password, Notification notification) {
        if (password == null) {
            notification.addError("Password is required.");
            return;
        }

        if (password.length() < 8) {
            notification.addError("Password must have at least 8 characters.");
        }

        if (!containsUpperCase(password)) {
            notification.addError("Password must have at least one upper case character.");
        }

        if (!containsDigit(password)) {
            notification.addError("Password must have at least one digit.");
        }

        if (!containsSpecialCharacter(password)) {
            notification.addError("Password must have at least one special character.");
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