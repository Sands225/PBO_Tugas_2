package handlers;

import models.*;
import db.Database;

import java.sql.*;
import java.util.*;

public class BookingsHandler {
    public static List<Map<String, Object>> getBookingsByVillaId(int villaId) {
        List<Map<String, Object>> bookings = new ArrayList<>();
        String sql =
            "SELECT b.* FROM bookings b"+
            "JOIN room_types rt ON b.room_type = rt.id"+
            "WHERE rt.villa = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, villaId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> booking = new HashMap<>();
                booking.put("id", rs.getInt("id"));
                booking.put("customer", rs.getInt("customer"));
                booking.put("room_type", rs.getInt("room_type"));
                booking.put("checkin_date", rs.getString("checkin_date"));
                booking.put("checkout_date", rs.getString("checkout_date"));
                booking.put("price", rs.getInt("price"));
                booking.put("voucher", rs.getObject("voucher"));
                booking.put("final_price", rs.getInt("final_price"));
                booking.put("payment_status", rs.getString("payment_status"));
                booking.put("has_checkedin", rs.getInt("has_checkedin"));
                booking.put("has_checkedout", rs.getInt("has_checkedout"));
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
}
