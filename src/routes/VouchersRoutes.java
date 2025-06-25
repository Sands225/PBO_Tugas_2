package routes;

import handlers.VouchersHandler;
import models.Voucher;
import exceptions.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.SendResponseUtils;

public class VouchersRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        Map<String, Object> response = new HashMap<>();

        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            ObjectMapper mapper = new ObjectMapper();

            switch (method) {
                case "GET":
                    if (path.matches("/vouchers/?")) {
                        List<Voucher> vouchers = VouchersHandler.getAllVouchers();
                        SendResponseUtils.sendJsonResponse(exchange, vouchers, "Vouchers retrieved successfully");
                        return;

                    } else if (path.matches("/vouchers/\\d+/?")) {
                        int id = Integer.parseInt(path.replaceAll("\\D+", ""));
                        Voucher voucher = VouchersHandler.getVoucherById(id);
                        SendResponseUtils.sendJsonResponse(exchange, voucher, "Voucher with ID " + id + " retrieved successfully");
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

                        SendResponseUtils.sendSuccessResponse(exchange, "Voucher created successfully", voucher, 200);
                        return;
                    }
                    break;

                case "PUT":
                    if (path.matches("/vouchers/\\d+/?")) {
                        int voucherId = Integer.parseInt(path.split("/")[2]);

                        // check if voucher exists
                        VouchersHandler.getVoucherById(voucherId);

                        InputStream is = exchange.getRequestBody();
                        Voucher voucher = mapper.readValue(is, Voucher.class);
                        voucher.setId(voucherId);

                        boolean success = VouchersHandler.updateVoucher(voucher);
                        if (!success) {
                            throw new RuntimeException("Failed to update voucher");
                        }

                        SendResponseUtils.sendSuccessResponse(exchange, "Voucher updated successfully", voucher, 200);
                        return;
                    }
                    break;

                case "DELETE":
                    if (path.matches("/vouchers/\\d+/?")) {
                        int voucherId = Integer.parseInt(path.split("/")[2]);

                        // check if voucher exists
                        Voucher voucher = VouchersHandler.getVoucherById(voucherId);

                        boolean success = VouchersHandler.deleteVoucherById(voucherId);
                        if (!success) {
                            throw new RuntimeException("Failed to delete voucher");
                        }

                        SendResponseUtils.sendSuccessResponse(exchange, "Voucher deleted successfully", voucher, 200);
                        return;
                    }
                    break;

                default:
                    SendResponseUtils.sendErrorResponse(exchange, "Method not allowed", 405);
            }

            SendResponseUtils.sendErrorResponse(exchange, "Path " + path + " not found", 404);

        } catch (NotFoundException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 404);
        } catch (DatabaseException e) {
            SendResponseUtils.sendErrorResponse(exchange, "Database error: " + e.getMessage(), 500);
        } catch (NumberFormatException e) {
            SendResponseUtils.sendErrorResponse(exchange, "Invalid number format: " + e.getMessage(), 400);
        } catch (IOException e) {
            SendResponseUtils.sendErrorResponse(exchange, "I/O error: " + e.getMessage(), 500);
        } catch (Exception e) {
            SendResponseUtils.sendErrorResponse(exchange, "Unexpected error: " + e.getMessage(), 500);
            e.printStackTrace(); // Optional: for debugging
        }
    }
}