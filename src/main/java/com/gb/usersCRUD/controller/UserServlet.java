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

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

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
            int userId = getUserIdFromRequestPath(request);
            UserDTO user = userService.getUserById(userId);

            ResponseWriter.write(response, HttpServletResponse.SC_OK, "User found", user);
        } catch (IllegalArgumentException e) {
            ResponseWriter.write(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Failed to get user: " + e.getMessage(),
                    null
            );
        } catch (Exception e) {
            ResponseWriter.write(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to get user: " + e.getMessage(),
                    null
            );
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
           int userId = getUserIdFromRequestPath(request);
           User newUser = gson.fromJson(request.getReader(), User.class);
           UserDTO updatedUser = userService.updateUser(userId, newUser);

           ResponseWriter.write(response, HttpServletResponse.SC_OK, "User updated successfully", updatedUser);
       } catch (IllegalArgumentException e) {
           ResponseWriter.write(
                   response,
                   HttpServletResponse.SC_BAD_REQUEST,
                   "Failed to update user: " + e.getMessage(),
                   null
           );
       } catch (Exception e) {
           ResponseWriter.write(
                   response,
                   HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                   "Failed to update user: " + e.getMessage(),
                   null
           );
       }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userId = getUserIdFromRequestPath(request);
            boolean result = userService.removeUser(userId);

            ResponseWriter.write(response, HttpServletResponse.SC_OK, "User deleted successfully", result);
        } catch (IllegalArgumentException e) {
            ResponseWriter.write(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Failed to delete user: " + e.getMessage(),
                    null
            );
        } catch (Exception e) {
            ResponseWriter.write(
                    response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to delete user: " + e.getMessage(),
                    null
            );
        }
    }

    private int getUserIdFromRequestPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return Integer.parseInt(pathInfo.substring(1));
    }
}
