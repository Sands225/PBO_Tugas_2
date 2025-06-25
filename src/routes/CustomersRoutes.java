package routes;

import exceptions.*;
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

        try {
            switch (method) {
                case "GET":
                    if (path.matches("/customers/?")) {
                        List<Customer> customers = CustomersHandler.getAllCustomers();
                        SendResponseUtils.sendJsonResponse(exchange, customers, "Customers record retrieved successfully.");
                        return;

                    } else if (path.matches("/customers/\\d+/?")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Customer customer = CustomersHandler.getCustomerById(id);
                        SendResponseUtils.sendJsonResponse(exchange, customer, "Customer with ID " + id + " retrieved successfully.");
                        return;

                    } else if (path.matches("/customers/\\d+/bookings/?")) {
                        int customerId = Integer.parseInt(path.split("/")[2]);
                        CustomersHandler.getCustomerById(customerId); // Throws NotFoundException if not found
                        List<Map<String, Object>> bookings = BookingsHandler.getBookingsByCustomerId(customerId);
                        SendResponseUtils.sendJsonResponse(exchange, bookings, "Bookings with customer ID " + customerId + " retrieved successfully.");
                        return;

                    } else if (path.matches("/customers/\\d+/reviews/?")) {
                        int customerId = Integer.parseInt(path.split("/")[2]);
                        CustomersHandler.getCustomerById(customerId);
                        List<Map<String, Object>> reviews = ReviewsHandler.getReviewsByCustomerId(customerId);
                        SendResponseUtils.sendJsonResponse(exchange, reviews, "Reviews by customer ID " + customerId + " retrieved successfully.");
                        return;
                    }
                    break;

                case "POST":
                    if (path.matches("/customers/?")) {
                        InputStream is = exchange.getRequestBody();
                        Customer customer = mapper.readValue(is, Customer.class);

                        CustomerValidation.isCustomerValid(customer);

                        boolean success = CustomersHandler.addCustomer(customer);
                        if (!success) {
                            throw new RuntimeException("Failed to register customer");
                        }

                        response.put("message", "Customer registered successfully.");
                        sendResponse(exchange, response, 200);
                        return;

                    } else if (path.matches("/customers/\\d+/bookings/?")) {
                        int customerId = Integer.parseInt(path.split("/")[2]);
                        InputStream is = exchange.getRequestBody();
                        Booking booking = mapper.readValue(is, Booking.class);
                        booking.setCustomer(customerId);

                        CustomersHandler.getCustomerById(customerId); // Check existence

                        if (!BookingsHandler.insertBooking(booking)) {
                            throw new RuntimeException("Failed to create booking");
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

                        CustomersHandler.getCustomerById(customerId);
                        Booking booking = BookingsHandler.getBookingByCustomerAndBookingId(customerId, bookingId);

                        if (!ReviewsHandler.insertBookingReview(review)) {
                            throw new RuntimeException("Failed to add review");
                        }

                        response.put("message", "Review has been successfully added");
                        sendResponse(exchange, response, 200);
                        return;
                    }
                    break;

                case "PUT":
                    if (path.matches("/customers/\\d+/?")) {
                        int customerId = Integer.parseInt(path.split("/")[2]);
                        CustomersHandler.getCustomerById(customerId); // check existence
                        InputStream is = exchange.getRequestBody();
                        Customer customer = mapper.readValue(is, Customer.class);
                        customer.setId(customerId);

                        if (!CustomersHandler.updateCustomer(customer)) {
                            throw new RuntimeException("Failed to update customer");
                        }

                        response.put("message", "Customer updated successfully");
                        sendResponse(exchange, response, 200);
                        return;
                    }
                    break;
            }

            throw new NotFoundException("Method or route not supported: " + method + " " + path);

        }  catch (NotFoundException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 404);
        } catch (IllegalArgumentException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 400);
        } catch (IOException e) {
            SendResponseUtils.sendErrorResponse(exchange, "I/O error: " + e.getMessage(), 500);
            e.printStackTrace();
        } catch (RuntimeException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 500);
        } catch (Exception e) {
            SendResponseUtils.sendErrorResponse(exchange, "Unexpected error: " + e.getMessage(), 500);
            e.printStackTrace();
        }
    }
}
