package com.gb.usersCRUD.dao;

import com.gb.usersCRUD.model.User;

import java.util.List;

public interface IUserDAO {
    User saveUser(User user);
    User updateUser(int userId, User user);
    boolean deleteUser(int userId);
    User findById(int userId);
    List<User> selectAllUsers();
    boolean emailRegistered(String userEmail);
    boolean idExists(int userId);
}
