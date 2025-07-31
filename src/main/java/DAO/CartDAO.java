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
                cart.setNumberOfChild(rs.getInt("numberOfChild"));
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
        String sql = "INSERT INTO Cart (userId, tourId, numberOfPeople, numberOfChild, addedDate, totalPrice, status) VALUES (?, ?, ?, ?, ?, ?, N'In Cart')";
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
            ps.setInt(4, cart.getNumberOfChild());
            ps.setTimestamp(5, cart.getAddedDate());
            ps.setBigDecimal(6, cart.getTotalPrice());
            int rowsAffected = ps.executeUpdate();
            System.out.println(
                    "Added to cart successfully: " + rowsAffected + " rows affected. UserID=" + cart.getUserId() +
                            ", TourID=" + cart.getTourId() + ", People=" + cart.getNumberOfPeople() + 
                            ", Children=" + cart.getNumberOfChild() + ", Price=" + cart.getTotalPrice());
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
        String sql = "UPDATE Cart SET numberOfPeople = ?, numberOfChild = ?, totalPrice = ? WHERE id = ? AND userId = ?";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cart.getNumberOfPeople());
            ps.setInt(2, cart.getNumberOfChild());
            ps.setBigDecimal(3, cart.getTotalPrice());
            ps.setInt(4, cart.getId());
            ps.setInt(5, cart.getUserId());
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
                cart.setNumberOfChild(rs.getInt("numberOfChild"));
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

    /**
     * Get multiple carts by their IDs
     * @param cartIds List of cart IDs to fetch
     * @param userId User ID for security (only fetch carts belonging to this user)
     * @return List of Cart objects
     */
    public List<Cart> getCartsByIds(List<Integer> cartIds, int userId) {
        List<Cart> carts = new ArrayList<>();
        
        if (cartIds == null || cartIds.isEmpty()) {
            System.out.println("No cart IDs provided");
            return carts;
        }
        
        // Build SQL query with IN clause
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT c.*, t.image_url as tourImage, t.name as tourName, ");
        sqlBuilder.append("t.description as tourDescription, t.price as tourPrice ");
        sqlBuilder.append("FROM Cart c ");
        sqlBuilder.append("JOIN Tour t ON c.tourId = t.id ");
        sqlBuilder.append("WHERE c.userId = ? AND c.id IN (");
        
        // Add placeholders for each cart ID
        for (int i = 0; i < cartIds.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(",");
            }
            sqlBuilder.append("?");
        }
        sqlBuilder.append(")");
        
        String sql = sqlBuilder.toString();
        System.out.println("Executing query: " + sql + " for cartIds: " + cartIds);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set the user ID
            ps.setInt(1, userId);
            
            // Set all cart IDs as parameters
            for (int i = 0; i < cartIds.size(); i++) {
                ps.setInt(i + 2, cartIds.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            int count = 0;

            while (rs.next()) {
                Cart cart = new Cart();
                cart.setId(rs.getInt("id"));
                cart.setUserId(rs.getInt("userId"));
                cart.setTourId(rs.getInt("tourId"));
                cart.setNumberOfPeople(rs.getInt("numberOfPeople"));
                cart.setNumberOfChild(rs.getInt("numberOfChild"));
                cart.setAddedDate(rs.getTimestamp("addedDate"));
                cart.setTotalPrice(rs.getBigDecimal("totalPrice"));
                cart.setStatus(rs.getString("status"));
                cart.setPaymentMethod(rs.getString("paymentMethod"));
                cart.setTourImage(rs.getString("tourImage"));
                carts.add(cart);
                count++;
                
                System.out.println("Found cart ID: " + cart.getId() + ", tourId: " + cart.getTourId());
            }
            System.out.println("Found " + count + " carts out of " + cartIds.size() + " requested IDs");
        } catch (SQLException e) {
            System.out.println("Error retrieving cart items by IDs: " + e.getMessage());
            e.printStackTrace();
        }
        
        return carts;
    }
}
