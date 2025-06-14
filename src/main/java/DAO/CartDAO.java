package DAO;

import db.DBContext;
import model.Cart;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartDAO extends DBContext {
    public List<Cart> getCartByUserId(int userId) {
        List<Cart> carts = new ArrayList<>();
        String sql = "SELECT c.*, t.image_url as tourImage, t.name as tourName, " +
                "t.description as tourDescription, t.price as tourPrice " +
                "FROM Cart c " +
                "JOIN Tour t ON c.tourId = t.id " +
                "WHERE c.userId = ? AND c.status = N'In Cart'";

        System.out.println("Finding cart items for user ID: " + userId); // Debug log

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            int count = 0;

            while (rs.next()) {
                Cart cart = new Cart();
                cart.setId(rs.getInt("id"));
                cart.setUserId(rs.getInt("userId"));
                cart.setTourId(rs.getInt("tourId"));
                cart.setNumberOfPeople(rs.getInt("numberOfPeople"));
                cart.setAddedDate(rs.getTimestamp("addedDate"));
                cart.setTotalPrice(rs.getBigDecimal("totalPrice"));
                cart.setStatus(rs.getString("status"));
                cart.setPaymentMethod(rs.getString("paymentMethod"));
                cart.setTourImage(rs.getString("tourImage"));
                carts.add(cart);
                count++;
            }
            System.out.println("Found " + count + " cart items for user ID: " + userId);
        } catch (SQLException e) {
            System.out.println("Error retrieving cart items: " + e.getMessage());
            e.printStackTrace();
        }
        return carts;
    }

    public void addToCart(Cart cart) {
        String sql = "INSERT INTO Cart (userId, tourId, numberOfPeople, addedDate, totalPrice, status) VALUES (?, ?, ?, ?, ?, N'In Cart')";
        System.out.println("Attempting to add to cart: UserID=" + cart.getUserId() + ", TourID=" + cart.getTourId());
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            if (conn == null) {
                System.out.println("ERROR: Database connection is null!");
                return;
            }

            ps = conn.prepareStatement(sql);
            ps.setInt(1, cart.getUserId());
            ps.setInt(2, cart.getTourId());
            ps.setInt(3, cart.getNumberOfPeople());
            ps.setTimestamp(4, cart.getAddedDate());
            ps.setBigDecimal(5, cart.getTotalPrice());
            int rowsAffected = ps.executeUpdate();
            System.out.println(
                    "Added to cart successfully: " + rowsAffected + " rows affected. UserID=" + cart.getUserId() +
                            ", TourID=" + cart.getTourId() + ", People=" + cart.getNumberOfPeople() + ", Price="
                            + cart.getTotalPrice());
        } catch (SQLException e) {
            System.out.println("SQL Exception in addToCart: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                // We don't close the connection here as it's managed by the connection pool
            } catch (SQLException e) {
                System.out.println("Error closing statement: " + e.getMessage());
            }
        }
    }

    public void updateCart(Cart cart) {
        String sql = "UPDATE Cart SET numberOfPeople = ?, totalPrice = ? WHERE id = ? AND userId = ?";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cart.getNumberOfPeople());
            ps.setBigDecimal(2, cart.getTotalPrice());
            ps.setInt(3, cart.getId());
            ps.setInt(4, cart.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFromCart(int cartId, int userId) {
        String sql = "DELETE FROM Cart WHERE id = ? AND userId = ?";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cart getCartById(int cartId) {
        String sql = "SELECT * FROM Cart WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cart cart = new Cart();
                cart.setId(rs.getInt("id"));
                cart.setUserId(rs.getInt("userId"));
                cart.setTourId(rs.getInt("tourId"));
                cart.setNumberOfPeople(rs.getInt("numberOfPeople"));
                cart.setAddedDate(rs.getTimestamp("addedDate"));
                cart.setTotalPrice(rs.getBigDecimal("totalPrice"));
                cart.setStatus(rs.getString("status"));
                cart.setPaymentMethod(rs.getString("paymentMethod"));
                return cart;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
