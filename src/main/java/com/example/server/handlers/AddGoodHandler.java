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

public class AddGoodHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String token = AuthUtils.getTokenFromCookies(exchange);

        if (token == null || !AuthUtils.isValidToken(token)) {
            sendResponse(exchange, 403, "Forbidden");
            return;
        }

        if ("GET".equals(exchange.getRequestMethod())) {
            byte[] response = Files.readAllBytes(Paths.get("src/pages/add-good.html"));
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
        if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> parameters = parseFormData(requestBody);
            String product_name = parameters.get("product_name");
            String description = parameters.get("description");
            String producer = parameters.get("producer");
            BigDecimal price = new BigDecimal(parameters.get("price"));
            String group_name = parameters.get("group_name");

            Good good = new Good(product_name, description, producer, 0, price, group_name);
            if (GoodDAO.createGood(good)) {

                String message = "Додано продукт: " + product_name;
                exchange.getResponseHeaders().set("Location", "/manage-goods/" + URLEncoder.encode(message, StandardCharsets.UTF_8.toString()));
                exchange.sendResponseHeaders(302, -1);
            } else {
                String errorMessage = "Помилка: Продукт вже існує з іменнем " + product_name +" або не існує групи товарів "+ group_name;
                exchange.getResponseHeaders().set("Location", "/manage-goods/" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
                exchange.sendResponseHeaders(302, -1);
            }
        }
    }
}

