package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SendResponseUtils {

    public static void sendJsonResponse(HttpExchange exchange, Object data, String message) throws IOException {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", data);

        String json = new ObjectMapper().writeValueAsString(response);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, json.getBytes(StandardCharsets.UTF_8).length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void sendResponse(HttpExchange exchange, Object data, int statusCode) throws IOException {
        String responseJson = new ObjectMapper().writeValueAsString(data);
        byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    public static void sendSuccessResponse(HttpExchange exchange, String message, Object data, int statusCode) {
        Map<String, Object> successResponse = new LinkedHashMap<>();
        successResponse.put("status", "success");
        successResponse.put("message", message);
        successResponse.put("data", data);

        try {
            sendResponse(exchange, successResponse, statusCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendSuccessResponse(HttpExchange exchange, String message, Object data, int statusCode, boolean excludeId) {
        Map<String, Object> successResponse = new LinkedHashMap<>();
        successResponse.put("status", "success");
        successResponse.put("message", message);

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (excludeId && data != null) {
                ObjectNode node = mapper.valueToTree(data);
                node.remove("id");
                successResponse.put("data", node);
            } else {
                successResponse.put("data", data);
            }
            sendResponse(exchange, successResponse, statusCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendErrorResponse(HttpExchange exchange, String message, int statusCode) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", message);

        try {
            sendResponse(exchange, errorResponse, statusCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
