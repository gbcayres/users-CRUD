package com.gb.usersCRUD.controller;

import com.gb.usersCRUD.helpers.ResponseHandler;
import com.gb.usersCRUD.repository.UserRepository;
import com.gb.usersCRUD.db.DatabaseConnector;
import com.gb.usersCRUD.dto.UserDTO;
import com.gb.usersCRUD.helpers.InstantAdapter;
import com.gb.usersCRUD.model.User;
import com.gb.usersCRUD.service.UserService;
import com.gb.usersCRUD.validation.UserValidationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@WebServlet("/v1/users")
public class UsersServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UserDTO newUser = gson.fromJson(request.getReader(), UserDTO.class);

            UUID userId = userService.createUser(newUser);

            String location = String.format("%s/users/%s", request.getRequestURL().toString(), userId.toString());

            ResponseHandler.created(response, URI.create(location));
        } catch (UserValidationException e) {
            ResponseHandler.badRequest(response, e.getErrorMessages());
        } catch (Exception e) {
            ResponseHandler.internalServerError(response, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<User> users = userService.listUsers();

            ResponseHandler.ok(response, users);
        } catch (Exception e) {
            ResponseHandler.internalServerError(response, e.getMessage());
        }
    }

}
