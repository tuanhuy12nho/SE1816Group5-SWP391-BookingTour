/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.ReviewDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Review;
import model.User;

/**
 * Servlet for handling review-related operations such as listing, adding,
 * updating, and deleting reviews.
 *
 * @author Admin
 */
@WebServlet(name = "ReviewServlet", urlPatterns = {"/ReviewServlet"}) // Maps the servlet to the "/ReviewServlet" URL
public class ReviewServlet extends HttpServlet {

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
        // Retrieve the "action" parameter to determine the requested operation
        String action = request.getParameter("action");

        // Create an instance of ReviewDAO to interact with the database
        ReviewDAO reviewDAO = new ReviewDAO();

        // Handle the "list" action to display all reviews for a specific tour
        if ("list".equals(action)) {
            // Get the tour ID from the request parameters and convert it to an integer
            int tourId = Integer.parseInt(request.getParameter("tourId"));

            // Fetch the list of reviews for the specified tour from the database
            List<Review> reviews = reviewDAO.getReviewsByTourId(tourId);

            // Set the reviews list as a request attribute to pass to the JSP
            request.setAttribute("reviews", reviews);

            // Set the tour ID as a request attribute to maintain context in the JSP
            request.setAttribute("tourId", tourId);

            // Forward the request to tourDetail.jsp to display the reviews
            request.getRequestDispatcher("tourDetail.jsp").forward(request, response);
        } // Handle the "delete" action to remove a specific review
        else if ("delete".equals(action)) {
            // Retrieve the current session to check if the user is logged in
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            // If the user is not logged in, redirect to the login page
            if (user == null) {
                response.sendRedirect("Login.jsp");
                return;
            }

            // Get the review ID from the request parameters and convert it to an integer
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));

            // Delete the specified review from the database
            reviewDAO.deleteReview(reviewId);

            // Redirect back to the review list for the same tour
            response.sendRedirect("ReviewServlet?action=list&tourId=" + request.getParameter("tourId"));
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
        // Retrieve the "action" parameter to determine the requested operation
        String action = request.getParameter("action");

        // Create an instance of ReviewDAO to interact with the database
        ReviewDAO reviewDAO = new ReviewDAO();

        // Retrieve the current session to check if the user is logged in
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // If the user is not logged in, redirect to the login page
        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // Get the user ID from the logged-in user object
        int userId = user.getId();

        // Retrieve review details from the request parameters
        int tourId = Integer.parseInt(request.getParameter("tourId"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");
        // Set the review date to the current date
        String reviewDate = new java.sql.Date(System.currentTimeMillis()).toString();

        // Create a new Review object with the provided details and the user's username
        Review review = new Review(userId, tourId, rating, comment, reviewDate, user.getUsername());

        // Handle the "add" action to insert a new review
        if ("add".equals(action)) {
            // Add the new review to the database
            reviewDAO.addReview(review);
        } // Handle the "update" action to modify an existing review
        else if ("update".equals(action)) {
            // Get the review ID from the request parameters and set it in the review object
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            review.setId(reviewId);

            // Update the review in the database
            reviewDAO.updateReview(review);
        }

        // Redirect back to the review list for the same tour after adding or updating
        response.sendRedirect("ReviewServlet?action=list&tourId=" + tourId);
    }
}
