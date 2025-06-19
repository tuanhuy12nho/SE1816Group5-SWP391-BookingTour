/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import DAO.PasswordTokenDAO;
import DAO.UserDAO;
import db.DBContext;
import model.PasswordToken;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Servlet to handle password reset functionality
 */
@WebServlet(name = "ResetPasswordServlet", urlPatterns = { "/reset-password" })
public class ResetPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final PasswordTokenDAO tokenDAO = new PasswordTokenDAO();

    /**
     * Handle GET request - Display password reset form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("error", "Token đặt lại mật khẩu không hợp lệ.");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }

        if (!tokenDAO.isTokenValid(token)) {
            request.setAttribute("error", "Token đặt lại mật khẩu đã hết hạn hoặc không hợp lệ.");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }

        request.setAttribute("token", token);
        request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
    }

    /**
     * Handle POST request - Process password reset
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("error", "Token đặt lại mật khẩu không hợp lệ.");
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
            return;
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu mới.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
            return;
        }

        if (confirmPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
            return;
        }

        if (newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
            return;
        }

        PasswordToken passwordToken = tokenDAO.findByToken(token);
        if (passwordToken == null || !passwordToken.isValid()) {
            request.setAttribute("error", "Token đặt lại mật khẩu đã hết hạn hoặc không hợp lệ.");
            request.getRequestDispatcher("/login").forward(request, response);
            return;
        }

        try {
            // Kiểm tra kết nối DB trước khi tiếp tục
            try (Connection testConn = new DBContext().getConnection()) {
                System.out.println("ResetPasswordServlet - DB Connection test successful");
            } catch (SQLException e) {
                System.out.println("ResetPasswordServlet - DB Connection test failed: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "Không thể kết nối với cơ sở dữ liệu. Vui lòng thử lại sau.");
                request.setAttribute("token", token);
                request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
                return;
            }

            User user = userDAO.getUserById(passwordToken.getUserId());
            if (user == null) {
                request.setAttribute("error", "Không tìm thấy người dùng.");
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }

            String hashedPassword = hashPassword(newPassword);
            boolean passwordUpdated = userDAO.updatePassword(user.getId(), hashedPassword);

            if (passwordUpdated) {
                tokenDAO.markTokenAsUsed(token);
                tokenDAO.deleteTokensByUserId(user.getId());

                // Đặt success message trong request để hiển thị thông báo
                request.setAttribute("success",
                        "Mật khẩu đã được cập nhật thành công. Bạn sẽ được chuyển hướng đến trang đăng nhập sau 3 giây.");
                request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
                return;
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật mật khẩu. Vui lòng thử lại.");
                request.setAttribute("token", token);
                request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra trong hệ thống. Vui lòng thử lại sau.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
        }
    }

    private String hashPassword(String password) {
        try {
            // Chuyển sang dùng MD5 để đồng nhất với UserDAO
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Thêm log để debug
            System.out.println("Password hashed using MD5: " + hexString.toString().length() + " chars");
            return hexString.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for handling password reset functionality";
    }
}
