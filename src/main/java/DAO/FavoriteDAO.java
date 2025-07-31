/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import db.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Favorite;
import model.Review;
import model.Tour;

/**
 *
 * @author ADMIN
 */
public class FavoriteDAO extends DBContext {

    TourDAO tourDao = new TourDAO();

    public List<Favorite> getAllMyFavorites(int user_id) {
        List<Favorite> favorites = new ArrayList<>();
        String sql = "SELECT * FROM Favorite f  JOIN [Tour] t ON f.tourId = t.id where f.userId = ?";

        try ( Connection conn = getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Favorite fav = new Favorite();
                fav.setId(rs.getInt("id"));
                fav.setUserId(rs.getInt("userId"));
                Tour tour = tourDao.getTourById(rs.getInt("tourId"));
                fav.setTour(tour);
                favorites.add(fav);
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return favorites;
    }
    
       public boolean haveLikeInMyFavorites(int user_id,int tour_id) {
        String sql = "SELECT * FROM Favorite f  JOIN [Tour] t ON f.tourId = t.id where f.userId = ? and f.tourId = ?";

        try ( Connection conn = getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user_id);
            stmt.setInt(2, tour_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
               return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Favorite> getFavoritesWithPagination(int userId, int offset, int limit) {
    List<Favorite> favorites = new ArrayList<>();
    String sql = "SELECT * FROM Favorite f JOIN [Tour] t ON f.tourId = t.id WHERE f.userId = ? ORDER BY f.id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        stmt.setInt(2, offset);
        stmt.setInt(3, limit);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Favorite fav = new Favorite();
            fav.setId(rs.getInt("id"));
            fav.setUserId(rs.getInt("userId"));
            Tour tour = tourDao.getTourById(rs.getInt("tourId"));
            fav.setTour(tour);
            favorites.add(fav);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return favorites;
}
    public int getTotalFavoriteCount(int userId) {
    String sql = "SELECT COUNT(*) FROM Favorite WHERE userId = ?";
    try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}



    public boolean addFavorite(Favorite favorite) {
        String sql = "INSERT INTO Favorite (userId, tourId) VALUES (?, ?)";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query
            stmt.setInt(1, favorite.getUserId());
            stmt.setInt(2, favorite.getTourId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeFavorite(Favorite fav) {
        String sql = "DELETE FROM Favorite WHERE  userId = ? and tourId = ?";

        try ( Connection conn = getConnection(); 
                PreparedStatement stmt = conn.prepareStatement(sql)) {

             stmt.setInt(1, fav.getUserId());
            stmt.setInt(2, fav.getTourId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting favorite: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
      public boolean removeFavoriteById(int id) {
        String sql = "DELETE FROM Favorite WHERE id = ?";

        try ( Connection conn = getConnection(); 
                PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting favorite: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean haveExistTourFavorite(int user_id, int tour_id) {
        String sql = "Select * from Favorite where user_id = ? and tour_id = ?";
        try ( Connection conn = getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query
            stmt.setInt(1, user_id);
            stmt.setInt(2, tour_id);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return false;
    }
}
