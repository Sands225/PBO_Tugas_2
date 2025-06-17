package handlers;

import models.*;
import db.Database;

import java.sql.*;
import java.util.*;

public class VouchersHandler {
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
            e.printStackTrace();
        }

        return vouchers;
    }

}