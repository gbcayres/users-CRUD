package com.gb.usersCRUD.controller;

import com.gb.usersCRUD.helpers.ResponseWriter;
import com.gb.usersCRUD.dao.UserDAO;
import com.gb.usersCRUD.db.DatabaseConnector;
import com.gb.usersCRUD.dto.UserDTO;
import com.gb.usersCRUD.model.User;
import com.gb.usersCRUD.service.UserService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UsersServlet extends HttpServlet {
    private UserService userService;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        DatabaseConnector connector = new DatabaseConnector("db.properties");
        UserDAO userDAO = new UserDAO(connector);
        userService = new UserService(userDAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<UserDTO> users = userService.getAllUsers();
            ResponseWriter.write(response, HttpServletResponse.SC_OK, "Users found successfully", users);
        } catch (Exception e) {
            ResponseWriter.write(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "ERROR getting users:\n" + e.getMessage(),
                    null
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User newUser = gson.fromJson(request.getReader(), User.class);
            UserDTO savedUser = userService.createUser(newUser.getName(), newUser.getEmail(), newUser.getPassword());

            ResponseWriter.write(response, HttpServletResponse.SC_CREATED, "User found successfully", savedUser);
        } catch (IllegalArgumentException e) {
            ResponseWriter.write(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ERROR creating user: " + e.getMessage(),
                    null
            );
        } catch (Exception e) {
            ResponseWriter.write(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "ERROR creating user: " + e.getMessage(),
                    null
            );
        }
    }
}
