package routes;

import exceptions.*;
import handlers.*;
import models.*;
import validations.CustomerValidation;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.SendResponseUtils;
import validations.CustomerValidation;

import static utils.SendResponseUtils.sendResponse;

public class CustomersRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();

        if (method.equals("GET")) {
            if (path.matches("/customers/?")) {
                List<Customer> customer = CustomersHandler.getAllCustomers();
                sendJsonResponse(exchange, customer);
                return;

            } else if (path.matches("/customers/\\d+/?")) {
                int id = Integer.parseInt(path.replaceAll("\\D+", ""));
                Customer customer = CustomersHandler.getCustomerById(id);
                if (customer != null) {
                    sendJsonResponse(exchange, customer);
                } else {
                    sendError(exchange, 404, "Customer not found");
                }
                return;

            } else if (path.matches("/customers/\\d+/bookings/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);
                List<Map<String, Object>> bookings = BookingsHandler.getBookingsByCustomerId(customerId);
                response.put("bookings", bookings);

            } else if (path.matches("/customers/\\d+/reviews/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);
                List<Map<String, Object>> reviews = ReviewsHandler.getReviewsByCustomerId(customerId);
                response.put("reviews", reviews);
            }

        } else if (method.equals("POST")) {
            if (path.matches("/customers/?")) {
                InputStream is = exchange.getRequestBody();
                Customer customer = mapper.readValue(is, Customer.class);

                // ✅ VALIDASI PER INPUT
                if (!CustomerValidation.isNameValid(customer.getName())) {
                    sendError(exchange, 400, "Nama tidak boleh kosong.");
                    return;
                }

                if (!CustomerValidation.isEmailValid(customer.getEmail())) {
                    sendError(exchange, 400, "Format email tidak valid.");
                    return;
                }

                if (!CustomerValidation.isPhoneValid(customer.getPhone())) {
                    sendError(exchange, 400, "Nomor telepon tidak valid. Minimal 10 digit angka.");
                    return;
                }

                boolean success = CustomersHandler.addCustomer(customer);
                if (success) {
                    response.put("message", "Customer registered successfully.");
                    sendResponse(exchange, response);
                } else {
                    sendError(exchange, 400, "Gagal menambahkan customer.");
                }
                return;
            }

            else if (path.matches("/customers/\\d+/bookings/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);

                InputStream is = exchange.getRequestBody();
                Booking booking = mapper.readValue(is, Booking.class);
                booking.setCustomer(customerId);

                boolean success = BookingsHandler.insertBooking(booking);
                if (success) {
                    response.put("message", "Booking successfully created.");
                } else {
                    sendError(exchange, 400, "Failed to create booking. Check data validity.");
                }
                sendResponse(exchange, response);
                return;
            }

            else if (path.matches("/customers/\\d+/bookings/\\d+/reviews/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);
                int bookingId = Integer.parseInt(path.split("/")[4]);

                InputStream is = exchange.getRequestBody();
                Review review = mapper.readValue(is, Review.class);
                review.setBooking(bookingId);

                boolean success = ReviewsHandler.insertBookingReview(review);
                if (success) {
                    response.put("message", "Review has been successfully added");
                } else {
                    sendError(exchange, 400, "Failed to add review");
                }
                sendResponse(exchange, response);
                return;
            }

        } else if (method.equals("PUT") && path.matches("/customers/\\d+/?")) {
            int customerId = Integer.parseInt(path.split("/")[2]);
            InputStream is = exchange.getRequestBody();
            Customer customer = mapper.readValue(is, Customer.class);
            customer.setId(customerId);

            if (!CustomerValidation.isNameValid(customer.getName())) {
                sendError(exchange, 400, "Nama tidak boleh kosong.");
                return;
            }

            if (!CustomerValidation.isEmailValid(customer.getEmail())) {
                sendError(exchange, 400, "Format email tidak valid.");
                return;
            }

            if (!CustomerValidation.isPhoneValid(customer.getPhone())) {
                sendError(exchange, 400, "Nomor telepon tidak valid. Minimal 10 digit angka.");
                return;
            }

            boolean success = CustomersHandler.updateCustomer(customer);
            if (success) {
                response.put("message", "Customer updated successfully.");
                sendResponse(exchange, response);
            } else {
                sendError(exchange, 404, "Customer tidak ditemukan atau gagal diperbarui.");
            }
            return;
        }

          throw new NotFoundException("Method or route not supported: " + method + " " + path);

        } catch (NotFoundException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 404);
        } catch (IllegalArgumentException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 400);
        } catch (RuntimeException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 500);
        } catch (Exception e) {
            SendResponseUtils.sendErrorResponse(exchange, "Unexpected error: " + e.getMessage(), 500);
        }
    }
}