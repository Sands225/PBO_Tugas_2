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
import validations.VoucherValidation;

public class VouchersRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        ObjectMapper mapper = new ObjectMapper();

        try {
            switch (method) {
                case "GET":
                    if (path.matches("/vouchers/?")) {
                        List<Voucher> vouchers = VouchersHandler.getAllVouchers();
                        SendResponseUtils.sendSuccessResponse(exchange, "Vouchers retrieved successfully", vouchers, 200);
                        return;

                    } else if (path.matches("/vouchers/\\d+/?")) {
                        int id = Integer.parseInt(path.replaceAll("\\D+", ""));
                        Voucher voucher = VouchersHandler.getVoucherById(id);
                        SendResponseUtils.sendSuccessResponse(exchange, "Voucher with ID " + id + " retrieved successfully", voucher, 200);
                        return;

                    }
                    break;

                case "POST":
                    if (path.matches("/vouchers/?")) {
                        InputStream is = exchange.getRequestBody();
                        Voucher voucher = mapper.readValue(is, Voucher.class);

                        VoucherValidation.isVoucherValid(voucher);  // check if input voucher valid

                        VouchersHandler.insertVoucher(voucher);
                        SendResponseUtils.sendSuccessResponse(exchange, "Voucher added successfully", voucher, 200, true);
                        return;
                    }
                    break;

                case "PUT":
                    if (path.matches("/vouchers/\\d+/?")) {
                        int voucherId = Integer.parseInt(path.split("/")[2]);
                        InputStream is = exchange.getRequestBody();
                        Voucher voucher = mapper.readValue(is, Voucher.class);
                        voucher.setId(voucherId);

                        VouchersHandler.getVoucherById(voucherId);  // check if voucher exist
                        VoucherValidation.isVoucherValid(voucher);  // check if input voucher valid

                        VouchersHandler.updateVoucher(voucher);
                        SendResponseUtils.sendSuccessResponse(exchange, "Voucher updated successfully", voucher, 200);
                        return;
                    }
                    break;

                case "DELETE":
                    if (path.matches("/vouchers/\\d+/?")) {
                        int voucherId = Integer.parseInt(path.split("/")[2]);

                        Voucher voucher = VouchersHandler.getVoucherById(voucherId);    // check if voucher exist
                        VoucherValidation.isVoucherValid(voucher);  // check if input voucher valid

                        VouchersHandler.deleteVoucherById(voucherId);
                        SendResponseUtils.sendSuccessResponse(exchange, "Voucher deleted successfully", voucher, 200);
                        return;
                    }
                    break;

                default:
                    SendResponseUtils.sendErrorResponse(exchange, "Method not allowed", 405);
            }
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
        }
    }
}