package handlers;

import exceptions.*;
import models.*;
import db.Database;

import java.sql.*;
import java.util.*;

public class RoomsHandler {
    //  GET
    public static List<Room> getRoomsByVillaId(int villaId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room_types WHERE villa = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, villaId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setVilla(rs.getInt("villa"));
                room.setName(rs.getString("name"));
                room.setQuantity(rs.getInt("quantity"));
                room.setCapacity(rs.getInt("capacity"));
                room.setPrice(rs.getInt("price"));
                room.setBed_size(rs.getString("bed_size"));
                room.setHas_desk(rs.getInt("has_desk"));
                room.setHas_ac(rs.getInt("has_ac"));
                room.setHas_tv(rs.getInt("has_tv"));
                room.setHas_wifi(rs.getInt("has_wifi"));
                room.setHas_shower(rs.getInt("has_shower"));
                room.setHas_hotwater(rs.getInt("has_hotwater"));
                room.setHas_fridge(rs.getInt("has_fridge"));
                rooms.add(room);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve rooms with villa ID " + villaId, e);
        }
        if (rooms.isEmpty()) {
            throw new NotFoundException("No rooms found for villa ID " + villaId);
        }
        return rooms;
    }

    public static Room getRoomByVillaAndRoomId(int villaId, int roomId) {
        String sql = "SELECT * FROM room_types WHERE id = ? AND villa = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            stmt.setInt(2, villaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Room(
                        rs.getInt("id"),
                        rs.getInt("villa"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getInt("capacity"),
                        rs.getInt("price"),
                        rs.getString("bed_size"),
                        rs.getInt("has_desk"),
                        rs.getInt("has_ac"),
                        rs.getInt("has_tv"),
                        rs.getInt("has_wifi"),
                        rs.getInt("has_shower"),
                        rs.getInt("has_hotwater"),
                        rs.getInt("has_fridge")
                );
            } else {
                throw new NotFoundException("Room with ID " + roomId + " for villa ID " + villaId + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve room with ID " + roomId + " for villa ID " + villaId, e);
        }
    }

    // POST
    public static void insertRoomType(Room room) {
        String sql = "INSERT INTO room_types (villa, name, quantity, capacity, price, bed_size, has_desk, has_ac, has_tv, has_wifi, has_shower, has_hotwater, has_fridge) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, room.getVilla());
            pstmt.setString(2, room.getName());
            pstmt.setInt(3, room.getQuantity());
            pstmt.setInt(4, room.getCapacity());
            pstmt.setInt(5, room.getPrice());
            pstmt.setString(6, room.getBed_size());
            pstmt.setInt(7, room.getHas_desk());
            pstmt.setInt(8, room.getHas_ac());
            pstmt.setInt(9, room.getHas_tv());
            pstmt.setInt(10, room.getHas_wifi());
            pstmt.setInt(11, room.getHas_shower());
            pstmt.setInt(12, room.getHas_hotwater());
            pstmt.setInt(13, room.getHas_fridge());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add room", e);
        }
    }

    // PUT / UPDATE
    public static void updateRoomType(Room room) {
        String sql = "UPDATE room_types SET name = ?, quantity = ?, capacity = ?, price = ?, bed_size = ?, has_desk = ?, has_ac = ?, has_tv = ?, has_wifi = ?, has_shower = ?, has_hotwater = ?, has_fridge = ? " +
                "WHERE id = ? AND villa = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, room.getName());
            pstmt.setInt(2, room.getQuantity());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setInt(4, room.getPrice());
            pstmt.setString(5, room.getBed_size());
            pstmt.setInt(6, room.getHas_desk());
            pstmt.setInt(7, room.getHas_ac());
            pstmt.setInt(8, room.getHas_tv());
            pstmt.setInt(9, room.getHas_wifi());
            pstmt.setInt(10, room.getHas_shower());
            pstmt.setInt(11, room.getHas_hotwater());
            pstmt.setInt(12, room.getHas_fridge());
            pstmt.setInt(13, room.getId());
            pstmt.setInt(14, room.getVilla());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update room ID " + room.getId() + " for villa ID " + room.getVilla(), e);
        }
    }

    // DELETE
    public static void deleteRoomTypeById(int roomId, int villaId) {
        String sql = "DELETE FROM room_types WHERE id = ? AND villa = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            pstmt.setInt(2, villaId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete room ID " + roomId + " from villa ID " + villaId, e);
        }
    }
}
