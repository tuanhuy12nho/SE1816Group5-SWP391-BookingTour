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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import model.Booking;
import model.Tour;
import model.User;

public class BookingDAO extends DBContext {

    // Method to retrieve a user by their ID from the database
    public User getUserById(int id) {
        // Initialize the user variable as null
        User user = null;
        // Use try-with-resources to automatically close database resources
        try ( Connection conn = getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User WHERE id = ?")) {  // Prepare the SQL query
            // Set the user ID as the parameter in the query
            stmt.setInt(1, id);
            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Check if a user is found
            if (rs.next()) {
                // Create a new User object with the retrieved data
                user = new User(
                        rs.getInt("id"), // User ID
                        rs.getString("fullName"), // Full name
                        rs.getString("email"), // Email
                        rs.getString("phone") // Phone number
                );
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return the user object (or null if not found)
        return user;
    }

    // Method to retrieve a tour by its ID from the database
    public Tour getTourById(int id) {
        // Initialize the tour variable as null
        Tour tour = null;
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
                    // Create a new Tour object with the retrieved data
                    tour = new Tour(
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
            System.out.println("❌ SQL Query Error (getTourById): " + e.getMessage());
        }
        // Return the tour object (or null if not found)
        return tour;
    }

    // Method to retrieve a booking by its ID from the database
    public Booking getBookingById(int bookingId) throws SQLException {
        // SQL query to fetch booking details by joining Booking, User, and Tour tables
        String query = "SELECT u.full_name, u.email, u.phone_number, u.gender, u.country, "
                + "t.name, t.location, t.price, t.transport, t.startDate, t.endDate, "
                + "b.id, b.bookingDate, b.status, b.numberOfPeople, b.pay "
                + "FROM Booking b "
                + "JOIN [User] u ON b.userId = u.id "
                + "JOIN Tour t ON b.tourId = t.id "
                + "WHERE b.id = ?";

        // Check if the database connection is null
        if (conn == null) {
            throw new SQLException("Connection is null!");
        }

        // Use try-with-resources to automatically close database resources
        try ( PreparedStatement ps = conn.prepareStatement(query)) {  // Prepare the SQL query
            // Set the booking ID as the parameter in the query
            ps.setInt(1, bookingId);
            try ( ResultSet rs = ps.executeQuery()) {  // Execute the query and get the result set
                // Check if a booking is found
                if (rs.next()) {
                    // Create and return a new Booking object with the retrieved data
                    return new Booking(
                            rs.getInt("id"), // Booking ID
                            rs.getString("full_name"), // User's full name
                            rs.getString("email"), // User's email
                            rs.getString("phone_number"), // User's phone number
                            rs.getString("gender"), // User's gender
                            rs.getString("country"), // User's country
                            rs.getString("name"), // Tour name
                            rs.getString("location"), // Tour location
                            rs.getString("transport"), // Tour transport
                            rs.getString("status"), // Booking status
                            rs.getDouble("price"), // Tour price
                            rs.getDate("startDate"), // Tour start date
                            rs.getDate("endDate"), // Tour end date
                            rs.getDate("bookingDate"), // Booking date
                            rs.getInt("numberOfPeople"), // Number of people
                            rs.getString("pay") // Payment method
                    );
                } else {
                    // Print a message if no booking is found
                    System.out.println("No booking found with ID: " + bookingId);
                }
            }
        }
        // Return null if no booking is found
        return null;
    }

    // Method to retrieve all bookings for a specific user by their ID
    public List<Booking> getBookingsByUserId(int userId) {
        // Initialize an empty list to store the booking records
        List<Booking> bookings = new ArrayList<>();
        // SQL query to fetch bookings for a user by joining Booking and Tour tables
        String query = "SELECT b.id, b.bookingDate, b.status, b.numberOfPeople, "
                + "t.name, t.location, t.price, t.transport, t.startDate, t.endDate, b.pay "
                + "FROM Booking b "
                + "JOIN Tour t ON b.tourId = t.id "
                + "WHERE b.userId = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = getConnection(); // Establish database connection
                  PreparedStatement ps = conn.prepareStatement(query)) {  // Prepare the SQL query
            // Set the user ID as the parameter in the query
            ps.setInt(1, userId);
            try ( ResultSet rs = ps.executeQuery()) {  // Execute the query and get the result set
                // Loop through each row in the result set
                while (rs.next()) {
                    // Create a new Booking object with the retrieved data and add it to the list
                    bookings.add(new Booking(
                            rs.getInt("id"), // Booking ID
                            rs.getDate("bookingDate"), // Booking date
                            rs.getString("status"), // Booking status
                            rs.getInt("numberOfPeople"), // Number of people
                            rs.getString("name"), // Tour name
                            rs.getString("location"), // Tour location
                            rs.getDouble("price"), // Tour price
                            rs.getString("transport"), // Tour transport
                            rs.getDate("startDate"), // Tour start date
                            rs.getDate("endDate"), // Tour end date
                            rs.getString("pay") // Payment method
                    ));
                }
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return the list of bookings
        return bookings;
    }

    // Method to execute a SELECT query and return the result set
    public ResultSet execSelectQuery(String query) {
        // Check if the database connection is null
        if (conn == null) {
            // Print an error message if there is no database connection
            System.out.println("Error: No database connection!");
            return null;
        }
        try {
            // Prepare and execute the SELECT query
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
            return null;
        }
    }

    // Method to retrieve specific details of a tour by its ID
    public Tour getTourDetails(int idTour) {
        // Initialize the tour variable as null
        Tour tour = null;
        // Use try-with-resources to automatically close database resources
        try ( Connection conn = getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement("SELECT id, name, price, description FROM Tour WHERE id = ?")) {  // Prepare the SQL query

            // Set the tour ID as the parameter in the query
            stmt.setInt(1, idTour);
            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Check if a tour is found
            if (rs.next()) {
                // Create a new Tour object with the retrieved data
                tour = new Tour(
                        rs.getInt("id"), // Tour ID
                        rs.getString("name"), // Tour name
                        rs.getBigDecimal("price"), // Tour price
                        rs.getString("description") // Tour description
                );
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return the tour object (or null if not found)
        return tour;
    }

    // Method to book a tour and insert the booking into the database
    public int bookTour(Booking booking) {
        // Initialize the new booking ID (default to 1 if the table is empty)
        int newId = 1;
        // Use try-with-resources to automatically close database resources
        try ( Connection conn = getConnection()) {  // Establish database connection

            // 1. Get the maximum booking ID and increment it by 1 to generate a new ID
            String getMaxIdQuery = "SELECT ISNULL(MAX(id), 0) + 1 AS NewId FROM Booking";
            try ( PreparedStatement getMaxIdStmt = conn.prepareStatement(getMaxIdQuery); // Prepare the query to get the max ID
                      ResultSet rs = getMaxIdStmt.executeQuery()) {  // Execute the query
                if (rs.next()) {
                    newId = rs.getInt("NewId");  // Set the new ID
                }
            }

            // Format the booking date to the required format (yyyy-MM-dd)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String bookingDate = sdf.format(booking.getBookingDate());

            // 2. Insert the new booking into the Booking table, including the payment method
            String insertQuery = "INSERT INTO Booking (userId, tourId, bookingDate, status, numberOfPeople, pay) VALUES (?, ?, ?, ?, ?, ?)";
            try ( PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {  // Prepare the insert query
                // Set the parameters for the new booking
                insertStmt.setInt(1, booking.getUserId());         // User ID
                insertStmt.setInt(2, booking.getTourId());         // Tour ID
                insertStmt.setString(3, bookingDate);              // Booking date
                insertStmt.setString(4, booking.getStatus());      // Booking status
                insertStmt.setInt(5, booking.getNumberOfPeople()); // Number of people
                insertStmt.setString(6, booking.getPay());         // Payment method

                // Execute the insert query and get the number of affected rows
                int affectedRows = insertStmt.executeUpdate();
                if (affectedRows > 0) {
                    return newId;  // Return the new booking ID if successful
                }
            }
        } catch (SQLException ex) {
            // Print an error message if a database error occurs
            System.out.println("Error booking tour: " + ex.getMessage());
        }
        // Return -1 if an error occurs
        return -1;
    }

    // Method to cancel a booking by updating its status to 'Cancelled'
    public boolean cancelBooking(int bookingId) {
        // SQL query to update the booking status to 'Cancelled' if the current status is 'Pending' or 'Confirmed'
        String sql = "UPDATE booking SET status = 'Cancelled' WHERE id = ? AND status IN ('Pending', 'Confirmed')";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query
            // Set the booking ID as the parameter in the query
            stmt.setInt(1, bookingId);
            // Execute the update query and get the number of affected rows
            int affectedRows = stmt.executeUpdate();
            // Return true if at least one row was affected (i.e., the booking was updated)
            return affectedRows > 0;
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
            return false;
        }
    }

    // Method to retrieve bookings for a specific user with pagination and sorting
    public List<Booking> getBookingsByUserIdWithPagination(int userId, int page, int pageSize, String sortBy) {
        // Initialize an empty list to store the booking records
        List<Booking> bookings = new ArrayList<>();
        // Base SQL query to fetch bookings for a user by joining Booking and Tour tables
        String sql = "SELECT b.id, b.bookingDate, b.status, b.numberOfPeople, "
                + "t.name, t.location, t.price, t.transport, t.startDate, t.endDate, b.pay "
                + "FROM Booking b "
                + "JOIN Tour t ON b.tourId = t.id "
                + "WHERE b.userId = ? ";

        // Handle sorting based on the sortBy parameter
        String orderByClause;
        if (sortBy == null) {
            sortBy = "bookingdate";  // Default sorting by booking date
        }
        switch (sortBy.toLowerCase()) {
            case "name":
                orderByClause = "ORDER BY t.name ASC";  // Sort by tour name in ascending order
                break;
            case "price":
                orderByClause = "ORDER BY t.price ASC";  // Sort by tour price in ascending order
                break;
            case "startdate":
                orderByClause = "ORDER BY t.startDate ASC";  // Sort by tour start date in ascending order
                break;
            case "bookingdate":
            default:
                orderByClause = "ORDER BY b.bookingDate DESC";  // Default sort by booking date in descending order
                break;
        }

        // Add pagination to the query
        sql = sql + orderByClause + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query
            // Calculate the offset based on the page number and page size
            int offset = (page - 1) * pageSize;
            // Set the parameters for the query
            stmt.setInt(1, userId);      // User ID
            stmt.setInt(2, offset);      // Offset for pagination
            stmt.setInt(3, pageSize);    // Number of rows to fetch

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();
            // Loop through each row in the result set
            while (rs.next()) {
                // Create a new Booking object with the retrieved data and add it to the list
                bookings.add(new Booking(
                        rs.getInt("id"), // Booking ID
                        rs.getDate("bookingDate"), // Booking date
                        rs.getString("status"), // Booking status
                        rs.getInt("numberOfPeople"), // Number of people
                        rs.getString("name"), // Tour name
                        rs.getString("location"), // Tour location
                        rs.getDouble("price"), // Tour price
                        rs.getString("transport"), // Tour transport
                        rs.getDate("startDate"), // Tour start date
                        rs.getDate("endDate"), // Tour end date
                        rs.getString("pay") // Payment method
                ));
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return the list of bookings
        return bookings;
    }

    // Method to count the total number of bookings for a specific user
    public int getTotalBookingsByUserId(int userId) {
        // SQL query to count the total number of bookings for a user
        String query = "SELECT COUNT(*) as total FROM Booking WHERE userId = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = getConnection(); // Establish database connection
                  PreparedStatement ps = conn.prepareStatement(query)) {  // Prepare the SQL query
            // Set the user ID as the parameter in the query
            ps.setInt(1, userId);
            try ( ResultSet rs = ps.executeQuery()) {  // Execute the query and get the result set
                // Check if a result is found
                if (rs.next()) {
                    // Return the total count of bookings
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return 0 if an error occurs or no bookings are found
        return 0;
    }
}
