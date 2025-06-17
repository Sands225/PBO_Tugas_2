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
                response.put("message", "List of customers");
            } else if (path.matches("/customers/\\d+/?")) {
                response.put("message", "Customer detail");
            } else if (path.matches("/customers/\\d+/bookings/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);
                List<Map<String, Object>> bookings = BookingHandler.getBookingsByCustomerId(customerId);
                response.put("bookings", bookings);
            } else if (path.matches("/customers/\\d+/reviews/?")) {
                int customerId = Integer.parseInt(path.split("/")[2]);
                List<Map<String, Object>> reviews = ReviewsHandler.getReviewsByCustomerId(customerId);
                response.put("reviews", reviews);
            }
        } else if (method.equals("POST")) {
            if (path.matches("/customers/?")) {
                response.put("message", "Register new customer");
            } else if (path.matches("/customers/\\d+/bookings/?")) {
                response.put("message", "Customer books villa");
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
}
