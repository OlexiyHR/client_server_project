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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.example.server.MyHttpServer.parseFormData;
import static com.example.server.MyHttpServer.sendResponse;


public class GroupPricesHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String token = AuthUtils.getTokenFromCookies(exchange);

        if (token == null || !AuthUtils.isValidToken(token)) {
            sendResponse(exchange, 403, "Forbidden");
            return;
        }

        if ("GET".equals(exchange.getRequestMethod())) {
            byte[] response = Files.readAllBytes(Paths.get("src/pages/group-prices.html"));
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
        if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> parameters = parseFormData(requestBody);
            String groupName = parameters.get("group_name");

            List<Good> goods = GoodDAO.getAllGoodsFromGroup(groupName);
            if (!goods.isEmpty()) {
                String response = generateResultPage(goods);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                String response = generateErrorPage(groupName);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(404, response.getBytes(StandardCharsets.UTF_8).length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            }

        }
    }

    private String generateResultPage (List < Good > goods) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='ua'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Результати пошуку</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f0f0f0; display: flex; flex-direction: column; align-items: center; margin: 0; }");
        html.append(".container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: center; margin-top: 20px; }");
        html.append("h1 { color: #333; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append(".btn { display: inline-block; background-color: #007bff; color: #fff; border: none; padding: 10px 20px; text-decoration: none; border-radius: 5px; cursor: pointer; font-size: 16px; margin: 10px; }");
        html.append(".btn:hover { background-color: #0056b3; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<h1>Результати пошуку</h1>");
        html.append("<table>");
        html.append("<tr><th>Назва</th><th>Загальна вартість</th></tr>");
        for (Good good : goods) {
            html.append("<tr>");
            html.append("<td>").append(good.getProduct_name()).append("</td>");
            BigDecimal totalValue = good.getPrice().multiply(BigDecimal.valueOf(good.getAmount()));
            html.append("<td>").append(totalValue).append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");
        html.append("<form method='GET' action='/menu'>");
        html.append("<button type='submit' class='btn'>Назад</button>");
        html.append("</form>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }

    private String generateErrorPage (String groupName) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='ua'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Результати пошуку</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f0f0f0; display: flex; flex-direction: column; align-items: center; margin: 0; }");
        html.append(".container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: center; margin-top: 20px; }");
        html.append("h1 { color: #333; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append(".btn { display: inline-block; background-color: #007bff; color: #fff; border: none; padding: 10px 20px; text-decoration: none; border-radius: 5px; cursor: pointer; font-size: 16px; margin: 10px; }");
        html.append(".btn:hover { background-color: #0056b3; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<h1>Результати пошуку</h1>");
        html.append("<div>");
        html.append("Немає товарів у цій групі, або не існує групи "+ groupName);
        html.append("</div>");
        html.append("<form method='GET' action='/menu'>");
        html.append("<button type='submit' class='btn'>Назад</button>");
        html.append("</form>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }
}
