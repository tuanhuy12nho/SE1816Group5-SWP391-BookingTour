package controller;

import DAO.ReviewDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.Review;
import model.User;

@WebServlet(name = "AddReviewServlet", urlPatterns = {"/add-review"})
public class AddReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get the user from session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (user == null) {
            // Redirect to login if not logged in
            session.setAttribute("errorMessage", "Bạn cần đăng nhập để đánh giá tour");
            response.sendRedirect("Login.jsp");
            return;
        }
        
        try {
            // Get parameters from the form
            int tourId = Integer.parseInt(request.getParameter("tourId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");
            
            // Validate input
            if (rating < 1 || rating > 5) {
                request.setAttribute("reviewError", "Điểm đánh giá phải từ 1 đến 5");
                request.getRequestDispatcher("booking?action=viewTour&id=" + tourId).forward(request, response);
                return;
            }
            
            if (comment == null || comment.trim().isEmpty()) {
                request.setAttribute("reviewError", "Vui lòng nhập nội dung đánh giá");
                request.getRequestDispatcher("booking?action=viewTour&id=" + tourId).forward(request, response);
                return;
            }
            
            // Create review object
            Review review = new Review();
            review.setUserId(user.getId());
            review.setTourId(tourId);
            review.setRating(rating);
            review.setComment(comment);
            
            // Set current date as review date
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String reviewDate = now.format(formatter);
            review.setReviewDate(reviewDate);
            
            // Add review to database
            ReviewDAO reviewDAO = new ReviewDAO();
            reviewDAO.addReview(review);
            
            // Set success message
            request.setAttribute("reviewSuccess", "Cảm ơn bạn đã đánh giá tour!");
            
            // Redirect back to tour detail page
            response.sendRedirect("booking?action=viewTour&id=" + tourId);
            
        } catch (NumberFormatException e) {
            // Handle parsing errors
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle other errors
            e.printStackTrace();
            request.setAttribute("error", "Đã có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
} 