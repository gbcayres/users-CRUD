package com.gb.javacrud.service;

import com.gb.javacrud.dao.IUserDAO;
import com.gb.javacrud.model.User;
import com.gb.javacrud.notification.Notification;
import com.gb.javacrud.validation.IUserValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserService implements iUserService {
    IUserDAO userDAO;
    IUserValidator validator;
    public UserService(IUserDAO userDAO, IUserValidator validator) {
        this.userDAO = userDAO;
        this.validator = validator;
    }

    @Override
    public void createUser(String name, String email, String password) {
        User newUser = new User(name, email, password);

        newUser.setPassword(hashPassword(password));
        userDAO.saveUser(newUser);
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public void removeUser(int userId) {
        userDAO.deleteUser(userId);
    }

    @Override
    public void updateUser(int userId, String fieldName, String newValue) {
        userDAO.updateUser(userId, fieldName, newValue);
    }

    @Override
    public User getUserbyId(int userId) {
        return userDAO.findById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.selectAllUsers();
    }
}
