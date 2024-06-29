package com.example.server.handlers;

import com.example.server.utils.AuthUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.example.server.MyHttpServer.sendResponse;

public class MenuHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String token = AuthUtils.getTokenFromCookies(exchange);

        if (token == null || !AuthUtils.isValidToken(token)) {
            sendResponse(exchange, 403, "Forbidden");
            return;
        }

        if ("GET".equals(exchange.getRequestMethod())) {
            byte[] response = Files.readAllBytes(Paths.get("src/pages/menu.html"));
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }
}

