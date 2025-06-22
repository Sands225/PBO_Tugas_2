package handlers;

import models.*;
import db.Database;

import java.sql.*;
import java.util.*;

public class BookingsHandler {
    // GET
    public static List<Map<String, Object>> getBookingsByVillaId(int villaId) {
        List<Map<String, Object>> bookings = new ArrayList<>();
        String sql =
                "SELECT b.* FROM bookings b " +
                        "JOIN room_types rt ON b.room_type = rt.id " +
                        "WHERE rt.villa = ? ";

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

    public static List<Map<String, Object>> getBookingsByCustomerId(int customerId) {
        List<Map<String, Object>> bookings = new ArrayList<>();
        String sql =
                "SELECT b.* FROM bookings b " +
                        "JOIN customers c ON c.id = b.customer " +
                        "WHERE c.id = ? ";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
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

    public static Booking getBookingByCustomerAndBookingId(int customerId, int bookingId) {
        String sql = "SELECT * FROM bookings WHERE id = ? AND customer = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            stmt.setInt(2, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Booking(
                        rs.getInt("id"),
                        rs.getInt("customer"),
                        rs.getInt("room_type"),
                        rs.getString("checkin_date"),
                        rs.getString("checkout_date"),
                        rs.getInt("price"),
                        (Integer) rs.getObject("voucher"), // handles nulls
                        rs.getInt("final_price"),
                        rs.getString("payment_status"),
                        rs.getInt("has_checkedin"),
                        rs.getInt("has_checkedout")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // POST
        public static boolean insertBooking (Booking booking){
            String sql = "INSERT INTO bookings (customer, room_type, checkin_date, checkout_date, price, voucher, final_price, payment_status, has_checkedin, has_checkedout) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, booking.getCustomer());
                pstmt.setInt(2, booking.getRoom_type());
                pstmt.setString(3, booking.getCheckin_date());
                pstmt.setString(4, booking.getCheckout_date());
                pstmt.setInt(5, booking.getPrice());

                if (booking.getVoucher() != null) {
                    pstmt.setInt(6, booking.getVoucher());
                } else {
                    pstmt.setNull(6, java.sql.Types.INTEGER);
                }

                pstmt.setInt(7, booking.getFinal_price());
                pstmt.setString(8, booking.getPayment_status());
                pstmt.setInt(9, booking.getHas_checkedin());
                pstmt.setInt(10, booking.getHas_checkedout());

                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
}