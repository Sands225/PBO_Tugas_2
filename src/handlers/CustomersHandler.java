package handlers;

import exceptions.DatabaseException;
import exceptions.NotFoundException;
import models.*;
import db.Database;

import java.sql.*;
import java.util.*;

public class CustomersHandler {
    // GET
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                customers.add(customer);
            }

            if (customers.isEmpty()) {
                throw new NotFoundException("No customers found.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve customers", e);
        }
        return customers;
    }

    public static Customer getCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            } else {
                throw new NotFoundException("Customer with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve customer with ID " + id, e);
        }
    }
    // POST
    public static void addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add customer", e);
        }
    }

    // UPDATE
    public static void updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setInt(4, customer.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update customer", e);
        }
    }
}