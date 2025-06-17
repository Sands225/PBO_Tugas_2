package handlers;

import models.*;
import db.Database;

import java.sql.*;
import java.util.*;

public class CustomersHandler {
// GET
//    public static List<Customer> getAllCustomers() {
//
//    }

//    public static Customer getCustomerById(int id) {
//
//    }

// POST
public static boolean addCustomer(Customer customer) {
    String sql = "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)";

    try (Connection conn = Database.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        if (customer.getName() == null || customer.getEmail() == null || customer.getPhone() == null) {
            throw new IllegalArgumentException("Name, email, and phone cannot be null.");
        }

        pstmt.setString(1, customer.getName());
        pstmt.setString(2, customer.getEmail());
        pstmt.setString(3, customer.getPhone());

        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

// UPDATE
    public static boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setInt(4, customer.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }
}