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
import model.AdminBooking;

/**
 *
 * @author kloane
 */
public class AdminBookingDAO extends DBContext {

    // Method to retrieve all bookings from the database
    public List<AdminBooking> getAllBookings() {
        // Initialize an empty list to store the booking records
        List<AdminBooking> bookingList = new ArrayList<>();

        // SQL query to fetch booking details by joining Booking, User, and Tour tables
        String query = "SELECT b.id AS Booking_ID, u.full_name AS Customer_Name, "
                + "u.phone_number AS Phone, u.email AS Email, "
                + "t.name AS Tour_Name, t.location AS Destination, "
                + "t.price AS Price, t.transport AS Transport, "
                + "b.bookingDate AS Booking_Date, b.numberOfPeople AS Number_Of_People, "
                + "b.status AS Status, t.startDate AS Departure, t.endDate AS End_Date "
                + "FROM Booking b "
                + "JOIN [User] u ON b.userId = u.id "
                + "JOIN Tour t ON b.tourId = t.id";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement ps = conn.prepareStatement(query); // Prepare the SQL query
                  ResultSet rs = ps.executeQuery()) {  // Execute the query and get the result set

            // Loop through each row in the result set
            while (rs.next()) {
                // Create a new AdminBooking object for each row
                AdminBooking bookings = new AdminBooking();
                // Set the booking ID
                bookings.setId(rs.getInt("Booking_ID"));
                // Set the customer's full name
                bookings.setCustomerName(rs.getString("Customer_Name"));
                // Set the customer's phone number
                bookings.setPhone(rs.getString("Phone"));
                // Set the customer's email
                bookings.setEmail(rs.getString("Email"));
                // Set the tour name
                bookings.setTourName(rs.getString("Tour_Name"));
                // Set the tour destination
                bookings.setDestination(rs.getString("Destination"));
                // Set the tour price
                bookings.setPrice(rs.getDouble("Price"));
                // Set the mode of transport for the tour
                bookings.setTransport(rs.getString("Transport"));
                // Set the booking date
                bookings.setBookingDate(rs.getDate("Booking_Date"));
                // Set the number of people in the booking
                bookings.setNumberOfPeople(rs.getInt("Number_Of_People"));
                // Set the booking status
                bookings.setStatus(rs.getString("Status"));
                // Set the departure date of the tour
                bookings.setDeparture(rs.getDate("Departure"));
                // Set the end date of the tour
                bookings.setEndDate(rs.getDate("End_Date"));

                // Calculate the total amount by multiplying the price by the number of people
                bookings.setTotalAmount(bookings.getPrice() * bookings.getNumberOfPeople());

                // Add the booking object to the list
                bookingList.add(bookings);
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return the list of bookings
        return bookingList;
    }

    // Method to update the status of a booking in the database
    public void updateBookingStatus(int bookingId, String newStatus) {
        // SQL query to update the status of a booking based on its ID
        String query = "UPDATE Booking SET status = ? WHERE id = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement ps = conn.prepareStatement(query)) {  // Prepare the SQL query
            // Set the new status as the first parameter in the query
            ps.setString(1, newStatus);
            // Set the booking ID as the second parameter in the query
            ps.setInt(2, bookingId);
            // Execute the update query
            ps.executeUpdate();
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
    }

// Method to retrieve bookings for a specific month and year
    public List<AdminBooking> getBookingsByMonth(int month, int year) {
        // Initialize an empty list to store the booking records
        List<AdminBooking> bookingList = new ArrayList<>();

        // SQL query to fetch bookings for a specific month and year by joining Booking, User, and Tour tables
        String query = "SELECT b.id AS Booking_ID, "
                + "u.full_name AS Customer_Name, "
                + "u.phone_number AS Phone, "
                + "u.email AS Email, "
                + "t.name AS Tour_Name, "
                + "t.location AS Destination, "
                + "t.price AS Price, "
                + "b.bookingDate AS Booking_Date, "
                + "b.numberOfPeople AS Number_Of_People, "
                + "b.status AS Payment_Status, "
                + "t.startDate AS Departure "
                + "FROM Booking b "
                + "JOIN [User] u ON b.userId = u.id "
                + "JOIN Tour t ON b.tourId = t.id "
                + "WHERE MONTH(b.bookingDate) = ? AND YEAR(b.bookingDate) = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement ps = conn.prepareStatement(query)) {  // Prepare the SQL query
            // Set the month as the first parameter in the query
            ps.setInt(1, month);
            // Set the year as the second parameter in the query
            ps.setInt(2, year);
            // Execute the query and get the result set
            ResultSet rs = ps.executeQuery();

            // Loop through each row in the result set
            while (rs.next()) {
                // Create a new AdminBooking object for each row
                AdminBooking booking = new AdminBooking();
                // Set the booking ID
                booking.setId(rs.getInt("Booking_ID"));
                // Set the customer's full name
                booking.setCustomerName(rs.getString("Customer_Name"));
                // Set the customer's phone number
                booking.setPhone(rs.getString("Phone"));
                // Set the customer's email
                booking.setEmail(rs.getString("Email"));
                // Set the tour name
                booking.setTourName(rs.getString("Tour_Name"));
                // Set the tour destination
                booking.setDestination(rs.getString("Destination"));
                // Set the tour price
                booking.setPrice(rs.getDouble("Price"));
                // Set the booking date
                booking.setBookingDate(rs.getDate("Booking_Date"));
                // Set the number of people in the booking
                booking.setNumberOfPeople(rs.getInt("Number_Of_People"));
                // Set the payment status
                booking.setPaymentStatus(rs.getString("Payment_Status"));
                // Set the departure date of the tour
                booking.setDeparture(rs.getDate("Departure"));

                // Add the booking object to the list
                bookingList.add(booking);
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return the list of bookings for the specified month and year
        return bookingList;
    }
}
