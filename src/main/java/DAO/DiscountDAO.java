/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import db.DBContext;
import java.sql.PreparedStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import model.Discount;
import model.Tour;

/**
 *
 * @author admin
 */
public class DiscountDAO extends DBContext {

    public Discount getDiscountById(int id) {
        String sql = "SELECT * FROM Discount WHERE id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) { // <-- Cần kiểm tra nếu có dữ liệu
                Discount dis = new Discount();
                dis.setId(rs.getInt("id"));
                dis.setCode(rs.getString("code"));
                dis.setDescription(rs.getString("description"));
                dis.setDiscount_percent(rs.getFloat("discount_percent"));
                dis.setStart_date(rs.getTimestamp("start_date").toLocalDateTime());
                dis.setEnd_date(rs.getTimestamp("end_date").toLocalDateTime());
                dis.setMax_usage(rs.getInt("max_usage"));
                dis.setUsage_count(rs.getInt("usage_count"));
                return dis;
            }

        } catch (SQLException e) {
            System.out.println("Error in getDiscountById: " + e.getMessage());
        }
        return null; // Trả về null nếu không có discount nào được tìm thấy
    }

    //get all discount co phan trang
    public Vector<Discount> getAllDiscountWithPaging(int pageIndex, int pageSize) {
        Vector<Discount> list = new Vector<>();
        String sql = "SELECT [id], [code], [description], [discount_percent], [start_date], "
                + "[end_date], [max_usage], [usage_count] "
                + "FROM Discount "
                + "ORDER BY id "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, (pageIndex - 1) * pageSize); // OFFSET
            st.setInt(2, pageSize); // FETCH NEXT
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Discount dis = new Discount();
                dis.setId(rs.getInt("id"));
                dis.setCode(rs.getString("code"));
                dis.setDescription(rs.getString("description"));
                dis.setDiscount_percent(rs.getFloat("discount_percent"));
                dis.setStart_date(rs.getTimestamp("start_date").toLocalDateTime());
                dis.setEnd_date(rs.getTimestamp("end_date").toLocalDateTime());
                dis.setMax_usage(rs.getInt("max_usage"));
                dis.setUsage_count(rs.getInt("usage_count"));
                list.add(dis);
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllDiscountWithPaging: " + e.getMessage());
        }
        return list;
    }

    public List<Integer> getAllTourIdHaveInDiscount(int dis_id) {
        String sql = "SELECT [tour_id]\n"
                + "  FROM [TourManagement_GR5_Tour].[dbo].[TourDiscount] where discount_id = ?";
        List<Integer> ids = new ArrayList<>();
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, dis_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public int getTotalDiscountCount() {
        String sql = "SELECT COUNT(*) FROM Discount";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //get top 10 discount nh nguoi dung nhat (con hsd, sl)
    public Vector<Discount> getTopDiscount() {
        String sql = "Select top (10) * from Discount where GETDATE() > start_date and GETDATE() < end_date and usage_count < max_usage";
        Vector<Discount> list = new Vector<>();
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Discount dis = new Discount();
                dis.setId(rs.getInt("id"));
                dis.setCode(rs.getString("code"));
                dis.setMax_usage(rs.getInt("max_usage"));
                dis.setDiscount_percent(rs.getFloat("discount_percent"));
                dis.setDescription(rs.getString("description"));
                list.add(dis);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    //tao discount (so luong; percent; code random)
    public int createDiscount(Discount discount) {
        System.out.println("runheee");
        String sql = "INSERT INTO Discount (code, description, discount_percent, start_date, end_date, max_usage) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, discount.getCode()); // random code bạn tự sinh bên ngoài DAO
            st.setString(2, discount.getDescription());
            st.setFloat(3, discount.getDiscount_percent());

            st.setTimestamp(4, Timestamp.valueOf(discount.getStart_date()));
            st.setTimestamp(5, Timestamp.valueOf(discount.getEnd_date()));

            st.setInt(6, discount.getMax_usage());

            int affectedRows = st.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }
            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    //update discount info
    public void updateDiscount(Discount d) {
        String sql = "UPDATE Discount SET description = ?, discount_percent = ?, start_date = ?, end_date = ?, max_usage = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, d.getDescription());
            ps.setDouble(2, d.getDiscount_percent());
            ps.setTimestamp(3, Timestamp.valueOf(d.getStart_date()));
            ps.setTimestamp(4, Timestamp.valueOf(d.getEnd_date()));
            ps.setInt(5, d.getMax_usage());
            ps.setInt(6, d.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //update discount
    public boolean updateDiscountWithProducts(int discountId, Discount discount, Vector<Integer> selectedTourIds) {
        String updateSql = "UPDATE Discount SET code=?, description=?, discount_percent=?, start_date=?, end_date=?, max_usage=? WHERE id=?";
        String deleteOldLinks = "DELETE FROM TourDiscount WHERE discount_id=?";
        String insertNewLink = "INSERT INTO TourDiscount (tour_id, discount_id) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false);

            // Cập nhật Discount
            PreparedStatement st = conn.prepareStatement(updateSql);
            st.setString(1, discount.getCode());
            st.setString(2, discount.getDescription());
            st.setFloat(3, discount.getDiscount_percent());
            st.setTimestamp(4, Timestamp.valueOf(discount.getStart_date()));
            st.setTimestamp(5, Timestamp.valueOf(discount.getEnd_date()));
            st.setInt(6, discount.getMax_usage());
            st.setInt(7, discountId);
            st.executeUpdate();

            // Xóa tất cả liên kết cũ
            PreparedStatement delStmt = conn.prepareStatement(deleteOldLinks);
            delStmt.setInt(1, discountId);
            delStmt.executeUpdate();

            // Thêm lại các liên kết mới
            if (selectedTourIds != null && !selectedTourIds.isEmpty()) {
                PreparedStatement insertStmt = conn.prepareStatement(insertNewLink);
                for (int tid : selectedTourIds) {
                    insertStmt.setInt(1, tid);
                    insertStmt.setInt(2, discountId);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //xoa discount
    public boolean deleteDiscount(int discountId) {
        String deleteProductDiscountSql = "DELETE FROM TourDiscount WHERE discount_id = ?";
        String deleteDiscountSql = "DELETE FROM Discount WHERE id = ?";
        try {
            conn.setAutoCommit(false);

            PreparedStatement st1 = conn.prepareStatement(deleteProductDiscountSql);
            st1.setInt(1, discountId);
            st1.executeUpdate();

            PreparedStatement st2 = conn.prepareStatement(deleteDiscountSql);
            st2.setInt(1, discountId);
            int rowsAffected = st2.executeUpdate();

            conn.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //get tour by discount
    public Vector<Tour> getToursByDiscountId(int discountId, int pageIndex, int pageSize) {
        Vector<Tour> tours = new Vector<>();
        String sql = "SELECT t.* FROM Tour t "
                + "JOIN TourDiscount td ON td.tour_id = t.id "
                + "WHERE td.discount_id = ? "
                + "ORDER BY t.id "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, discountId);
            st.setInt(2, (pageIndex - 1) * pageSize); // OFFSET
            st.setInt(3, pageSize);                  // FETCH NEXT

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Tour t = new Tour();
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                t.setLocation(rs.getString("location"));
                t.setPrice(rs.getBigDecimal("price"));
                t.setTransport(rs.getString("transport"));
                t.setDescription(rs.getString("description"));
                t.setStartDate(rs.getDate("startDate"));
                t.setEndDate(rs.getDate("endDate"));
                t.setImageUrl(rs.getString("image_url"));
                t.setType(rs.getString("type"));
                tours.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tours;
    }

    public int countToursByDiscountId(int discountId) {
        String sql = "SELECT COUNT(*) FROM TourDiscount WHERE discount_id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, discountId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addTourToDiscount(int tourId, int discountId) throws SQLException {
        String sql = "INSERT INTO TourDiscount (tour_id, discount_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tourId);
            ps.setInt(2, discountId);
            ps.executeUpdate();
        }
    }

    public void removeTourFromDiscount(int tourId, int discountId) throws SQLException {
        String sql = "DELETE FROM TourDiscount WHERE tour_id = ? and discount_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tourId);
            ps.setInt(2, discountId);
            ps.executeUpdate();
        }
    }

    public boolean checkCodeExists(String code) {
        String sql = "SELECT 1 FROM Discount WHERE code = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, code);
            ResultSet rs = st.executeQuery();
            return rs.next(); // true nếu code đã tồn tại
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}