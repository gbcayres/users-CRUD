package com.gb.javacrud.notification;

import java.util.ArrayList;
import java.util.List;

public class Notification implements iNotification {

    private final List<String> errors = new ArrayList<>();

    @Override
    public void addError(String message) {
        this.errors.add(message);
    }

    @Override
    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    @Override
    public List<String> getErrors() {
        return this.errors;
    }

    @Override
    public String errorMessage() {
        return this.errors.toString();
    }
}
