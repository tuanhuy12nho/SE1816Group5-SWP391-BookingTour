/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import db.DBContext;
import model.PasswordToken;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Data Access Object for PasswordToken operations
 * 
 * @author ACER
 */
public class PasswordTokenDAO extends DBContext {

    /**
     * Create a new password reset token for a user
     * 
     * @param userId            The ID of the user
     * @param expirationMinutes Token expiration time in minutes
     * @return The generated token string
     */
    public String createPasswordToken(int userId, int expirationMinutes) {
        String token = UUID.randomUUID().toString();
        String query = "INSERT INTO PasswordToken (userId, token, expired_at, is_used) VALUES (?, ?, ?, ?)";

        // Calculate expiration time
        Timestamp expiredAt = new Timestamp(System.currentTimeMillis() + (expirationMinutes * 60 * 1000));

        try (Connection conn = new DBContext().getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setTimestamp(3, expiredAt);
            ps.setBoolean(4, false);

            int result = ps.executeUpdate();
            if (result > 0) {
                return token;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Find a password token by token string
     * 
     * @param token The token string
     * @return PasswordToken object or null if not found
     */
    public PasswordToken findByToken(String token) {
        String query = "SELECT * FROM PasswordToken WHERE token = ?";

        try (Connection conn = new DBContext().getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PasswordToken passwordToken = new PasswordToken();
                passwordToken.setId(rs.getInt("id"));
                passwordToken.setUserId(rs.getInt("userId"));
                passwordToken.setToken(rs.getString("token"));
                passwordToken.setExpiredAt(rs.getTimestamp("expired_at"));
                passwordToken.setUsed(rs.getBoolean("is_used"));
                return passwordToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mark a token as used
     * 
     * @param token The token string
     * @return true if successful, false otherwise
     */
    public boolean markTokenAsUsed(String token) {
        String query = "UPDATE PasswordToken SET is_used = 1 WHERE token = ?";

        try (Connection conn = new DBContext().getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, token);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete expired tokens (cleanup method)
     * 
     * @return Number of deleted tokens
     */
    public int deleteExpiredTokens() {
        String query = "DELETE FROM PasswordToken WHERE expired_at < GETDATE()";

        try (Connection conn = new DBContext().getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Delete all tokens for a specific user
     * 
     * @param userId The user ID
     * @return true if successful, false otherwise
     */
    public boolean deleteTokensByUserId(int userId) {
        String query = "DELETE FROM PasswordToken WHERE userId = ?";

        try (Connection conn = new DBContext().getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            int result = ps.executeUpdate();
            return result >= 0; // Return true even if no tokens were deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Validate if a token is valid (exists, not used, not expired)
     * 
     * @param token The token string
     * @return true if valid, false otherwise
     */
    public boolean isTokenValid(String token) {
        PasswordToken passwordToken = findByToken(token);
        return passwordToken != null && passwordToken.isValid();
    }
}
