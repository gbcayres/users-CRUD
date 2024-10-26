package com.gb.usersCRUD.helpers;

import com.gb.usersCRUD.dto.Response;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseWriter {
    private static final Gson gson = new Gson();

    public static void write(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode);

        Response<Object> formattedResponse = new Response<>(statusCode, message, data);
        response.getWriter().write(gson.toJson(formattedResponse));
    }
}
