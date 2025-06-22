package routes;

import handlers.*;
import models.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

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

                // check if customer exist
                Customer customer = CustomersHandler.getCustomerById(id);
                if (customer == null) {
                    response.put("error", "Customer not found");
                    sendResponse(exchange, response, 404);
                    return;
                }

                sendJsonResponse(exchange, customer);
                return;

            } else if (path.matches("/customers/\\d+/bookings/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);

                // check if customer exist
                Customer customer = CustomersHandler.getCustomerById(customerId);
                if (customer == null) {
                    response.put("error", "Customer not found");
                    sendResponse(exchange, response, 404);
                }

                List<Map<String, Object>> bookings = BookingsHandler.getBookingsByCustomerId(customerId);
                response.put("bookings", bookings);

            } else if (path.matches("/customers/\\d+/reviews/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);

                // check if customer exist
                Customer customer = CustomersHandler.getCustomerById(customerId);
                if (customer == null) {
                    response.put("error", "Customer not found");
                    sendResponse(exchange, response, 404);
                }

                List<Map<String, Object>> reviews = ReviewsHandler.getReviewsByCustomerId(customerId);
                response.put("reviews", reviews);
            }
        } else if (method.equals("POST")) {
            if (path.matches("/customers/?")) {
                InputStream is = exchange.getRequestBody();
                Customer customer = mapper.readValue(is, Customer.class);

                boolean success = CustomersHandler.addCustomer(customer);
                if (!success) {
                    response.put("error", "Failed to register customer");
                    sendResponse(exchange, response, 400);
                    return;
                }

                response.put("message", "Customer registered successfully.");
                sendResponse(exchange, response, 200);
                return;

            } else if (path.matches("/customers/\\d+/bookings/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);

                InputStream is = exchange.getRequestBody();
                Booking booking = mapper.readValue(is, Booking.class);
                booking.setCustomer(customerId);

                // check if customer exist
                Customer customer = CustomersHandler.getCustomerById(customerId);
                if (customer == null) {
                    response.put("error", "Customer not found");
                    sendResponse(exchange, response, 404);
                }

                boolean success = BookingsHandler.insertBooking(booking);
                if (!success) {
                    response.put("error", "Failed to create booking");
                    sendResponse(exchange, response, 400);
                    return;
                }

                response.put("message", "Booking has been successfully added");
                sendResponse(exchange, response, 200);
                return;
            } else if (path.matches("/customers/\\d+/bookings/\\d+/reviews/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);
                int bookingId = Integer.parseInt(path.split("/")[4]);

                InputStream is = exchange.getRequestBody();
                Review review = mapper.readValue(is, Review.class);
                review.setBooking(bookingId);

                // check if customer exist
                Customer customer = CustomersHandler.getCustomerById(customerId);
                if (customer == null) {
                    response.put("error", "Customer not found");
                    sendResponse(exchange, response, 404);
                }

                // check if booking exist
                Booking booking = BookingsHandler.getBookingByCustomerAndBookingId(customerId, bookingId);
                if (booking == null) {
                    response.put("error", "Booking not found");
                    sendResponse(exchange, response, 404);
                }

                boolean success = ReviewsHandler.insertBookingReview(review);
                if (!success) {
                    response.put("error", "Failed to add review");
                    sendResponse(exchange, response, 400);
                    return;
                }

                response.put("message", "Review has been successfully added");
                sendResponse(exchange, response, 200);
                return;
            }
        } else if (method.equals("PUT") && path.matches("/customers/\\d+/?")) {
            int customerId = Integer.parseInt(path.split("/")[2]);
            InputStream is = exchange.getRequestBody();

            // check if customer exist
            Customer existingCustomer = CustomersHandler.getCustomerById(customerId);
            if (existingCustomer == null) {
                response.put("error", "Customer not found");
                sendResponse(exchange, response, 404);
            }

            Customer customer = mapper.readValue(is, Customer.class);
            customer.setId(customerId);

            boolean success = CustomersHandler.updateCustomer(customer);
            if (!success) {
                response.put("error", "Failed to update customer");
                sendResponse(exchange, response, 400);
            }

            response.put("message", "Customer updated successfully");
            sendResponse(exchange, response, 200);
            return;
        }

        sendResponse(exchange, response, 200);
    }

    public static void sendResponse(HttpExchange exchange, Map<String, Object> responseMap, int responseCode) throws IOException {
        String responseJson = new ObjectMapper().writeValueAsString(responseMap);
        byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(responseCode, responseBytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
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
