package com.example.server.handlers;

import com.example.database.Dao.GroupDAO;
import com.example.server.utils.AuthUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static com.example.server.MyHttpServer.parseFormData;
import static com.example.server.MyHttpServer.sendResponse;

public class DeleteGroupHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String token = AuthUtils.getTokenFromCookies(exchange);

        if (token == null || !AuthUtils.isValidToken(token)) {
            sendResponse(exchange, 403, "Forbidden");
            return;
        }

        if ("GET".equals(exchange.getRequestMethod())) {
            byte[] response = Files.readAllBytes(Paths.get("src/pages/delete-group.html"));
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
        if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> parameters = parseFormData(requestBody);
            String groupName = parameters.get("group_name");

            if (GroupDAO.deleteGroup(groupName)) {

                String message = "Видалено групу: " + groupName;
                exchange.getResponseHeaders().set("Location", "/manage-groups/" + URLEncoder.encode(message, StandardCharsets.UTF_8.toString()));
                exchange.sendResponseHeaders(302, -1);
            } else {
                String errorMessage = "Помилка: Групи не існує з іменнем " + groupName;
                exchange.getResponseHeaders().set("Location", "/manage-groups/" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                exchange.sendResponseHeaders(302, -1);
            }
        }
    }
}
