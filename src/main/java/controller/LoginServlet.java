/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.SessionLogin;

/**
 *
 * @author hungn
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

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
        // Forward the request to the Login.jsp page for the user to log in
        request.getRequestDispatcher("Login.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy tham số "action" để xác định đăng nhập hay đăng xuất
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            // Xử lý đăng xuất
            // Hủy session nếu tồn tại
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // Tìm và cập nhật cookie "username" để tồn tại 1 phút
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("username".equals(cookie.getName())) {
                        cookie.setMaxAge(60); // Cookie tồn tại 1 phút sau khi đăng xuất
                        cookie.setHttpOnly(true);
                        cookie.setSecure(true);
                        response.addCookie(cookie);
                        break;
                    }
                }
            }

            // Xóa cookie "password"
            Cookie passwordCookie = new Cookie("password", "");
            passwordCookie.setMaxAge(0); // Xóa cookie ngay lập tức
            passwordCookie.setHttpOnly(true); // Giữ bảo mật
            passwordCookie.setSecure(true); // Giữ bảo mật
            response.addCookie(passwordCookie);

            // Chuyển hướng về trang đăng nhập sau khi đăng xuất
            response.sendRedirect(request.getServletContext().getContextPath() + "/Login.jsp");
        } else {
            // Xử lý đăng nhập (giữ nguyên code gốc)
            String u = request.getParameter("_username");
            String p = request.getParameter("_password");
            String remember = request.getParameter("remember");

            UserDAO d = new UserDAO();
            String hashedPassword = d.hashMd5(p);
            User a = d.loginUser(u, hashedPassword);

            if (a == null) {
                request.setAttribute("error", "Invalid username or password !!!");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("user", a);
                session.setAttribute("userId", a.getId());

                if ("on".equals(remember)) {
                    Cookie usernameCookie = new Cookie("username", u);
                    usernameCookie.setMaxAge(30 * 24 * 60 * 60); // 30 ngày
                    usernameCookie.setHttpOnly(true);
                    usernameCookie.setSecure(true);
                    response.addCookie(usernameCookie);
                }

                response.sendRedirect(request.getServletContext().getContextPath() + "/SortTour");
            }
        }
    }
}
