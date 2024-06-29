package com.example.server.handlers;

import com.example.database.Dao.UserDAO;
import com.example.models.User;
import com.example.server.utils.AuthUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.example.server.MyHttpServer.parseFormData;
import static com.example.server.MyHttpServer.sendResponse;

public class LoginHandler implements HttpHandler {

        private ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            if ("GET".equals(exchange.getRequestMethod())) {
                byte[] response = Files.readAllBytes(Paths.get("src/pages/login.html"));
                exchange.sendResponseHeaders(200, response.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }
            if (exchange.getRequestMethod().equals("POST")) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> parameters = parseFormData(requestBody);
                String username = parameters.get("username");
                String password = parameters.get("password");

                try {
                    String hashedPassword = hashPassword(password);
                    if (isValidLogin(username, hashedPassword)) {
                        String token = AuthUtils.generateToken(username);
                        String cookie = String.format("token=%s; HttpOnly; Path=/", token);
                        exchange.getResponseHeaders().add("Set-Cookie", cookie);
                        AuthUtils.storeToken(username, token);

                        exchange.getResponseHeaders().set("Location", "/menu");
                        exchange.sendResponseHeaders(302, -1);
                    } else {
                        sendResponse(exchange, 401, "Unauthorized");

                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    sendResponse(exchange, 500, "Internal Server Error");
                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        }


        private boolean isValidLogin(String login, String password) {
            User user = UserDAO.getUserByLogin(login);
            return user != null && user.getPassword().equals(password);
        }

        private static String hashPassword(String password) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }



    }
