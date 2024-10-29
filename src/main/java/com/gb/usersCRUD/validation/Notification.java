package com.gb.usersCRUD.validation;

import java.util.ArrayList;
import java.util.List;

public class Notification {

    private final List<String> errors = new ArrayList<>();

    public void addError(String message) {
        this.errors.add(message);
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    public String[] getErrors() {
        return errors.toArray(String[]::new);
    }

    public String errorMessage() {
        return this.errors.toString();
    }
}
