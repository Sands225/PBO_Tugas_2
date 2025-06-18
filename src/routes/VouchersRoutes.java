package routes;

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
                int id = Integer.parseInt(path.replaceAll("\\D+", ""));
                Voucher voucher = VouchersHandler.getVoucherById(id);
                if (voucher != null) {
                    sendJsonResponse(exchange, voucher);
                } else {
                    response.put("error", "Voucher not found");
                    exchange.sendResponseHeaders(404, 0);
                    exchange.getResponseBody().close();
                }
                return;
            }
        } else if (method.equals("POST") && path.matches("/vouchers/?")) {
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
        } else if (method.equals("PUT") && path.matches("/vouchers/\\d+/?")) {
            response.put("message", "Update voucher");
        } else if (method.equals("DELETE") && path.matches("/vouchers/\\d+/?")) {
            response.put("message", "Delete voucher");
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