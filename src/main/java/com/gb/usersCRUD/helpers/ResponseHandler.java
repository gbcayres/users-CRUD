package com.gb.usersCRUD.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Map;

public class ResponseHandler {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .create();

    public static <T> void ok(HttpServletResponse response, T body) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(gson.toJson(body));
    }

    public static <T> void created(HttpServletResponse response, URI location) throws IOException {
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader("Location", location.toString());
    }

    public static <T> void noContent(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    public static <T> void notFound(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(gson.toJson("{\"error\": \"resource not found\"}"));
    }

    public static <T> void badRequest(HttpServletResponse response, String[] errorMessages) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(gson.toJson(Map.of("errors", errorMessages)));
    }

    public static <T> void internalServerError(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(gson.toJson(Map.of("error", errorMessage)));
    }
}
