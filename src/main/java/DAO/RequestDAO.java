package DAO;

import db.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class RequestDAO {

    public static void saveRequest(String fullname, String email, String phone, String nationality,
            String tourType, String departure, String destination,
            String startDate, String endDate, String numPeople, String message) {
        String sql = "INSERT INTO requests (fullname, user_email, phone, nationality, tour_type, departure, destination, start_date, end_date, num_people, message, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, nationality);
            ps.setString(5, tourType);
            ps.setString(6, departure);
            ps.setString(7, destination);
            ps.setString(8, startDate);
            ps.setString(9, endDate);
            ps.setString(10, numPeople);
            ps.setString(11, message);
            ps.setString(12, "Chờ xử lý"); // trạng thái mặc định

            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, String>> getRequestsByEmail(String email) {
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT * FROM requests WHERE user_email = ? ORDER BY created_at DESC";

        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> req = new HashMap<>();
                req.put("tourType", rs.getString("tour_type"));
                req.put("message", rs.getString("message"));
                req.put("date", rs.getString("created_at"));
                list.add(req);
            }

            rs.close();
            ps.close();
            conn.close(); // nhớ đóng
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateRequestStatus(int requestId, String status) {
        String sql = "UPDATE requests SET status = ? WHERE id = ?";
        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status); // "Đã duyệt" hoặc "Từ chối"
            ps.setInt(2, requestId);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy tất cả yêu cầu tour (dành cho admin)
    public List<Map<String, String>> getAllRequests() {
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT * FROM requests ORDER BY created_at DESC";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, String> req = new HashMap<>();
                req.put("id", rs.getString("id"));
                req.put("fullname", rs.getString("fullname"));
                req.put("email", rs.getString("user_email"));
                req.put("tourType", rs.getString("tour_type"));
                req.put("departure", rs.getString("departure"));
                req.put("destination", rs.getString("destination"));
                req.put("createdAt", rs.getString("created_at")); // ✅ dùng camelCase để khớp JSP
                req.put("status", rs.getString("status"));
                list.add(req);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Map<String, String> getRequestById(int id) {
        Map<String, String> req = new HashMap<>();
        String sql = "SELECT * FROM requests WHERE id = ?";

        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                req.put("id", rs.getString("id"));
                req.put("fullname", rs.getString("fullname"));
                req.put("email", rs.getString("user_email"));
                req.put("phone", rs.getString("phone"));
                req.put("nationality", rs.getString("nationality"));
                req.put("tourType", rs.getString("tour_type"));
                req.put("departure", rs.getString("departure"));
                req.put("destination", rs.getString("destination"));
                req.put("startDate", rs.getString("start_date"));
                req.put("endDate", rs.getString("end_date"));
                req.put("numPeople", rs.getString("num_people"));
                req.put("message", rs.getString("message"));
                req.put("status", rs.getString("status"));
                req.put("created_at", rs.getString("created_at"));
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return req;
    }

}
