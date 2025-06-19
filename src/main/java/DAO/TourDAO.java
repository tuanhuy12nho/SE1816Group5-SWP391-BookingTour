package DAO;

import db.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Tour;

public class TourDAO extends DBContext {

    public ArrayList<Tour> getAllTours() {
        ArrayList<Tour> tours = new ArrayList<>();
        String query = "SELECT * FROM Tour";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            if (conn == null) {
                System.out.println("❌ Unable to connect to the database");
                return null;
            }

            while (rs.next()) {
                Tour tour = new Tour(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getBigDecimal("price"),
                        rs.getString("transport"),
                        rs.getString("description"),
                        rs.getDate("startDate"),
                        rs.getDate("endDate"),
                        rs.getString("image_url") // thống nhất image_url
                );
                tours.add(tour);
                System.out.println("📌 Retrieved tour: " + tour.getName());
            }

            System.out.println("✅ Total tours retrieved: " + tours.size());

        } catch (SQLException ex) {
            System.out.println("❌ SQL Query Error: " + ex.getMessage());
        }
        return tours;
    }

    public Tour getTourById(int id) {
        Tour tour = null;
        String sql = "SELECT * FROM Tour WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tour = new Tour(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getBigDecimal("price"),
                            rs.getString("transport"),
                            rs.getString("description"),
                            rs.getDate("startDate"),
                            rs.getDate("endDate"),
                            rs.getString("image_url") // chỉnh thành image_url
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tour;
    }

    public List<Tour> getToursByFilter(String type, int offset, int limit) {
        List<Tour> tours = new ArrayList<>();
        String sql = "SELECT DISTINCT * FROM Tour "
                + "WHERE (? IS NULL OR type = ?) "
                + "ORDER BY startDate ASC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            stmt.setString(2, type);
            stmt.setInt(3, offset);
            stmt.setInt(4, limit);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tours.add(new Tour(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getBigDecimal("price"),
                        rs.getString("transport"),
                        rs.getString("description"),
                        rs.getDate("startDate"),
                        rs.getDate("endDate"),
                        rs.getString("image_url"),
                        rs.getString("type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tours;
    }

    public List<Tour> getSortedTours(String type, String sort, int offset, int limit) {
        List<Tour> tours = new ArrayList<>();
        String sql = "SELECT DISTINCT id, name, location, price, transport, description, startDate, endDate, image_url, type "
                + "FROM Tour WHERE (? IS NULL OR (type IS NULL OR UPPER(type) = UPPER(?))) ";

        if ("priceAsc".equals(sort)) {
            sql += "ORDER BY price ASC ";
        } else if ("priceDesc".equals(sort)) {
            sql += "ORDER BY price DESC ";
        } else {
            sql += "ORDER BY id ASC ";
        }
        sql += "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            stmt.setString(2, type);
            stmt.setInt(3, offset);
            stmt.setInt(4, limit);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tours.add(new Tour(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getBigDecimal("price"),
                        rs.getString("transport"),
                        rs.getString("description"),
                        rs.getDate("startDate"),
                        rs.getDate("endDate"),
                        rs.getString("image_url"),
                        rs.getString("type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tours;
    }

    public int getTotalTours(String tourType) {
        String sql = "SELECT COUNT(DISTINCT id) FROM Tour WHERE (? IS NULL OR (type IS NULL OR UPPER(type) = UPPER(?)))";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tourType);
            stmt.setString(2, tourType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Tour> searchTours(String location, java.sql.Date startDate, java.sql.Date endDate) {
        List<Tour> tours = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Tour WHERE 1=1");
        if (location != null && !location.isEmpty()) {
            sql.append(" AND location = ?");
        }
        if (startDate != null) {
            sql.append(" AND startDate >= ?");
        }
        if (endDate != null) {
            sql.append(" AND startDate <= ?");
        }
        sql.append(" ORDER BY startDate ASC");
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (location != null && !location.isEmpty()) {
                stmt.setString(idx++, location);
            }
            if (startDate != null) {
                stmt.setDate(idx++, startDate);
            }
            if (endDate != null) {
                stmt.setDate(idx++, endDate);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tours.add(new Tour(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getBigDecimal("price"),
                        rs.getString("transport"),
                        rs.getString("description"),
                        rs.getDate("startDate"),
                        rs.getDate("endDate"),
                        rs.getString("image_url"),
                        rs.getString("type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tours;
    }

    public static void main(String[] args) {
        TourDAO tourDAO = new TourDAO();
        List<Tour> tours = tourDAO.getAllTours();
        if (tours == null || tours.isEmpty()) {
            System.out.println("No tours found or database connection failed.");
        } else {
            System.out.println("Total tours found: " + tours.size());
            for (Tour tour : tours) {
                System.out.println("-----------------------------");
                System.out.println("ID: " + tour.getId());
                System.out.println("Tour Name: " + tour.getName());
                System.out.println("Location: " + tour.getLocation());
                System.out.println("Price: " + tour.getPrice());
                System.out.println("Transport: " + tour.getTransport());
                System.out.println("Description: " + tour.getDescription());
                System.out.println("Start Date: " + tour.getStartDate());
                System.out.println("End Date: " + tour.getEndDate());
                System.out.println("Image: " + tour.getImageUrl());
            }
        }
    }
}
