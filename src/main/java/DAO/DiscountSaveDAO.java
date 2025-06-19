/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import db.DBContext;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Discount;

/**
 *
 * @author criss
 */
public class DiscountSaveDAO extends DBContext {

    public boolean haveSaveDiscountById(int id) {
        String sql = "SELECT * FROM VoucherSaved WHERE id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) { // <-- Cần kiểm tra nếu có dữ liệu
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error in getDiscountById: " + e.getMessage());
        }
        return false;
    }

    public List<Discount> getSavedVouchersByUser(int userId, String keyword, Date startDate, Date endDate, int offset, int limit) {
        List<Discount> list = new ArrayList<>();
        String sql = "SELECT * FROM ("
                + " SELECT d.*, ROW_NUMBER() OVER (ORDER BY d.start_date DESC) AS rn"
                + " FROM VoucherSaved vs JOIN Discount d ON vs.discount_id = d.id"
                + " WHERE vs.user_id = ? AND d.usage_count < d.max_usage"
                + " AND d.description LIKE ?"
                + " AND d.start_date <= GETDATE() AND d.end_date >= GETDATE()"
                + // 👈 Điều kiện kiểm tra current date
                ") AS sub"
                + " WHERE sub.rn BETWEEN ? AND ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, "%" + keyword + "%");
            stmt.setInt(3, offset);
            stmt.setInt(4, limit);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Discount d = new Discount();
                d.setId(rs.getInt("id"));
                d.setCode(rs.getString("code"));
                d.setDescription(rs.getString("description"));
                d.setDiscount_percent(rs.getInt("discount_percent"));
                d.setStart_date(rs.getTimestamp("start_date").toLocalDateTime());
                d.setEnd_date(rs.getTimestamp("end_date").toLocalDateTime());
                d.setMax_usage(rs.getInt("max_usage"));
                d.setUsage_count(rs.getInt("usage_count"));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countSavedVouchers(int userId, String keyword, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM VoucherSaved vs JOIN Discount"
                + " d ON vs.discount_id = d.id WHERE vs.user_id = ?"
                + " AND d.usage_count < d.max_usage AND d.description LIKE ? and"
                + " d.start_date <= GETDATE() AND d.end_date >= GETDATE()";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean saveVoucher(int userId, int discountId) {
        String sql = "INSERT INTO VoucherSaved(user_id, discount_id) VALUES (?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, discountId);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public boolean checkVoucherAlreadySaved(int userId, int discountId) {
        String sql = "SELECT COUNT(*) FROM VoucherSaved WHERE user_id = ? AND discount_id = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, discountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteSavedVoucher(int userId, int discountId) {
        String sql = "DELETE FROM VoucherSaved WHERE user_id = ? AND discount_id = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, discountId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
