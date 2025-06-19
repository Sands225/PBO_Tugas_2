package routes;

import handlers.VillasHandler;
import handlers.VouchersHandler;
import models.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.*;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class VouchersRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();

        if (method.equals("GET")) {
            if (path.matches("/vouchers/?")) {
                List<Voucher> vouchers = VouchersHandler.getAllVouchers();
                sendJsonResponse(exchange, vouchers);
                return;
            } else if (path.matches("/vouchers/\\d+/?")) {
                InputStream is = exchange.getRequestBody();
                Voucher voucher = mapper.readValue(is, Voucher.class);

                boolean success = VouchersHandler.insertVoucher(voucher);
                if (success) {
                    response.put("message", "Voucher created successfully");
                } else {
                    response.put("error", "Failed to create voucher");
                }
                sendResponse(exchange, response);
                return;
            }
        } else if (method.equals("POST") && path.matches("/vouchers/?")) {
            response.put("message", "Create voucher");
        } else if (method.equals("PUT") && path.matches("/vouchers/\\d+/?")) {
            int voucherId = Integer.parseInt(path.split("/")[2]);
            InputStream is = exchange.getRequestBody();
            Voucher voucher = mapper.readValue(is, Voucher.class);
            voucher.setId(voucherId);

            boolean success = VouchersHandler.updateVoucher(voucher);
            if (success) {
                response.put("message", "Voucher updated successfully");
            } else {
                response.put("error", "Failed to update voucher");
            }
            sendResponse(exchange, response);
            return;
        } else if (method.equals("DELETE") && path.matches("/vouchers/\\d+/?")) {
            int voucherId = Integer.parseInt(path.split("/")[2]);
            boolean success = VillasHandler.deleteVillaById(voucherId);

            if (success) {
                response.put("message", "Voucher deleted successfully");
            } else {
                response.put("error", "Failed to delete voucher");
            }
            sendResponse(exchange, response);
            return;
        }

        sendResponse(exchange, response);
    }

    public void sendResponse(HttpExchange exchange, Map<String, Object> data) throws IOException {
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