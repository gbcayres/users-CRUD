package com.gb.usersCRUD.validation;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        super("User with ID " + userId.toString() + " not found.");
    }
}