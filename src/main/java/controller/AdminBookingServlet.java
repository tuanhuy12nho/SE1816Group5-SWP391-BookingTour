/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.AdminBookingDAO;
import model.Booking;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.AdminBooking;
import util.SessionLogin;

@WebServlet(name = "AdminBookingServlet", urlPatterns = {"/AdminBookingServlet"})
public class AdminBookingServlet extends HttpServlet {
// Declare an instance of AdminBookingDAO to interact with the booking database

    private AdminBookingDAO bookingDAO;

    // Initialize the servlet and set up the AdminBookingDAO instance
    @Override
    public void init() {
        bookingDAO = new AdminBookingDAO();
    }

    // Method to handle HTTP GET requests for managing bookings
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if the user is an admin; if not, redirect to the admin login page
        if (!SessionLogin.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin.jsp");
            return;
        }

        // Get the action parameter from the request to determine the operation
        String action = request.getParameter("action");

        // Handle the request based on the action
        if ("search".equals(action)) {
            // If the action is "search", handle the search operation
            handleSearch(request, response);
        } else {
            // Otherwise, display the list of all bookings
            handleListBookings(request, response);
        }
    }

    // Method to handle HTTP POST requests for managing bookings
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the action parameter from the request to determine the operation
        String action = request.getParameter("action");

        if ("updateStatus".equals(action)) {
            int bookingId = Integer.parseInt(request.getParameter("id"));
            String newStatus = request.getParameter("status");

            // Cập nhật trạng thái trong cơ sở dữ liệu
            bookingDAO.updateBookingStatus(bookingId, newStatus);
            response.sendRedirect("AdminBookingServlet");
        }
    }

    private void handleListBookings(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<AdminBooking> bookings = bookingDAO.getAllBookings();
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("Admin/booking.jsp").forward(request, response);
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int month = Integer.parseInt(request.getParameter("month"));
            int year = Integer.parseInt(request.getParameter("year"));

            List<AdminBooking> bookings = bookingDAO.getBookingsByMonth(month, year);
            request.setAttribute("bookings", bookings);
            request.setAttribute("month", month);
            request.setAttribute("year", year);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Please enter the correct format for month and year.");
        }
        request.getRequestDispatcher("Admin/booking.jsp").forward(request, response);
    }

}
