package com.gb.usersCRUD.validation;

public class UserValidationException extends RuntimeException {
    private final String[] errorMessages;

    public UserValidationException(String[] errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String[] getErrorMessages() {
        return errorMessages;
    }
}
