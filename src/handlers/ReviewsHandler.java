package handlers;

import models.*;
import db.Database;

import java.sql.*;
import java.util.*;

public class ReviewsHandler {
    public static List<Map<String, Object>> getReviewsByVillaId(int villaId) {
        List<Map<String, Object>> reviews = new ArrayList<>();
        String sql =
            "SELECT rv.*" +
            "FROM reviews rv" +
            "JOIN bookings b ON rv.booking = b.id" +
            "JOIN room_types r ON b.room_type = r.id" +
            "WHERE r.villa = ?";
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
            e.printStackTrace();
        }
        return reviews;
    }

}