package com.example.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())&& "/".equals(exchange.getRequestURI().getPath())) {
            String expiredCookie = "token=; HttpOnly; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT";
            exchange.getResponseHeaders().add("Set-Cookie", expiredCookie);
            byte[] response = Files.readAllBytes(Paths.get("src/pages/main.html"));
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }
}
