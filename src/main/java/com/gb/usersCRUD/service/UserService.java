package com.gb.usersCRUD.service;

import com.gb.usersCRUD.repository.UserRepository;
import com.gb.usersCRUD.dto.UserDTO;
import com.gb.usersCRUD.model.User;
import com.gb.usersCRUD.validation.Notification;
import com.gb.usersCRUD.validation.UserNotFoundException;
import com.gb.usersCRUD.validation.UserValidationException;
import com.gb.usersCRUD.validation.UserValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userValidator = new UserValidator(userRepository);
    }

    public UUID createUser(UserDTO createUserDTO) {
        Notification notification = new Notification();

        userValidator.validateUser(createUserDTO, notification);
        if (notification.hasErrors()) {
            throw new UserValidationException(notification.getErrors());
        }

        User user = new User(
                UUID.randomUUID(),
                createUserDTO.name(),
                createUserDTO.email(),
                createUserDTO.password(),
                Instant.now(),
                null
        );

        String passwordHash = hashPassword(user.getPassword());
        user.setPassword(passwordHash);

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void updateUser(UUID userId, UserDTO updateUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Notification notification = new Notification();

        User updatedUser = validateFieldsAndUpdateUser(user, updateUserDTO, notification);
        if (notification.hasErrors()) {
            throw new UserValidationException(notification.getErrors());
        }

        userRepository.update(userId, updatedUser);
    }

    private User validateFieldsAndUpdateUser(User user, UserDTO updateUserDTO, Notification notification) {
        if (updateUserDTO.name() != null) {
            userValidator.validateName(updateUserDTO.name(), notification);
            user.setName(updateUserDTO.name());
        }
        if (updateUserDTO.email() != null) {
            userValidator.validateEmail(updateUserDTO.email(), notification);
            user.setEmail(updateUserDTO.email());
        }
        if (updateUserDTO.password() != null) {
            userValidator.validatePassword(updateUserDTO.password(), notification);

            String newPasswordHash = hashPassword(updateUserDTO.password());
            user.setPassword(newPasswordHash);
        }

        user.setUpdatedAt(Instant.now());

        return user;
    }

    public void removeUser(UUID userId) {
        boolean userExists = userRepository.idExists(userId);

        if (!userExists) {
            throw new UserNotFoundException(userId);
        }

        userRepository.delete(userId);
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }
}