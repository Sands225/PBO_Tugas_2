package routes;

import handlers.VouchersHandler;
import models.Voucher;
import exceptions.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.OutputStream;
import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.SendResponseUtils;

public class VouchersRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();

        try {
            switch (method) {
                case "GET":
                    if (path.matches("/vouchers/?")) {
                        List<Voucher> vouchers = VouchersHandler.getAllVouchers();
                        sendJsonResponse(exchange, vouchers);
                        return;

                    } else if (path.matches("/vouchers/\\d+/?")) {
                        int id = Integer.parseInt(path.replaceAll("\\D+", ""));
                        Voucher voucher = VouchersHandler.getVoucherById(id);
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
                            throw new RuntimeException("Failed to create voucher");
                        }

                        response.put("message", "Voucher created successfully");
                        sendResponse(exchange, response, 200);
                        return;
                    }
                    break;

                case "PUT":
                    if (path.matches("/vouchers/\\d+/?")) {
                        int voucherId = Integer.parseInt(path.split("/")[2]);

                        VouchersHandler.getVoucherById(voucherId); // Will throw NotFoundException if not found

                        InputStream is = exchange.getRequestBody();
                        Voucher voucher = mapper.readValue(is, Voucher.class);
                        voucher.setId(voucherId);

                        boolean success = VouchersHandler.updateVoucher(voucher);
                        if (!success) {
                            throw new RuntimeException("Failed to update voucher");
                        }

                        response.put("message", "Voucher updated successfully");
                        sendResponse(exchange, response, 200);
                        return;
                    }
                    break;

                case "DELETE":
                    if (path.matches("/vouchers/\\d+/?")) {
                        int voucherId = Integer.parseInt(path.split("/")[2]);

                        VouchersHandler.getVoucherById(voucherId); // Will throw NotFoundException if not found

                        boolean success = VouchersHandler.deleteVoucherById(voucherId);
                        if (!success) {
                            throw new RuntimeException("Failed to delete voucher");
                        }

                        response.put("message", "Voucher deleted successfully");
                        sendResponse(exchange, response, 200);
                        return;
                    }
                    break;
            }

            throw new NotFoundException("Unsupported route or method: " + method + " " + path);

        } catch (NotFoundException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 404);
        } catch (IllegalArgumentException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 400);
        } catch (DatabaseException e) {
            SendResponseUtils.sendErrorResponse(exchange, "Database error: " + e.getMessage(), 500);
        } catch (Exception e) {
            SendResponseUtils.sendErrorResponse(exchange, "Unexpected error: " + e.getMessage(), 500);
            e.printStackTrace();
        }
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