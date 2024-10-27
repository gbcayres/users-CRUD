package com.gb.usersCRUD.service;

import com.gb.usersCRUD.dao.UserDAO;
import com.gb.usersCRUD.dto.UserDTO;
import com.gb.usersCRUD.model.User;
import com.gb.usersCRUD.validation.Notification;
import com.gb.usersCRUD.validation.UserValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final UserDAO userDAO;
    private final UserValidator userValidator;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.userValidator = new UserValidator(userDAO);
    }

    public UserDTO createUser(String name, String email, String password) {
        User newUser = new User(name, email, password);
        Notification notification = new Notification();

        userValidator.validateUser(newUser, notification);
        if (notification.hasErrors()) {
            throw new IllegalArgumentException(notification.errorMessage());
        }

        newUser.setPassword(hashPassword(password));
        User user = userDAO.saveUser(newUser);

        return mapUserToDTO(user);
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public boolean removeUser(int userId) {
        Notification notification =  new Notification();

        userValidator.validateId(userId, notification);
        if (notification.hasErrors()) {
            throw new IllegalArgumentException(notification.errorMessage());
        }

        return userDAO.deleteUser(userId);
    }


    public UserDTO updateUser(int userId, User updatedUser) {
        Notification notification =  new Notification();

        userValidator.validateUser(updatedUser, notification);
        if (notification.hasErrors()) {
            throw new IllegalArgumentException(notification.errorMessage());
        }

        User user = userDAO.updateUser(userId, updatedUser);

        return mapUserToDTO(user);
    }

    public UserDTO getUserById(int userId) {
        Notification notification =  new Notification();

        userValidator.validateId(userId, notification);
        if (notification.hasErrors()) {
            throw new IllegalArgumentException(notification.errorMessage());
        }

        User user = userDAO.findById(userId);

        return mapUserToDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userDAO.selectAllUsers();
        return users.stream()
                .map(this::mapUserToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO mapUserToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), formatDate(user.getCreatedAt()));
    }

    private String formatDate(Instant createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:ss").withZone(ZoneId.systemDefault());
        return formatter.format(createdAt);
    }
}
