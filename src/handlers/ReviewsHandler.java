package handlers;

import exceptions.DatabaseException;
import exceptions.NotFoundException;
import models.*;
import db.Database;

import java.sql.*;
import java.util.*;

public class ReviewsHandler {
    public static List<Map<String, Object>> getReviewsByVillaId(int villaId) {
        List<Map<String, Object>> reviews = new ArrayList<>();
        String sql =
            "SELECT rv.* " +
            "FROM reviews rv " +
            "LEFT JOIN bookings b ON rv.booking = b.id " +
            "LEFT JOIN room_types r ON b.room_type = r.id " +
            "WHERE r.villa = ? ";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> review = new HashMap<>();
                review.put("booking", rs.getInt("booking"));
                review.put("star", rs.getInt("star"));
                review.put("title", rs.getString("title"));
                review.put("content", rs.getString("content"));
                reviews.add(review);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving reviews with villa ID " + villaId, e);
        }

        if (reviews.isEmpty()) {
            throw new NotFoundException("No reviews found with villa ID " + villaId);
        }
        return reviews;
    }

    public static List<Map<String, Object>> getReviewsByCustomerId(int customerId) {
        List<Map<String, Object>> reviews = new ArrayList<>();
        String sql =
                "SELECT rv.* " +
                        "FROM reviews rv " +
                        "JOIN bookings b ON rv.booking = b.id " +
                        "JOIN customers c ON b.customer = c.id " +
                        "WHERE b.customer = ? ";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> review = new HashMap<>();
                review.put("booking", rs.getInt("booking"));
                review.put("star", rs.getInt("star"));
                review.put("title", rs.getString("title"));
                review.put("content", rs.getString("content"));
                reviews.add(review);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving reviews with customer ID " + customerId, e);
        }

        if (reviews.isEmpty()) {
            throw new NotFoundException("No reviews found with customer ID " + customerId);
        }

        return reviews;
    }

    // POST
    public static boolean insertBookingReview(Review review) {
        String sql =
                "INSERT INTO reviews (booking, star, title, content) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, review.getBooking());
            pstmt.setInt(2, review.getStar());
            pstmt.setString(3, review.getTitle());
            pstmt.setString(4, review.getContent());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert review", e);
        }
    }
}
