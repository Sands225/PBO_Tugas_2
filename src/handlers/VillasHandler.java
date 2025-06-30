package handlers;

import models.*;
import exceptions.*;
import db.Database;

import java.sql.*;
import java.util.*;

public class VillasHandler {
    // GET
    public static List<Villa> getAllVillas() {
        List<Villa> villas = new ArrayList<>();
        String sql = "SELECT * FROM villas";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Villa villa = new Villa(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("address")
                );
                villas.add(villa);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve villas", e);
        }

        if (villas.isEmpty()) {
            throw new NotFoundException("No villa found");
        }

        return villas;
    }

    public static Villa getVillaById(int id) {
        String sql = "SELECT * FROM villas WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Villa(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("address")
                );
            } else {
                throw new NotFoundException("Villa with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve villa with ID " + id, e);
        }
    }

    public static List<Villa> getAvailableVillas(String ciDate, String coDate) {
        List<Villa> villas = new ArrayList<>();
        String sql = "SELECT DISTINCT v.* " +
                "FROM villas v " +
                "JOIN room_types r ON r.villa = v.id " +
                "WHERE NOT EXISTS ( " +
                "    SELECT 1 FROM bookings b " +
                "    WHERE b.room_type = r.id " +
                "    AND NOT (b.checkout_date <= ? OR b.checkin_date >= ?) " +
                ")";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ciDate);
            stmt.setString(2, coDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Villa villa = new Villa(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("address")
                );
                villas.add(villa);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve available villas", e);
        }
        if (villas.isEmpty()) {
            throw new NotFoundException("No available villas found in the selected date range");
        }
        return villas;
    }

    // POST
    public static void insertVilla(Villa villa) {
        String sql = "INSERT INTO villas (name, description, address) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, villa.getName());
            pstmt.setString(2, villa.getDescription());
            pstmt.setString(3, villa.getAddress());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add villa ", e);
        }
    }

    // UPDATE
    public static void updateVilla(Villa villa) {
        String sql = "UPDATE villas SET name = ?, description = ?, address = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, villa.getName());
            pstmt.setString(2, villa.getDescription());
            pstmt.setString(3, villa.getAddress());
            pstmt.setInt(4, villa.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update villa", e);
        }
    }

    // DELETE
    public static void deleteVillaById(int villaId) {
        String sql = "DELETE FROM villas WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, villaId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete villa", e);
        }
    }
}
