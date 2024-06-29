package com.example.server.handlers;

import com.example.server.utils.AuthUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.example.server.MyHttpServer.sendResponse;

public class ManageQuantityHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String token = AuthUtils.getTokenFromCookies(exchange);

        if (token == null || !AuthUtils.isValidToken(token)) {
            sendResponse(exchange, 403, "Forbidden");
            return;
        }

        if ("GET".equals(exchange.getRequestMethod())) {
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            byte[] response;
            if (pathParts.length == 3){
                String message = pathParts[2];
                response = generateManageGroupsPage(exchange, message);
            } else {
                response = Files.readAllBytes(Paths.get("src/pages/manage-quantity.html"));
            }
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }
    private byte[] generateManageGroupsPage(HttpExchange exchange, String message) throws IOException {
        // Read the HTML template from a file (replace "src/pages/manage-groups.html" with your actual path)
        String htmlTemplate = new String(Files.readAllBytes(Paths.get("src/pages/manage-quantity.html")), StandardCharsets.UTF_8);

        String formattedMessage = message.replace("+", " ");

        // Replace the placeholder in the template with the message
        String htmlResponse = htmlTemplate.replace("<div id=\"message\" class=\"message\"></div>", "<div id=\"message\" class=\"message\">" + formattedMessage + "</div>");

        // Convert the HTML string to bytes
        return htmlResponse.getBytes(StandardCharsets.UTF_8);
    }

}
