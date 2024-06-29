package com.example.server;

import com.example.server.handlers.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class MyHttpServer {
    private static final int PORT = 8080;

    public static void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", new MainHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/menu", new MenuHandler());
        server.createContext("/manage-groups", new ManageGroupsHandler());
        server.createContext("/add-group", new AddGroupHandler());
        server.createContext("/edit-group", new EditGroupHandler());
        server.createContext("/delete-group", new DeleteGroupHandler());
        server.createContext("/manage-goods", new ManageGoodsHandler());
        server.createContext("/add-good", new AddGoodHandler());
        server.createContext("/edit-good", new EditGoodHandler());
        server.createContext("/delete-good", new DeleteGoodHandler());
        server.createContext("/manage-quantity", new ManageQuantityHandler());
        server.createContext("/credit", new CreditHandler());
        server.createContext("/write-off", new WriteOffHandler());
        server.createContext("/search-good", new SearchGoodHandler());
        server.createContext("/statistics", new StatisticsHandler());
        server.createContext("/all-goods", new AllGoodsHandler());
        server.createContext("/group-goods", new GroupGoodsHandler());
        server.createContext("/good-prices", new GoodPricesHandler());
        server.createContext("/group-prices", new GroupPricesHandler());

        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    public static void main(String[] args) throws Exception {
        startServer();
    }

    public static void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, body.getBytes(StandardCharsets.UTF_8).length);
        exchange.getResponseBody().write(body.getBytes(StandardCharsets.UTF_8));
        exchange.getResponseBody().close();
    }

    public static Map<String, String> parseFormData(String formData) throws IOException {
        Map<String, String> result = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            result.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return result;
    }
}
