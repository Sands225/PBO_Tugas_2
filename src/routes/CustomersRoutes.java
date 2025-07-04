package routes;

import exceptions.*;
import handlers.*;
import models.*;
import validations.BookingValidation;
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
import validations.ReviewValidation;

public class CustomersRoutes implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        ObjectMapper mapper = new ObjectMapper();

        try {
            switch (method) {
                case "GET":
                    if (path.matches("/customers/?")) {
                        List<Customer> customers = CustomersHandler.getAllCustomers();
                        SendResponseUtils.sendSuccessResponse(exchange, "Customers retrieved successfully.", customers, 200);
                        return;

                    } else if (path.matches("/customers/\\d+/?")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Customer customer = CustomersHandler.getCustomerById(id);
                        SendResponseUtils.sendSuccessResponse(exchange, "Customer with ID " + id + " retrieved successfully.", customer, 200);
                        return;

                    } else if (path.matches("/customers/\\d+/bookings/?")) {
                        int customerId = Integer.parseInt(path.split("/")[2]);

                        CustomersHandler.getCustomerById(customerId);   // check if customer exist

                        List<Map<String, Object>> bookings = BookingsHandler.getBookingsByCustomerId(customerId);
                        SendResponseUtils.sendSuccessResponse(exchange, "Bookings with customer ID " + customerId + " retrieved successfully.", bookings, 200);
                        return;

                    } else if (path.matches("/customers/\\d+/reviews/?")) {
                        int customerId = Integer.parseInt(path.split("/")[2]);

                        CustomersHandler.getCustomerById(customerId);   // check if customer exist

                        List<Map<String, Object>> reviews = ReviewsHandler.getReviewsByCustomerId(customerId);
                        SendResponseUtils.sendSuccessResponse(exchange, "Reviews by customer ID " + customerId + " retrieved successfully.", reviews, 200);
                        return;
                    }
                    SendResponseUtils.sendErrorResponse(exchange, "GET route not found: " + path, 404);
                    break;

                case "POST":
                    if (path.matches("/customers/?")) {
                        InputStream is = exchange.getRequestBody();
                        Customer customer = mapper.readValue(is, Customer.class);

                        CustomerValidation.isCustomerValid(customer);   // check if input customer valid

                        CustomersHandler.addCustomer(customer);
                        SendResponseUtils.sendSuccessResponse(exchange, "customer added successfully", customer, 200, true);
                        return;

                    } else if (path.matches("/customers/\\d+/bookings/?")) {
                        int customerId = Integer.parseInt(path.split("/")[2]);
                        InputStream is = exchange.getRequestBody();
                        Booking booking = mapper.readValue(is, Booking.class);
                        booking.setCustomer(customerId);

                        CustomersHandler.getCustomerById(customerId);   // check if customer exist
                        BookingValidation.isBookingValid(booking);      // check if input booking valid

                        BookingsHandler.insertBooking(booking);
                        SendResponseUtils.sendSuccessResponse(exchange, "customer with ID " + customerId + " successfully booked", booking, 200, true);
                        return;

                    } else if (path.matches("/customers/\\d+/bookings/\\d+/reviews/?")) {
                        int customerId = Integer.parseInt(path.split("/")[2]);
                        int bookingId = Integer.parseInt(path.split("/")[4]);
                        InputStream is = exchange.getRequestBody();
                        Review review = mapper.readValue(is, Review.class);
                        review.setBooking(bookingId);

                        CustomersHandler.getCustomerById(customerId);   // check if customer exist
                        BookingsHandler.getBookingByCustomerAndBookingId(customerId, bookingId);    // check if booking with customer exist
                        ReviewValidation.isReviewValid(review);         // check if input review valid

                        ReviewsHandler.insertBookingReview(review);
                        SendResponseUtils.sendSuccessResponse(exchange, "customer review successfully added to booking with ID " + bookingId, review, 200, true);
                        return;
                    }
                    SendResponseUtils.sendErrorResponse(exchange, "POST route not found: " + path, 404);
                    break;

                case "PUT":
                    if (path.matches("/customers/\\d+/?")) {
                        int customerId = Integer.parseInt(path.split("/")[2]);
                        InputStream is = exchange.getRequestBody();
                        Customer customer = mapper.readValue(is, Customer.class);
                        customer.setId(customerId);

                        CustomersHandler.getCustomerById(customerId);   // check if customer exist
                        CustomerValidation.isCustomerValid(customer);   // check if input customer valid

                        CustomersHandler.updateCustomer(customer);
                        SendResponseUtils.sendSuccessResponse(exchange, "Customer updated successfully", customer, 200);
                        return;
                    }
                    SendResponseUtils.sendErrorResponse(exchange, "PUT route not found: " + path, 404);
                    break;

                default:
                    SendResponseUtils.sendErrorResponse(exchange, "Method not allowed", 405);
            }
        }  catch (NotFoundException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 404);
        } catch (IllegalArgumentException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 400);
        } catch (IOException e) {
            SendResponseUtils.sendErrorResponse(exchange, "I/O error: " + e.getMessage(), 500);
        } catch (RuntimeException e) {
            SendResponseUtils.sendErrorResponse(exchange, e.getMessage(), 500);
        } catch (Exception e) {
            SendResponseUtils.sendErrorResponse(exchange, "Unexpected error: " + e.getMessage(), 500);
        }
    }
}