package com.gb.javacrud.validation;

import com.gb.javacrud.model.User;
import com.gb.javacrud.notification.Notification;

public interface IUserValidator {
    void validateUser(User user, Notification notification);
    void validateId(int userId, Notification notification);
    void validateFieldName(String field, Notification notification);
    void validateFieldValue(String field, String value, Notification notification);
}
