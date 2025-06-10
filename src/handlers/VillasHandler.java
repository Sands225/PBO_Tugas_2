package handlers;

import models.*;
import db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VillasHandler {
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
            e.printStackTrace();
        }

        return villas;
    }
}
