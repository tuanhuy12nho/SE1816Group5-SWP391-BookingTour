package DAO;

import db.DBContext;
import model.Itinerary;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItineraryDAO extends DBContext {
    public List<Itinerary> getItinerariesByTourId(int tourId) {
        List<Itinerary> list = new ArrayList<>();
        String sql = "SELECT * FROM Itinerary WHERE tourId = ? ORDER BY day_number ASC";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tourId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Itinerary i = new Itinerary(
                        rs.getInt("id"),
                        rs.getInt("tourId"),
                        rs.getInt("day_number"),
                        rs.getString("description"));
                list.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addItinerary(Itinerary itinerary) {
        String sql = "INSERT INTO Itinerary (tourId, day_number, description) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itinerary.getTourId());
            ps.setInt(2, itinerary.getDayNumber());
            ps.setString(3, itinerary.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItinerary(Itinerary itinerary) {
        String sql = "UPDATE Itinerary SET day_number = ?, description = ? WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itinerary.getDayNumber());
            ps.setString(2, itinerary.getDescription());
            ps.setInt(3, itinerary.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteItinerary(int id) {
        String sql = "DELETE FROM Itinerary WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Itinerary getItineraryById(int id) {
        Itinerary itinerary = null;
        String sql = "SELECT * FROM Itinerary WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                itinerary = new Itinerary(
                        rs.getInt("id"),
                        rs.getInt("tourId"),
                        rs.getInt("day_number"),
                        rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itinerary;
    }

}
