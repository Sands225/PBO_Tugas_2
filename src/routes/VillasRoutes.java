package routes;

import handlers.VillasHandler;
import models.Villa;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.OutputStream;

public class VillasRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();

        switch (method) {
            case "GET":
                if (path.matches("/villas/?")) {
                    List<Villa> villas = VillasHandler.getAllVillas();
                    sendJsonResponse(exchange, villas);
                    return;
                } else if (path.matches("/villas/\\d+/?")) {
                    response.put("message", "Get Villa by ID");
                } else if (path.matches("/villas/\\d+/rooms/?")) {
                    response.put("message", "Room details for villa");
                } else if (path.matches("/villas/\\d+/bookings/?")) {
                    response.put("message", "Bookings for villa");
                } else if (path.matches("/villas/\\d+/reviews/?")) {
                    response.put("message", "Reviews for villa");
                } else if (path.contains("ci_date") && path.contains("co_date")) {
                    response.put("message", "Villa availability by date");
                }
                break;
            case "POST":
                if (path.matches("/villas/?")) {
                    response.put("message", "Create new villa");
                } else if (path.matches("/villas/\\d+/rooms/?")) {
                    response.put("message", "Add room to villa");
                }
                break;
            case "PUT":
                if (path.matches("/villas/\\d+/?")) {
                    response.put("message", "Update villa info");
                } else if (path.matches("/villas/\\d+/rooms/\\d+/?")) {
                    response.put("message", "Update villa room info");
                }
                break;
            case "DELETE":
                if (path.matches("/villas/\\d+/?")) {
                    response.put("message", "Delete villa");
                } else if (path.matches("/villas/\\d+/rooms/\\d+/?")) {
                    response.put("message", "Delete villa room");
                }
                break;
        }

        sendResponse(exchange, response);
    }

    private void sendResponse(HttpExchange exchange, Map<String, Object> data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(json.getBytes());
        os.close();
    }

    private void sendJsonResponse(HttpExchange exchange, Object data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes());
        }
    }
}