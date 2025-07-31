/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import DAO.PasswordTokenDAO;
import DAO.UserDAO;
import model.User;
import util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to handle forgot password functionality
 * 
 * @author ACER
 */
@WebServlet(name = "ForgotPasswordServlet", urlPatterns = { "/forgot-password" })
public class ForgotPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final PasswordTokenDAO tokenDAO = new PasswordTokenDAO();

    /**
     * Handle GET request - show forgot password form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/forgotPassword.jsp").forward(request, response);
    }

    /**
     * Handle POST request - process forgot password form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        // Validate email input
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập địa chỉ email");
            request.getRequestDispatcher("/forgotPassword.jsp").forward(request, response);
            return;
        }

        try {
            // Check if user exists with this email
            User user = userDAO.getUserByEmail(email);

            if (user == null) {
                // Don't reveal if email exists or not for security
                request.setAttribute("success",
                        "Nếu email tồn tại trong hệ thống, liên kết đặt lại mật khẩu đã được gửi.");
                request.getRequestDispatcher("/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Delete any existing tokens for this user
            tokenDAO.deleteTokensByUserId(user.getId());

            // Create new password reset token (valid for 30 minutes)
            String token = tokenDAO.createPasswordToken(user.getId(), 30);

            if (token != null) {
                // Send password reset email
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" +
                        request.getServerPort() + request.getContextPath();

                boolean emailSent = EmailUtil.sendPasswordResetEmail(email, token, baseUrl);

                if (emailSent) {
                    request.setAttribute("success",
                            "Liên kết đặt lại mật khẩu đã được gửi đến email của bạn. Vui lòng kiểm tra hộp thư.");
                } else {
                    request.setAttribute("error", "Có lỗi xảy ra khi gửi email. Vui lòng thử lại sau.");
                }
            } else {
                request.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại sau.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại sau.");
        }

        request.getRequestDispatcher("/forgotPassword.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Forgot Password Servlet";
    }
}
