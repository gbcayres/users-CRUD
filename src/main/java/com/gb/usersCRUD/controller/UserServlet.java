package com.gb.usersCRUD.controller;

import com.gb.usersCRUD.helpers.ResponseHandler;
import com.gb.usersCRUD.model.User;
import com.gb.usersCRUD.repository.UserRepository;
import com.gb.usersCRUD.db.DatabaseConnector;
import com.gb.usersCRUD.dto.UserDTO;
import com.gb.usersCRUD.helpers.InstantAdapter;
import com.gb.usersCRUD.service.UserService;
import com.gb.usersCRUD.validation.UserNotFoundException;
import com.gb.usersCRUD.validation.UserValidationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@WebServlet("/v1/users/*")
public class UserServlet extends HttpServlet {

    private UserService userService;
    private Gson gson;
    @Override
    public void init() throws ServletException {
        DatabaseConnector connector = new DatabaseConnector("db.properties");
        UserRepository userRepository = new UserRepository(connector);
        userService = new UserService(userRepository);

        gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UUID userId = getUserIdFromRequestPath(request);

            User user = userService.getUserById(userId);

            ResponseHandler.ok(response, user);
        } catch (UserNotFoundException e) {
            ResponseHandler.notFound(response);
        } catch (Exception e) {
            ResponseHandler.internalServerError(response, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
           UUID userId = getUserIdFromRequestPath(request);

           UserDTO newUser = gson.fromJson(request.getReader(), UserDTO.class);

           userService.updateUser(userId, newUser);

           ResponseHandler.noContent(response);
       } catch (UserNotFoundException e) {
           ResponseHandler.notFound(response);
       } catch (UserValidationException e) {
           ResponseHandler.badRequest(response, e.getErrorMessages());
       } catch (Exception e) {
           ResponseHandler.internalServerError(response, e.getMessage());
       }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UUID userId = getUserIdFromRequestPath(request);

            userService.removeUser(userId);

            ResponseHandler.noContent(response);
        } catch (UserNotFoundException e) {
            ResponseHandler.notFound(response);
        } catch (Exception e) {
            ResponseHandler.internalServerError(response, e.getMessage());
        }
    }

    private UUID getUserIdFromRequestPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return UUID.fromString(pathInfo.substring(1));
    }
}
