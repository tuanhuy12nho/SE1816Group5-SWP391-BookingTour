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
import model.Booking;

/**
 *
 * @author kloane
 */
public class AdminStatisticsDAO extends DBContext {

    // Method to calculate the total revenue by month and type
    public double getRevenueByMonth(int month, int year, String tourType) {
        double revenue = 0;
        String query = "SELECT COALESCE(SUM(t.price * b.numberOfPeople), 0) AS total_revenue "
                + "FROM Booking b "
                + "JOIN Tour t ON b.tourId = t.id "
                + "WHERE MONTH(b.bookingDate) = ? "
                + "AND YEAR(b.bookingDate) = ? "
                + "AND UPPER(t.type) = UPPER(?) "
                + "AND b.status = 'Confirmed'";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ps.setString(3, tourType);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                revenue = rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }

    // Method to get booking count by month and type
    public int getBookingCountByMonth(int month, int year, String tourType) {
        String query = "SELECT COUNT(*) AS total_bookings "
                + "FROM Booking b "
                + "JOIN Tour t ON b.tourId = t.id "
                + "WHERE MONTH(b.bookingDate) = ? "
                + "AND YEAR(b.bookingDate) = ? "
                + "AND UPPER(t.type) = UPPER(?) "
                + "AND b.status = 'Confirmed'";

        int count = 0;
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ps.setString(3, tourType);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total_bookings");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Method to get detailed revenue information by month and type
    public List<AdminBooking> getRevenueDetailsByMonth(int month, int year, String tourType) {
        List<AdminBooking> bookingList = new ArrayList<>();

        String query = "SELECT "
                + "b.id AS Booking_ID, "
                + "u.full_name AS Customer_Name, "
                + "u.phone_number AS Phone, "
                + "u.email AS Email, "
                + "t.startDate AS Departure, "
                + "t.location AS Destination, "
                + "t.price AS Price, "
                + "b.numberOfPeople AS NumberOfPeople, "
                + "b.status AS Booking_Status, "
                + "t.type AS Tour_Type, "
                + "b.bookingDate AS Booking_Date "
                + "FROM Booking b "
                + "JOIN Tour t ON b.tourId = t.id "
                + "JOIN [User] u ON b.userId = u.id "
                + "WHERE MONTH(b.bookingDate) = ? "
                + "AND YEAR(b.bookingDate) = ? "
                + "AND UPPER(t.type) = UPPER(?) "
                + "AND b.status = 'Confirmed' "
                + "ORDER BY b.bookingDate DESC"; // Added sorting for better presentation

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ps.setString(3, tourType);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AdminBooking booking = new AdminBooking();
                booking.setId(rs.getInt("Booking_ID"));
                booking.setCustomerName(rs.getString("Customer_Name"));
                booking.setPhone(rs.getString("Phone"));
                booking.setEmail(rs.getString("Email"));
                booking.setDeparture(rs.getDate("Departure"));
                booking.setDestination(rs.getString("Destination"));
                booking.setPrice(rs.getDouble("Price"));
                booking.setNumberOfPeople(rs.getInt("NumberOfPeople"));
                booking.setTotalAmount(rs.getDouble("Price") * rs.getInt("NumberOfPeople"));
                booking.setPaymentStatus(rs.getString("Booking_Status"));
                booking.setTourType(rs.getString("Tour_Type"));
                booking.setBookingDate(rs.getDate("Booking_Date"));
                bookingList.add(booking);
            }

            // Debug statement to confirm data retrieval
            System.out.println("📢 DEBUG: Found " + bookingList.size() + " bookings for " + month + "/" + year
                    + " type: " + tourType);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    // Method to get all revenue details without filtering
    public List<AdminBooking> getAllRevenueDetails() {
        List<AdminBooking> bookingList = new ArrayList<>();

        String query = "SELECT "
                + "b.id AS Booking_ID, "
                + "u.full_name AS Customer_Name, "
                + "u.phone_number AS Phone, "
                + "u.email AS Email, "
                + "t.startDate AS Departure, "
                + "t.location AS Destination, "
                + "t.price AS Price, "
                + "b.numberOfPeople AS NumberOfPeople, "
                + "b.status AS Booking_Status, "
                + "t.type AS Tour_Type, "
                + "b.bookingDate AS Booking_Date "
                + "FROM Booking b "
                + "JOIN Tour t ON b.tourId = t.id "
                + "JOIN [User] u ON b.userId = u.id "
                + "WHERE b.status = 'Confirmed' "
                + "ORDER BY b.bookingDate DESC";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AdminBooking booking = new AdminBooking();
                booking.setId(rs.getInt("Booking_ID"));
                booking.setCustomerName(rs.getString("Customer_Name"));
                booking.setPhone(rs.getString("Phone"));
                booking.setEmail(rs.getString("Email"));
                booking.setDeparture(rs.getDate("Departure"));
                booking.setDestination(rs.getString("Destination"));
                booking.setPrice(rs.getDouble("Price"));
                booking.setNumberOfPeople(rs.getInt("NumberOfPeople"));
                booking.setTotalAmount(rs.getDouble("Price") * rs.getInt("NumberOfPeople"));
                booking.setPaymentStatus(rs.getString("Booking_Status"));
                booking.setTourType(rs.getString("Tour_Type"));
                booking.setBookingDate(rs.getDate("Booking_Date"));
                bookingList.add(booking);
            }

            System.out.println("📢 DEBUG: Found " + bookingList.size() + " total bookings.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    // Method to get total revenue for a specific month and year
    public double getTotalRevenueByMonth(int month, int year) {
        double revenue = 0;
        String query = "SELECT COALESCE(SUM(t.price * b.numberOfPeople), 0) AS total_revenue "
                + "FROM Booking b "
                + "JOIN Tour t ON b.tourId = t.id "
                + "WHERE MONTH(b.bookingDate) = ? " // Use bookingDate consistently
                + "AND YEAR(b.bookingDate) = ? " // Use bookingDate consistently
                + "AND b.status = 'Confirmed'";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                revenue = rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }
}
