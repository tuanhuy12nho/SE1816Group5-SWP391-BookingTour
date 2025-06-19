/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.BookingDAO;
import DAO.ReviewDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import model.Booking;
import model.Review;
import model.Tour;
import model.User;

/**
 *
 * @author Admin
 */
@WebServlet(name = "BookingServlet", urlPatterns = {"/booking"})
public class BookingServlet extends HttpServlet {

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

        // Retrieve the current session to manage user data
        HttpSession session = request.getSession();

        // Get the user object from the session to check if the user is logged in
        User user = (User) session.getAttribute("user");

        // Set userId to the user's ID if logged in, otherwise default to -1
        int userId = (user != null) ? user.getId() : -1;

        // Retrieve the "action" parameter from the request to determine the operation
        String action = request.getParameter("action");

        // Handle the "viewTour" action to display tour details
        if ("viewTour".equals(action)) {
            // Get the tour ID from the request parameters
            String tourIdParam = request.getParameter("id");

            // Validate that the tour ID is provided and is a valid integer
            if (tourIdParam != null && tourIdParam.matches("\\d+")) {
                int tourId = Integer.parseInt(tourIdParam);

                // Create an instance of BookingDAO to interact with the database
                BookingDAO bookingDAO = new BookingDAO();

                // Fetch the tour details from the database using the tour ID
                Tour tour = bookingDAO.getTourById(tourId);

                // Create an instance of ReviewDAO to fetch reviews
                ReviewDAO reviewDAO = new ReviewDAO();

                // Fetch the list of reviews associated with the tour
                List<Review> reviews = reviewDAO.getReviewsByTourId(tourId);

                // Check if the tour exists; if not, set an error message
                if (tour == null) {
                    request.setAttribute("error", "Tour does not exist!");
                } else {
                    // Set the tour and reviews as request attributes to pass to the JSP
                    request.setAttribute("tour", tour);
                    request.setAttribute("reviews", reviews);
                }
            } else {
                // Set an error message if the tour ID is invalid or missing
                request.setAttribute("error", "Invalid ID!");
            }
            // Forward the request to tourDetail.jsp to display the results
            request.getRequestDispatcher("tourDetail.jsp").forward(request, response);
        } // Handle the "inputTour" action to prepare the booking form
        else if ("inputTour".equals(action)) {
            // Check if the user is logged in; redirect to login page if not
            if (user == null) {
                response.sendRedirect("Login.jsp");
                return;
            }

            // Get the tour ID from the request parameters
            String tourIdParam = request.getParameter("id");
            System.out.println("Received tour ID from form: " + tourIdParam);

            // Validate that the tour ID is provided and is a valid integer
            if (tourIdParam != null && tourIdParam.matches("\\d+")) {
                int tourId = Integer.parseInt(tourIdParam);

                // Create an instance of BookingDAO to interact with the database
                BookingDAO bookingDAO = new BookingDAO();

                // Fetch the tour details from the database using the tour ID
                Tour tour = bookingDAO.getTourById(tourId);

                // Check if the tour exists; if not, set an error message
                if (tour == null) {
                    request.setAttribute("error", "Tour does not exist!");
                } else {
                    // Set the tour as a request attribute to pass to the JSP
                    request.setAttribute("tour", tour);
                }
            } else {
                // Set an error message if the tour ID is invalid or missing
                request.setAttribute("error", "Invalid ID!");
            }
            // Forward the request to Booking.jsp to display the booking form
            request.getRequestDispatcher("Booking.jsp").forward(request, response);
        } // Handle the "viewInvoice" action to display a booking invoice
        else if ("viewInvoice".equals(action)) {
            // Check if the user is logged in; redirect to login page if not
            if (user == null) {
                response.sendRedirect("Login.jsp");
                return;
            }

            // Get the booking ID from the request parameters
            String bookingIdParam = request.getParameter("bookingId");

            // Validate that the booking ID is provided and is a valid integer
            if (bookingIdParam == null || !bookingIdParam.matches("\\d+")) {
                request.setAttribute("error", "Invalid booking ID.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            int bookingId = Integer.parseInt(bookingIdParam);
            Booking booking = null;

            try {
                // Create an instance of BookingDAO to interact with the database
                BookingDAO bookingDAO = new BookingDAO();

                // Fetch the booking details from the database using the booking ID
                booking = bookingDAO.getBookingById(bookingId);

                // Check if the booking exists; if not, set an error message
                if (booking == null) {
                    request.setAttribute("error", "No invoice found with ID: " + bookingId);
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                // Set the booking as a request attribute to pass to the JSP
                request.setAttribute("booking", booking);
                // Forward the request to invoice.jsp to display the invoice
                request.getRequestDispatcher("invoice.jsp").forward(request, response);

            } catch (SQLException e) {
                // Handle database errors by setting an error message
                request.setAttribute("error", "Data query error: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
            
        } else {
            // Redirect to the SortTour servlet if no valid action is specified
            response.sendRedirect("/SortTour");
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
        // Retrieve the "action" parameter from the request to determine the operation
        String action = request.getParameter("action");

        // Create instances of BookingDAO and ReviewDAO to interact with the database
        BookingDAO bookingDAO = new BookingDAO();
        ReviewDAO reviewDAO = new ReviewDAO();

        // Retrieve the current session to manage user data
        HttpSession session = request.getSession();

        // Get the user object from the session to check if the user is logged in
        User user = (User) session.getAttribute("user");

        // Check if the user is logged in; redirect to login page if not
        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // Get the user ID from the user object
        int userId = user.getId();

        // Handle different actions based on the "action" parameter
        switch (action) {
            case "save":
                // Handle the booking process; ensure the user is logged in
                if (user == null) {
                    request.setAttribute("error", "You need to log in to book a tour.");
                    request.getRequestDispatcher("Login.jsp").forward(request, response);
                    return;
                }

                // Get booking parameters from the request
                String tourIdParam = request.getParameter("tourId");
                String peoplecount = request.getParameter("peopleCount");
                String pay = request.getParameter("pay"); // Payment method

                // Validate that the number of people is provided and is a valid integer
                if (peoplecount == null || !peoplecount.matches("\\d+")) {
                    request.getRequestDispatcher("List.jsp").forward(request, response);
                    return;
                }

                // Validate that the tour ID is provided and is a valid integer
                if (tourIdParam == null || !tourIdParam.matches("\\d+")) {
                    request.setAttribute("error", "Invalid tour ID.");
                    request.getRequestDispatcher("List.jsp").forward(request, response);
                    return;
                }

                // Validate that the payment method is valid
                if (pay == null || !(pay.equals("Tiền mặt") || pay.equals("Thẻ ngân hàng") || pay.equals("Visa"))) {
                    request.setAttribute("error", "Invalid payment method.");
                    request.getRequestDispatcher("Info.jsp").forward(request, response);
                    return;
                }

                int tourId = Integer.parseInt(tourIdParam);
                int peopleCounts = Integer.parseInt(peoplecount);

                // Fetch the tour details from the database
                Tour tour = bookingDAO.getTourById(tourId);

                // Check if the tour exists; if not, set an error message
                if (tour == null) {
                    request.setAttribute("error", "Tour information not found.");
                    request.getRequestDispatcher("List.jsp").forward(request, response);
                    return;
                }

                try {
                    // Use the current date as the booking date
                    java.sql.Date bookingDATE = new java.sql.Date(System.currentTimeMillis());

                    // Create a new Booking object with the provided details
                    Booking booking = new Booking(userId, tourId, bookingDATE, "Pending", peopleCounts, pay);

                    // Save the booking to the database and get the generated booking ID
                    int bookingId = bookingDAO.bookTour(booking);

                    if (bookingId > 0) {
                        // If booking is successful, set the booking attribute and forward to Info.jsp
                        request.setAttribute("booking", booking);
                        request.getRequestDispatcher("Info.jsp").forward(request, response);
                    } else {
                        // Set an error message if booking fails
                        request.setAttribute("error", "Booking failed.");
                        request.getRequestDispatcher("Info.jsp").forward(request, response);
                    }
                } catch (Exception e) {
                    // Handle any exceptions by setting an error message
                    request.setAttribute("error", "An error occurred: " + e.getMessage());
                    request.getRequestDispatcher("Info.jsp").forward(request, response);
                }
                break;

            case "addReview":
                // Handle adding a new review for a tour
                int tourIdAdd = Integer.parseInt(request.getParameter("tourId"));
                int ratingAdd = Integer.parseInt(request.getParameter("rating"));
                String commentAdd = request.getParameter("comment");
                String reviewDateAdd = new java.sql.Date(System.currentTimeMillis()).toString();

                // Create a new Review object with the provided details
                Review reviewAdd = new Review(userId, tourIdAdd, ratingAdd, commentAdd, reviewDateAdd, user.getUsername());

                // Add the review to the database
                reviewDAO.addReview(reviewAdd);

                // Redirect back to the tour details page with the tour ID
                response.sendRedirect("booking?action=viewTour&id=" + tourIdAdd);
                break;

            case "updateReview":
                // Handle updating an existing review
                int reviewIdUpdate = Integer.parseInt(request.getParameter("reviewId"));
                int tourIdUpdate = Integer.parseInt(request.getParameter("tourId"));
                int ratingUpdate = Integer.parseInt(request.getParameter("rating"));
                String commentUpdate = request.getParameter("comment");
                String reviewDateUpdate = new java.sql.Date(System.currentTimeMillis()).toString();

                // Create a Review object with the updated details
                Review reviewUpdate = new Review(reviewIdUpdate, userId, tourIdUpdate, ratingUpdate, commentUpdate, reviewDateUpdate, user.getUsername());

                // Update the review in the database
                reviewDAO.updateReview(reviewUpdate);

                // Redirect back to the tour details page with the tour ID
                response.sendRedirect("booking?action=viewTour&id=" + tourIdUpdate);
                break;

            case "deleteReview":
                // Handle deleting a review
                String reviewIdParam = request.getParameter("reviewId");
                String tourIdDeleteParam = request.getParameter("tourId");

                // Validate that both review ID and tour ID are provided and are valid integers
                if (reviewIdParam != null && reviewIdParam.matches("\\d+") && tourIdDeleteParam != null && tourIdDeleteParam.matches("\\d+")) {
                    int reviewId = Integer.parseInt(reviewIdParam);
                    int tourIdDelete = Integer.parseInt(tourIdDeleteParam);

                    // Delete the review from the database
                    reviewDAO.deleteReview(reviewId);

                    // Redirect back to the tour details page with the tour ID
                    response.sendRedirect("booking?action=viewTour&id=" + tourIdDelete);
                } else {
                    // Set an error message if the IDs are invalid or missing
                    request.setAttribute("error", "Invalid ID!");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
                break;

            default:
                // Handle invalid actions by setting an error message
                request.setAttribute("error", "Invalid action.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // Helper method to handle errors by setting an error message and forwarding to a specified page
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage, String targetPage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher(targetPage).forward(request, response);
    }
}
