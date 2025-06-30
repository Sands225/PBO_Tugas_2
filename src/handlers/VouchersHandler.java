package handlers;

import exceptions.*;
import models.*;
import db.Database;

import java.sql.*;
import java.util.*;

public class VouchersHandler {
    // GET
    public static List<Voucher> getAllVouchers() {
        List<Voucher> vouchers = new ArrayList<>();
        String sql = "SELECT * FROM vouchers";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Voucher voucher = new Voucher(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("description"),
                        rs.getDouble("discount"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                );
                vouchers.add(voucher);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve vouchers", e);
        }
        if (vouchers.isEmpty()) {
            throw new NotFoundException("No vouchers found");
        }
        return vouchers;
    }

    public static Voucher getVoucherById(int id) {
        String sql = "SELECT * FROM vouchers WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Voucher(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("description"),
                        rs.getDouble("discount"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                );
            } else {
                throw new NotFoundException("Voucher with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve voucher with ID " + id, e);
        }
    }

    // POST
    public static void insertVoucher(Voucher voucher) {
        String sql = "INSERT INTO vouchers (code, description, discount, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, voucher.getCode());
            pstmt.setString(2, voucher.getDescription());
            pstmt.setDouble(3, voucher.getDiscount());
            pstmt.setString(4, voucher.getStart_date());
            pstmt.setString(5, voucher.getEnd_date());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert voucher", e);
        }
    }

    // PUT
    public static void updateVoucher(Voucher voucher) {
        String sql = "UPDATE vouchers SET code = ?, description = ?, discount = ?, start_date = ?, end_date = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, voucher.getCode());
            pstmt.setString(2, voucher.getDescription());
            pstmt.setDouble(3, voucher.getDiscount());
            pstmt.setString(4, voucher.getStart_date());
            pstmt.setString(5, voucher.getEnd_date());
            pstmt.setInt(6, voucher.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update voucher with ID " + voucher.getId(), e);
        }
    }
  
    // DELETE
    public static void deleteVoucherById(int voucherId) {
        String sql = "DELETE FROM vouchers WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, voucherId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete voucher with ID " + voucherId, e);
        }
    }
}