package com.example.server.handlers;

import com.example.database.Dao.GoodDAO;
import com.example.models.Good;
import com.example.server.utils.AuthUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static com.example.server.MyHttpServer.parseFormData;
import static com.example.server.MyHttpServer.sendResponse;

public class EditGoodHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String token = AuthUtils.getTokenFromCookies(exchange);

        if (token == null || !AuthUtils.isValidToken(token)) {
            sendResponse(exchange, 403, "Forbidden");
            return;
        }

        if ("GET".equals(exchange.getRequestMethod())) {
            byte[] response = Files.readAllBytes(Paths.get("src/pages/edit-good.html"));
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
        if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> parameters = parseFormData(requestBody);
            String productName = parameters.get("product_name");
            Good existingGood = GoodDAO.getGood(productName);
            if (existingGood != null) {

                if (!parameters.get("new_name").isEmpty()) {
                    existingGood.setProduct_name(parameters.get("new_name"));
                }
                if (!parameters.get("description").isEmpty()) {
                    existingGood.setDescription(parameters.get("description"));
                }
                if (!parameters.get("producer").isEmpty()) {
                    existingGood.setProducer(parameters.get("producer"));
                }
                if (!parameters.get("price").isEmpty()) {
                    existingGood.setPrice(new BigDecimal(parameters.get("price")));
                }
                if (!parameters.get("group_name").isEmpty()) {
                    existingGood.setGroup_name(parameters.get("group_name"));
                }

                if (GoodDAO.updateGood(existingGood, productName)) {

                    String message = "Змінено товар: " + productName;
                    exchange.getResponseHeaders().set("Location", "/manage-goods/" + URLEncoder.encode(message, StandardCharsets.UTF_8.toString()));
                    exchange.sendResponseHeaders(302, -1);
                } else {
                    String errorMessage = "Помилка: Товар вже існує з іменнем " + parameters.get("new_name")  +" або не існує групи товарів "+ parameters.get("group_name");
                    exchange.getResponseHeaders().set("Location", "/manage-goods/" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                    exchange.sendResponseHeaders(302, -1);
                }

            } else {
                String errorMessage = "Помилка: Товар не знайдено з іменем " + productName;
                exchange.getResponseHeaders().set("Location", "/manage-goods/" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                exchange.sendResponseHeaders(302, -1);
            }
        }
    }
}
