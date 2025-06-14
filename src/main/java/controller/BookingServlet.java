package controller;

import DAO.BookingDAO;
import DAO.ReviewDAO;
import DAO.ItineraryDAO;
import model.Booking;
import model.Review;
import model.Tour;
import model.User;
import model.Itinerary;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BookingServlet", urlPatterns = { "/booking" })
public class BookingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userId = (user != null) ? user.getId() : -1;

        String action = request.getParameter("action");

        if ("viewTour".equals(action)) {
            handleViewTour(request, response);
        } else if ("inputTour".equals(action)) {
            if (user == null) {
                response.sendRedirect("Login.jsp");
                return;
            }
            handleInputTour(request, response);
        } else if ("viewInvoice".equals(action)) {
            if (user == null) {
                response.sendRedirect("Login.jsp");
                return;
            }
            handleViewInvoice(request, response);
        } else {
            response.sendRedirect("/SortTour");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        String action = request.getParameter("action");
        BookingDAO bookingDAO = new BookingDAO();
        ReviewDAO reviewDAO = new ReviewDAO();

        switch (action) {
            case "save":
                handleBookingSave(request, response, user, bookingDAO);
                break;
            case "addReview":
                handleAddReview(request, response, user, reviewDAO);
                break;
            case "updateReview":
                handleUpdateReview(request, response, user, reviewDAO);
                break;
            case "deleteReview":
                handleDeleteReview(request, response, reviewDAO);
                break;
            default:
                request.setAttribute("error", "Invalid action.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleViewTour(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tourIdParam = request.getParameter("id");
        // Check for message param from redirect
        String message = request.getParameter("message");
        if (message != null) {
            request.setAttribute("message", message);
        }

        if (tourIdParam != null && tourIdParam.matches("\\d+")) {
            int tourId = Integer.parseInt(tourIdParam);
            BookingDAO bookingDAO = new BookingDAO();
            Tour tour = bookingDAO.getTourById(tourId);
            ReviewDAO reviewDAO = new ReviewDAO();
            List<Review> reviews = reviewDAO.getReviewsByTourId(tourId);
            List<Itinerary> itineraries = getTourItineraries(tourId);

            if (tour == null) {
                request.setAttribute("error", "Tour does not exist!");
            } else {
                request.setAttribute("tour", tour);
                request.setAttribute("reviews", reviews);
                request.setAttribute("itineraries", itineraries);
            }
        } else {
            request.setAttribute("error", "Invalid ID!");
        }
        request.getRequestDispatcher("tourDetail.jsp").forward(request, response);
    }

    private void handleInputTour(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tourIdParam = request.getParameter("id");
        if (tourIdParam != null && tourIdParam.matches("\\d+")) {
            int tourId = Integer.parseInt(tourIdParam);
            BookingDAO bookingDAO = new BookingDAO();
            Tour tour = bookingDAO.getTourById(tourId);

            if (tour == null) {
                request.setAttribute("error", "Tour does not exist!");
            } else {
                request.setAttribute("tour", tour);
            }
        } else {
            request.setAttribute("error", "Invalid ID!");
        }
        request.getRequestDispatcher("Booking.jsp").forward(request, response);
    }

    private void handleViewInvoice(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookingIdParam = request.getParameter("bookingId");
        if (bookingIdParam == null || !bookingIdParam.matches("\\d+")) {
            request.setAttribute("error", "Invalid booking ID.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        int bookingId = Integer.parseInt(bookingIdParam);
        BookingDAO bookingDAO = new BookingDAO();
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                request.setAttribute("error", "No invoice found with ID: " + bookingId);
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } else {
                request.setAttribute("booking", booking);
                request.getRequestDispatcher("invoice.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Data query error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleBookingSave(HttpServletRequest request, HttpServletResponse response, User user,
            BookingDAO bookingDAO) throws ServletException, IOException {
        String tourIdParam = request.getParameter("tourId");
        String peoplecount = request.getParameter("peopleCount");
        String pay = request.getParameter("pay");

        if (tourIdParam == null || !tourIdParam.matches("\\d+") ||
                peoplecount == null || !peoplecount.matches("\\d+") ||
                pay == null || !(pay.equals("Tiền mặt") || pay.equals("Thẻ ngân hàng") || pay.equals("Visa"))) {
            request.setAttribute("error", "Invalid input.");
            request.getRequestDispatcher("List.jsp").forward(request, response);
            return;
        }

        int tourId = Integer.parseInt(tourIdParam);
        int peopleCounts = Integer.parseInt(peoplecount);
        Tour tour = bookingDAO.getTourById(tourId);

        if (tour == null) {
            request.setAttribute("error", "Tour not found.");
            request.getRequestDispatcher("List.jsp").forward(request, response);
            return;
        }

        try {
            Booking booking = new Booking(user.getId(), tourId, new java.sql.Date(System.currentTimeMillis()),
                    "Pending", peopleCounts, pay);
            int bookingId = bookingDAO.bookTour(booking);
            if (bookingId > 0) {
                request.setAttribute("booking", booking);
                request.getRequestDispatcher("Info.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Booking failed.");
                request.getRequestDispatcher("Info.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("Info.jsp").forward(request, response);
        }
    }

    private void handleAddReview(HttpServletRequest request, HttpServletResponse response, User user,
            ReviewDAO reviewDAO) throws IOException {
        int tourId = Integer.parseInt(request.getParameter("tourId"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");
        String date = new java.sql.Date(System.currentTimeMillis()).toString();
        Review review = new Review(user.getId(), tourId, rating, comment, date, user.getUsername());
        reviewDAO.addReview(review);
        response.sendRedirect("booking?action=viewTour&id=" + tourId);
    }

    private void handleUpdateReview(HttpServletRequest request, HttpServletResponse response, User user,
            ReviewDAO reviewDAO) throws IOException {
        int reviewId = Integer.parseInt(request.getParameter("reviewId"));
        int tourId = Integer.parseInt(request.getParameter("tourId"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");
        String date = new java.sql.Date(System.currentTimeMillis()).toString();
        Review review = new Review(reviewId, user.getId(), tourId, rating, comment, date, user.getUsername());
        reviewDAO.updateReview(review);
        response.sendRedirect("booking?action=viewTour&id=" + tourId);
    }

    private void handleDeleteReview(HttpServletRequest request, HttpServletResponse response, ReviewDAO reviewDAO)
            throws ServletException, IOException {
        String reviewIdParam = request.getParameter("reviewId");
        String tourIdParam = request.getParameter("tourId");
        if (reviewIdParam != null && reviewIdParam.matches("\\d+") &&
                tourIdParam != null && tourIdParam.matches("\\d+")) {
            int reviewId = Integer.parseInt(reviewIdParam);
            int tourId = Integer.parseInt(tourIdParam);
            reviewDAO.deleteReview(reviewId);
            response.sendRedirect("booking?action=viewTour&id=" + tourId);
        } else {
            request.setAttribute("error", "Invalid ID!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private List<Itinerary> getTourItineraries(int tourId) {
        try {
            ItineraryDAO dao = new ItineraryDAO();
            return dao.getItinerariesByTourId(tourId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
