package com.gb.javacrud.dao;

import com.gb.javacrud.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    boolean saveUser(User user);
    boolean updateUser(int userId, String columnName, String newValue);
    boolean deleteUser(int userId);
    User findById(int userId);
    List<User> selectAllUsers();
    boolean emailRegistered(String userEmail);
    boolean idExists(int userId);
}
