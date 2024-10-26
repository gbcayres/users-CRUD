package com.gb.usersCRUD.service;

import com.gb.usersCRUD.dto.UserDTO;
import com.gb.usersCRUD.model.User;

import java.util.List;

public interface IUserService {
    UserDTO createUser(String name, String email, String password);
    boolean removeUser(int userId);
    UserDTO updateUser(int userId, User updatedUser);
    UserDTO getUserById(int userId);
    List<UserDTO> getAllUsers();
}