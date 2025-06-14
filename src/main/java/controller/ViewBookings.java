/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.BookingDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Booking;
import model.User;

/**
 * Servlet for viewing and managing user bookings, including listing and canceling bookings.
 * @author Admin
 */
@WebServlet(name = "ViewBookings", urlPatterns = {"/ViewBookings"}) // Maps the servlet to the "/ViewBookings" URL
public class ViewBookings extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the current session to check user authentication
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Require user login; redirect to Login.jsp if not authenticated
        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // Get the user ID from the authenticated user
        int userId = user.getId();
        
        // Retrieve the "action" parameter to determine the requested operation
        String action = request.getParameter("action");

        // Handle the "viewBookings" action to display the user's bookings
        if ("viewBookings".equals(action)) {
            // Create an instance of BookingDAO to interact with the database
            BookingDAO bookingDAO = new BookingDAO();

            // Set up pagination variables
            int page = 1; // Default to the first page
            int pageSize = 5; // Number of bookings per page

            // Retrieve the requested page number from the request parameters
            if (request.getParameter("page") != null) {
                try {
                    page = Integer.parseInt(request.getParameter("page"));
                } catch (NumberFormatException e) {
                    page = 1; // Default to page 1 if the page parameter is invalid
                }
            }

            // Retrieve the sort criterion from the request; default to "bookingdate" if not provided
            String sortBy = request.getParameter("sortBy");
            if (sortBy == null || sortBy.isEmpty()) {
                sortBy = "bookingdate"; // Default sort by booking date
            }

            // Calculate the total number of bookings and pages for pagination
            int totalRecords = bookingDAO.getTotalBookingsByUserId(userId); // Assumes this method exists
            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

            // Ensure the page number is within valid bounds
            if (page > totalPages && totalPages > 0) {
                page = totalPages; // Set to last page if requested page exceeds total
            }
            if (page < 1) {
                page = 1; // Set to first page if requested page is less than 1
            }

            // Retrieve the list of bookings for the user with pagination and sorting
            List<Booking> bookingList = bookingDAO.getBookingsByUserIdWithPagination(userId, page, pageSize, sortBy);

            // Check if there are no bookings for the user
            if (bookingList == null || bookingList.isEmpty()) {
                request.setAttribute("error", "You have no bookings yet.");
            }

            // Set booking data and pagination details as request attributes for the JSP
            request.setAttribute("bookingList", bookingList); // List of bookings to display
            request.setAttribute("currentPage", page); // Current page number
            request.setAttribute("totalPages", totalPages); // Total number of pages
            request.setAttribute("sortBy", sortBy); // Current sort criterion

            // Forward the request to bookingList.jsp to display the bookings
            request.getRequestDispatcher("bookingList.jsp").forward(request, response);
        } else {
            // Redirect to the booking servlet if an invalid action is provided
            response.sendRedirect("/booking");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the current session to check user authentication
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Require user login; redirect to Login.jsp if not authenticated
        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // Retrieve the "action" parameter to determine the requested operation
        String action = request.getParameter("action");
        
        // Create an instance of BookingDAO to interact with the database
        BookingDAO bookingDAO = new BookingDAO();

        // Handle the "cancel" action to cancel a booking
        if ("cancel".equals(action)) {
            // Retrieve the booking ID from the request parameters
            String bookingIdParam = request.getParameter("bookingId");
            System.out.println("Received Booking ID: " + bookingIdParam); // Log for debugging

            // Validate that the booking ID is provided and is a valid integer
            if (bookingIdParam == null || !bookingIdParam.matches("\\d+")) {
                request.setAttribute("error", "Invalid booking ID.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Convert the booking ID to an integer
            int bookingId = Integer.parseInt(bookingIdParam);
            
            // Attempt to cancel the booking in the database
            boolean isCanceled = bookingDAO.cancelBooking(bookingId);

            if (isCanceled) {
                // If cancellation is successful, redirect to a success page
                response.sendRedirect("cancelSusscess.jsp"); // Note: Typo "cancelSusscess" should be "cancelSuccess"
            } else {
                // If cancellation fails (e.g., already canceled), set an error message and forward to error page
                request.setAttribute("error", "Failed to cancel the tour or it was already canceled.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else {
            // Handle invalid actions by setting an error message and forwarding to error page
            request.setAttribute("error", "Invalid action.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}