package com.gb.javacrud.notification;

import java.util.List;

public interface iNotification {
    void addError(String message);
    boolean hasErrors();
    List<String> getErrors();

    String errorMessage();
}
