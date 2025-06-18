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
                Customer customer = CustomersHandler.getCustomerById(id);
                if (customer != null) {
                    sendJsonResponse(exchange, customer);
                } else {
                    response.put("error", "Customer not found");
                    exchange.sendResponseHeaders(404, 0);
                    exchange.getResponseBody().close();
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

                boolean success = CustomersHandler.addCustomer(customer);
                if (success) {
                    response.put("message", "Customer registered successfully.");
                } else {
                    exchange.sendResponseHeaders(400, 0);
                    response.put("error", "Failed to register customer. Data might be invalid.");
                }
                sendResponse(exchange, response);
                return;
            } else if (path.matches("/customers/\\d+/bookings/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);

                InputStream is = exchange.getRequestBody();
                Booking booking = mapper.readValue(is, Booking.class);
                booking.setCustomer(customerId);

                boolean success = BookingsHandler.insertBooking(booking);
                if (success) {
                    response.put("message", "Booking successfully created.");
                } else {
                    exchange.sendResponseHeaders(400, 0);
                    response.put("error", "Failed to create booking. Check data validity.");
                }
                sendResponse(exchange, response);
                return;
            } else if (path.matches("/customers/\\d+/bookings/\\d+/reviews/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);
                int bookingId = Integer.parseInt(path.split("/")[4]);

                InputStream is = exchange.getRequestBody();
                Review review = mapper.readValue(is, Review.class);
                review.setBooking(bookingId);

                boolean success = ReviewsHandler.insertBookingReview(review);
                if (success) {
                    response.put("message", "Review has been successfully added");
                } else {
                    response.put("error", "Failed to add review");
                }
                sendResponse(exchange, response);
                return;
            }
        } else if (method.equals("PUT") && path.matches("/customers/\\d+/?")) {
            int customerId = Integer.parseInt(path.split("/")[2]);
            InputStream is = exchange.getRequestBody();
            Customer customer = mapper.readValue(is, Customer.class);
            customer.setId(customerId);

            boolean success = CustomersHandler.updateCustomer(customer);
            if (success) {
                response.put("message", "Villa updated successfully");
            } else {
                response.put("error", "Failed to update villa");
            }
            sendResponse(exchange, response);
            return;
        }

        sendResponse(exchange, response);
    }

    private void sendResponse(HttpExchange exchange, Map<String, Object> data) throws IOException {
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
