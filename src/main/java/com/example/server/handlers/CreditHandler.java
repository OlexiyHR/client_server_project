package com.example.server.handlers;

import com.example.database.Dao.GoodDAO;
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


public class CreditHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String token = AuthUtils.getTokenFromCookies(exchange);

        if (token == null || !AuthUtils.isValidToken(token)) {
            sendResponse(exchange, 403, "Forbidden");
            return;
        }

        if ("GET".equals(exchange.getRequestMethod())) {
            byte[] response = Files.readAllBytes(Paths.get("src/pages/credit.html"));
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
        if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> parameters = parseFormData(requestBody);
            String productName = parameters.get("product_name");

            try {
                int amount = Integer.parseInt(parameters.get("amount"));


                if (GoodDAO.credit(productName, amount)) {

                    String message = "Прийшло на склад: " + productName + " - штук " + amount;
                    exchange.getResponseHeaders().set("Location", "/manage-quantity/" + URLEncoder.encode(message, StandardCharsets.UTF_8.toString()));
                    exchange.sendResponseHeaders(302, -1);
                } else {
                    String errorMessage = "Помилка: Товару не існує з іменнем " + productName;
                    exchange.getResponseHeaders().set("Location", "/manage-quantity/" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                    exchange.sendResponseHeaders(302, -1);
                }
            } catch (NumberFormatException e) {
                String errorMessage = "Помилка: Некоректне значення кількості товару.";
                exchange.getResponseHeaders().set("Location", "/manage-quantity/" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                exchange.sendResponseHeaders(302, -1);
            }
        }
    }
}
