package com.gb.javacrud.controller;

import com.gb.javacrud.model.User;

import java.util.List;

public interface IUserController {
    void registerUser();
    void removeUser();
    void updateUser();
    void searchUser();
    void displayRegisteredUsers();
}
