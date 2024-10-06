package com.gb.javacrud.service;

import com.gb.javacrud.model.User;

import java.util.List;

public interface iUserService {
    void createUser(String name, String email, String password);
    void removeUser(int userId);
    void updateUser(int userId, String columnName, String newValue);
    User getUserbyId(int userId);
    List<User> getAllUsers();
}