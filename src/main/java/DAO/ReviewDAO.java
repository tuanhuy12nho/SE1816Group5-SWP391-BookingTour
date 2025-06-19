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
import java.util.Vector;
import model.Review;

/**
 *
 * @author Admin
 */
public class ReviewDAO extends DBContext {

    // Method to retrieve a list of reviews for a specific tour by its ID
    public List<Review> getReviewsByTourId(int tourId) {
        // Initialize an empty list to store the review records
        List<Review> reviews = new ArrayList<>();
        // SQL query to fetch reviews for a tour by joining Review and User tables
        String sql = "SELECT r.id, r.userId, r.tourId, r.rating, r.comment, r.reviewDate, u.username "
                + "FROM Review r JOIN [User] u ON r.userId = u.id "
                + "WHERE r.tourId = ? and status != 'hidden'";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query
            // Set the tour ID as the parameter in the query
            stmt.setInt(1, tourId);
            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Loop through each row in the result set
            while (rs.next()) {
                // Create a new Review object with the retrieved data and add it to the list
                reviews.add(new Review(
                        rs.getInt("id"), // Review ID
                        rs.getInt("userId"), // User ID
                        rs.getInt("tourId"), // Tour ID
                        rs.getInt("rating"), // Rating
                        rs.getString("comment"), // Comment
                        rs.getString("reviewDate"),// Review date
                        rs.getString("username") // Username of the reviewer
                ));
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return the list of reviews
        return reviews;
    }

    // Method to add a new review to the database
    public void addReview(Review review) {
        // SQL query to insert a new review into the Review table
        String sql = "INSERT INTO Review (userId, tourId, rating, comment, reviewDate, status) VALUES (?, ?, ?, ?, ?, ?)";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query
            // Set the parameters for the new review
            stmt.setInt(1, review.getUserId());      // User ID
            stmt.setInt(2, review.getTourId());      // Tour ID
            stmt.setInt(3, review.getRating());      // Rating
            stmt.setString(4, review.getComment());  // Comment
            stmt.setString(5, review.getReviewDate());  // Review date
            stmt.setString(6, "show");
            // Execute the insert query
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
    }

    // Method to update an existing review in the database
    public void updateReview(Review review) {
        // SQL query to update the rating and comment of a review based on its ID
        String sql = "UPDATE Review SET rating = ?, comment = ? WHERE id = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query
            // Set the parameters for the updated review
            stmt.setInt(1, review.getRating());      // Updated rating
            stmt.setString(2, review.getComment());  // Updated comment
            stmt.setInt(3, review.getId());          // Review ID

            // Execute the update query
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
    }

    // Method to delete a review from the database by its ID
    public void deleteReview(int reviewId) {
        // SQL query to delete a review by its ID
        String sql = "DELETE FROM Review WHERE id = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query
            // Set the review ID as the parameter in the query
            stmt.setInt(1, reviewId);
            // Execute the delete query
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
    }

    public Vector<Review> getReviewsByTour(
            int tourId,
            String commentKeyword,
            String userKeyword,
            String sortType,
            int pageIndex,
            int pageSize
    ) {
        Vector<Review> list = new Vector<>();
        StringBuilder sql = new StringBuilder(
                "SELECT r.*, u.username FROM Review r "
                + "JOIN [User] u ON r.userId = u.id "
                + "WHERE r.tourId = ? AND r.comment LIKE '%" + commentKeyword + "%' AND u.username LIKE '%" + userKeyword + "%'"
        );

        switch (sortType) {
            case "rating_asc":
                sql.append("ORDER BY r.rating ASC ");
                break;
            case "rating_desc":
                sql.append("ORDER BY r.rating DESC ");
                break;
            case "date_asc":
                sql.append("ORDER BY r.reviewDate ASC ");
                break;
            case "date_desc":
            default:
                sql.append("ORDER BY r.reviewDate DESC ");
                break;
        }

        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try {
            PreparedStatement st = conn.prepareStatement(sql.toString());
            st.setInt(1, tourId);
            st.setInt(2, (pageIndex - 1) * pageSize); // OFFSET
            st.setInt(3, pageSize); // FETCH NEXT

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Review r = new Review();
                r.setId(rs.getInt("id"));
                r.setStatus(rs.getString("status"));
                r.setUserId(rs.getInt("userId"));
                r.setTourId(rs.getInt("tourId"));
                r.setRating(rs.getInt("rating"));
                r.setComment(rs.getString("comment"));
                r.setReviewDate(rs.getString("reviewDate"));
                r.setUsername(rs.getString("username")); // giả sử bạn thêm thuộc tính này trong model

                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countReviewsByTour(int tourId, String commentKeyword, String userKeyword) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Review r "
                + "JOIN [User] u ON r.userId = u.id "
                + "WHERE r.tourId = ? "
                + "AND r.comment LIKE ? "
                + "AND u.username LIKE ?";

        try {
            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1, tourId);
            st.setString(2, "%" + commentKeyword + "%");
            st.setString(3, "%" + userKeyword + "%");

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public void hideReview(int reviewId, String status) {
        String sql = "UPDATE Review SET status = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, reviewId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
