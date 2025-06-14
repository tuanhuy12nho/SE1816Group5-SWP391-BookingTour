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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.AdminTour;
import model.Tour;

/**
 *
 * @author Admin
 */
public class AdminTourDAO {
    // Method to retrieve all tours from the database

    public ArrayList<AdminTour> getAllTours() {
        // Initialize an empty ArrayList to store the tour records
        ArrayList<AdminTour> tours = new ArrayList<>();
        // SQL query to select all distinct tours, ordered by start date in ascending order
        String query = "SELECT DISTINCT * FROM Tour ORDER BY startDate ASC";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement ps = conn.prepareStatement(query); // Prepare the SQL query
                  ResultSet rs = ps.executeQuery()) {  // Execute the query and get the result set

            // Loop through each row in the result set
            while (rs.next()) {
                // Create a new AdminTour object with the retrieved data
                AdminTour tour = new AdminTour(
                        rs.getInt("id"), // Tour ID
                        rs.getString("name"), // Tour name
                        rs.getString("location"), // Tour location
                        rs.getBigDecimal("price"), // Tour price
                        rs.getString("transport"), // Mode of transport
                        rs.getString("description"), // Tour description
                        rs.getDate("startDate"), // Start date
                        rs.getDate("endDate"), // End date
                        rs.getString("image_url"), // Image URL
                        rs.getString("type") // Tour type
                );
                // Add the tour object to the list
                tours.add(tour);
            }
        } catch (SQLException ex) {
            // Print an error message if a SQL query error occurs
            System.out.println("❌ SQL Query Error: " + ex.getMessage());
        }
        // Return the list of tours
        return tours;
    }

// Method to retrieve tours with filtering by type and pagination
    public List<AdminTour> getToursByFilter(String type, int offset, int limit) {
        // Initialize an empty list to store the tour records
        List<AdminTour> tours = new ArrayList<>();
        // SQL query to select distinct tours with filtering by type, ordered by start date, with pagination
        String sql = "SELECT DISTINCT * FROM Tour "
                + "WHERE (? IS NULL OR type = ?) "
                + "ORDER BY startDate ASC " // Order by departure date
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";  // Implement pagination

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query

            // Set the type parameter for filtering (can be null to select all types)
            stmt.setString(1, type);
            stmt.setString(2, type);
            // Set the offset for pagination
            stmt.setInt(3, offset);
            // Set the limit for pagination
            stmt.setInt(4, limit);

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();
            // Loop through each row in the result set
            while (rs.next()) {
                // Create a new AdminTour object with the retrieved data and add it to the list
                tours.add(new AdminTour(
                        rs.getInt("id"), // Tour ID
                        rs.getString("name"), // Tour name
                        rs.getString("location"), // Tour location
                        rs.getBigDecimal("price"), // Tour price
                        rs.getString("transport"), // Mode of transport
                        rs.getString("description"), // Tour description
                        rs.getDate("startDate"), // Start date
                        rs.getDate("endDate"), // End date
                        rs.getString("image_url"), // Image URL
                        rs.getString("type") // Tour type
                ));
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return the list of filtered tours
        return tours;
    }

// Method to retrieve tours with filtering by type, sorting, and pagination
    public List<AdminTour> getSortedTours(String type, String sort, int offset, int limit) {
        // Initialize an empty list to store the tour records
        List<AdminTour> tours = new ArrayList<>();
        // Base SQL query to select distinct tours with filtering by type
        String sql = "SELECT DISTINCT id, name, location, price, transport, description, startDate, endDate, image_url, type FROM Tour WHERE (? IS NULL OR type = ?) ";

        // Add sorting condition based on the sort parameter
        if ("priceAsc".equals(sort)) {
            sql += "ORDER BY price ASC ";  // Sort by price in ascending order
        } else if ("priceDesc".equals(sort)) {
            sql += "ORDER BY price DESC ";  // Sort by price in descending order
        } else {
            sql += "ORDER BY id ASC ";  // Default sorting by ID in ascending order
        }

        // Add pagination to the query
        sql += "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query

            // Set the type parameter for filtering (can be null to select all types)
            stmt.setString(1, type);
            stmt.setString(2, type);
            // Set the offset for pagination
            stmt.setInt(3, offset);
            // Set the limit for pagination
            stmt.setInt(4, limit);

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();
            // Loop through each row in the result set
            while (rs.next()) {
                // Create a new AdminTour object with the retrieved data and add it to the list
                tours.add(new AdminTour(
                        rs.getInt("id"), // Tour ID
                        rs.getString("name"), // Tour name
                        rs.getString("location"), // Tour location
                        rs.getBigDecimal("price"), // Tour price
                        rs.getString("transport"), // Mode of transport
                        rs.getString("description"), // Tour description
                        rs.getDate("startDate"), // Start date
                        rs.getDate("endDate"), // End date
                        rs.getString("image_url"), // Image URL
                        rs.getString("type") // Tour type
                ));
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return the list of sorted tours
        return tours;
    }

// Method to get the total number of tours, optionally filtered by type
    public int getTotalTours(String type) {
        // Initialize the SQL query based on whether a type filter is provided
        String sql;
        if (type == null || type.isEmpty()) {
            sql = "SELECT COUNT(*) FROM Tour";  // Count all tours if no type filter
        } else {
            sql = "SELECT COUNT(*) FROM Tour WHERE type = ?";  // Count tours of the specified type
        }

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query

            // If a type filter is provided, set the parameter
            if (type != null) {
                stmt.setString(1, type);
            }

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();
            // Check if there is a result and return the count
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return 0 if an error occurs or no tours are found
        return 0;
    }

// Method to retrieve a single tour by its ID
    public AdminTour getTourById(int id) {
        // Initialize the tour variable as null
        AdminTour tour = null;
        // SQL query to select a tour by its ID
        String query = "SELECT * FROM Tour WHERE id = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement ps = conn.prepareStatement(query)) {  // Prepare the SQL query

            // Set the tour ID as the parameter in the query
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {  // Execute the query and get the result set
                // Check if a tour is found
                if (rs.next()) {
                    // Create a new AdminTour object with the retrieved data
                    tour = new AdminTour(
                            rs.getInt("id"), // Tour ID
                            rs.getString("name"), // Tour name
                            rs.getString("location"), // Tour location
                            rs.getBigDecimal("price"), // Tour price
                            rs.getString("transport"), // Mode of transport
                            rs.getString("description"), // Tour description
                            rs.getDate("startDate"), // Start date
                            rs.getDate("endDate"), // End date
                            rs.getString("image_url") // Image URL
                    );
                }
            }
        } catch (SQLException e) {
            // Print an error message if a SQL query error occurs
            System.out.println("SQL Query Error (getTourById): " + e.getMessage());
        }
        // Return the tour object (or null if not found)
        return tour;
    }

// Method to add a new tour to the database
    public void addTour(AdminTour tour) {
        // SQL query to insert a new tour into the Tour table
        String sql = "INSERT INTO Tour (name, location, price, transport, description, startDate, endDate, image_url, type) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {  // Prepare the SQL query with generated keys

            // Set the parameters for the new tour
            stmt.setString(1, tour.getName());          // Tour name
            stmt.setString(2, tour.getLocation());      // Tour location
            stmt.setBigDecimal(3, tour.getPrice());     // Tour price
            stmt.setString(4, tour.getTransport());     // Mode of transport
            stmt.setString(5, tour.getDescription());   // Tour description
            stmt.setDate(6, new java.sql.Date(tour.getStartDate().getTime()));  // Start date
            stmt.setDate(7, new java.sql.Date(tour.getEndDate().getTime()));    // End date
            stmt.setString(8, tour.getImageUrl());      // Image URL
            stmt.setString(9, tour.getType());          // Tour type

            // Execute the insert query and get the number of affected rows
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                // Retrieve the generated ID for the new tour
                try ( ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // Set the generated ID to the tour object
                        tour.setId(generatedKeys.getInt(1));
                        // Print a success message with the new tour ID
                        System.out.println("✅ Tour added successfully with ID: " + tour.getId());
                    }
                }
            } else {
                // Print an error message if the tour could not be added
                System.out.println("Failed to add tour!");
            }
        } catch (SQLException e) {
            // Print an error message if a database error occurs
            System.out.println("Error adding tour: " + e.getMessage());
            e.printStackTrace();
        }
    }

// Method to update an existing tour in the database
    public void editTour(AdminTour tour) {
        // SQL query to update the details of a tour based on its ID
        String sql = "UPDATE Tour SET name=?, location=?, price=?, transport=?, description=?, startDate=?, endDate=?, image_url=?, type=? WHERE id=?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query
            // Set the parameters for the updated tour
            stmt.setString(1, tour.getName());          // Tour name
            stmt.setString(2, tour.getLocation());      // Tour location
            stmt.setBigDecimal(3, tour.getPrice());     // Tour price
            stmt.setString(4, tour.getTransport());     // Mode of transport
            stmt.setString(5, tour.getDescription());   // Tour description
            stmt.setDate(6, new java.sql.Date(tour.getStartDate().getTime()));  // Start date
            stmt.setDate(7, new java.sql.Date(tour.getEndDate().getTime()));    // End date
            stmt.setString(8, tour.getImageUrl());      // Image URL
            stmt.setString(9, tour.getType());          // Tour type
            stmt.setInt(10, tour.getId());              // Tour ID

            // Execute the update query and get the number of affected rows
            int affectedRows = stmt.executeUpdate();

            // Check if the update was successful
            if (affectedRows > 0) {
                // Print a success message if the tour was updated
                System.out.println("✅ Tour ID " + tour.getId() + " updated successfully!");
            } else {
                // Print an error message if the tour was not found
                System.out.println("Tour not found for update.");
            }
        } catch (SQLException e) {
            // Print an error message if a database error occurs
            System.out.println("Error updating tour: " + e.getMessage());
        }
    }

// Method to delete a tour from the database by its ID
    public void deleteTour(int id) {
        // SQL query to check if the tour exists
        String sqlCheck = "SELECT COUNT(*) FROM Tour WHERE id = ?";
        // SQL query to delete the tour by its ID
        String sqlDelete = "DELETE FROM Tour WHERE id = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck); // Prepare the check query
                  PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)) {  // Prepare the delete query

            // Check if the tour exists
            stmtCheck.setInt(1, id);
            ResultSet rs = stmtCheck.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                // Print an error message if the tour is not found
                System.out.println("❌ Tour not found for deletion, ID: " + id);
                return;
            }

            // Delete the tour (tourId in Booking & Review will automatically be set to NULL due to database constraints)
            stmtDelete.setInt(1, id);
            // Execute the delete query and get the number of deleted rows
            int rowsDeleted = stmtDelete.executeUpdate();

            // Check if the deletion was successful
            if (rowsDeleted > 0) {
                // Print a success message if the tour was deleted
                System.out.println("✅ Tour deleted successfully, ID: " + id);
            } else {
                // Print an error message if the tour was not found
                System.out.println("❌ Tour not found for deletion, ID: " + id);
            }
        } catch (SQLException e) {
            // Print an error message if a database error occurs
            System.out.println("Error deleting tour: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
