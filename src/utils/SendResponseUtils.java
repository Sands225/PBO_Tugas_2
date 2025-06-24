package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SendResponseUtils {
    public static void sendResponse(HttpExchange exchange, String message, Object data, int statusCode) throws IOException {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "success");
        responseMap.put("message", message);
        responseMap.put("data", data);

        String responseJson = new ObjectMapper().writeValueAsString(responseMap);
        byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    public static void sendJsonResponse(HttpExchange exchange, Object data, String message) throws IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", data);

        String json = new ObjectMapper().writeValueAsString(response);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(json.getBytes());
        }
    }

    public static void sendErrorResponse(HttpExchange exchange, int code, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", message);

        try {
            sendResponse(exchange, message, error, code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
