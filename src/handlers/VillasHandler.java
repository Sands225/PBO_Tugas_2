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
            throw new DatabaseException("Error retrieving villas", e);
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
                throw new NotFoundException("Villa with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch villa with ID " + id, e);
        }
    }

    public static List<Map<String, Object>> getAvailableVillas(String ciDate, String coDate) {
        List<Map<String, Object>> villas = new ArrayList<>();
        String sql =
                "SELECT DISTINCT v.* " +
                "FROM villas v " +
                "JOIN room_types r ON r.villa = v.id " +
                "WHERE r.id NOT IN ( " +
                "    SELECT room_type " +
                "    FROM bookings " +
                "    WHERE NOT ( " +
                "        checkout_date <= ? OR checkin_date >= ? " +
                "    ) " +
                ")";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ciDate);
            stmt.setString(2, coDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> villa = new HashMap<>();
                villa.put("id", rs.getInt("id"));
                villa.put("name", rs.getString("name"));
                villa.put("description", rs.getString("description"));
                villa.put("address", rs.getString("address"));
                villas.add(villa);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch available villas", e);
        }

        if (villas.isEmpty()) {
            throw new NotFoundException("No available villas found in the selected date range");
        }

        return villas;
    }

    // POST
    public static boolean insertVilla(Villa villa) {
        String sql = "INSERT INTO villas (name, description, address) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, villa.getName());
            pstmt.setString(2, villa.getDescription());
            pstmt.setString(3, villa.getAddress());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving customers", e);
        }
    }

    // PUT / UPDATE
    public static boolean updateVilla(Villa villa) {
        String sql = "UPDATE villas SET name = ?, description = ?, address = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, villa.getName());
            pstmt.setString(2, villa.getDescription());
            pstmt.setString(3, villa.getAddress());
            pstmt.setInt(4, villa.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving customers", e);
        }
    }

    // DELETE
    public static boolean deleteVillaById(int villaId) {
        String sql = "DELETE FROM villas WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, villaId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving customers", e);
        }
    }
}
