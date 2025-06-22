package routes;

import handlers.VouchersHandler;
import models.Voucher;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.OutputStream;
import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class VouchersRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();

        switch (method) {
            case "GET":
                if (path.matches("/vouchers/?")) {
                    List<Voucher> vouchers = VouchersHandler.getAllVouchers();
                    sendJsonResponse(exchange, vouchers);
                    return;

                } else if (path.matches("/vouchers/\\d+/?")) {
                    int id = Integer.parseInt(path.replaceAll("\\D+", ""));
                    Voucher voucher = VouchersHandler.getVoucherById(id);

                    if (voucher == null) {
                        response.put("error", "Voucher not found");
                        sendResponse(exchange, response, 404);
                        return;
                    }

                    sendJsonResponse(exchange, voucher);
                    return;
                }
                break;

            case "POST":
                if (path.matches("/vouchers/?")) {
                    InputStream is = exchange.getRequestBody();
                    Voucher voucher = mapper.readValue(is, Voucher.class);

                    boolean success = VouchersHandler.insertVoucher(voucher);
                    if (!success) {
                        response.put("error", "Failed to create voucher");
                        sendResponse(exchange, response, 400);
                        return;
                    }

                    response.put("message", "Voucher created successfully");
                    sendResponse(exchange, response, 200);
                    return;
                }
                break;

            case "PUT":
                if (path.matches("/vouchers/\\d+/?")) {
                    int voucherId = Integer.parseInt(path.split("/")[2]);
                    InputStream is = exchange.getRequestBody();
                    Voucher voucher = mapper.readValue(is, Voucher.class);
                    voucher.setId(voucherId);

                    boolean success = VouchersHandler.updateVoucher(voucher);
                    if (!success) {
                        response.put("error", "Failed to update voucher");
                        sendResponse(exchange, response, 400);
                    }

                    response.put("message", "Voucher updated successfully");
                    sendResponse(exchange, response, 200);
                    return;
                }
                break;

            case "DELETE":
                if (path.matches("/vouchers/\\d+/?")) {
                    int voucherId = Integer.parseInt(path.split("/")[2]);
                    boolean success = VouchersHandler.deleteVoucherById(voucherId);

                    if (!success) {
                        response.put("error", "Failed to delete voucher");
                        sendResponse(exchange, response, 400);
                        return;
                    }

                    response.put("message", "Voucher deleted successfully");
                    sendResponse(exchange, response, 200);
                    return;
                }
                break;
        }

        response.put("error", "Unsupported route or method");
        sendResponse(exchange, response, 404);
    }

    public static void sendResponse(HttpExchange exchange, Map<String, Object> responseMap, int responseCode) throws IOException {
        String responseJson = new ObjectMapper().writeValueAsString(responseMap);
        byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(responseCode, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
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