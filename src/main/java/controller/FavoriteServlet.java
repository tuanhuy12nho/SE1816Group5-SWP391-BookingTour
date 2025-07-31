/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.FavoriteDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Favorite;
import model.User;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "FavoriteServlet", urlPatterns = {"/favorite"})
public class FavoriteServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FavoriteServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FavoriteServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    FavoriteDAO favDao = new FavoriteDAO();

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("message", "Bạn cần đăng nhập để sử dụng chức năng này.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }
//        String role = user.getUserRole();
//
//// Nếu không phải Admin và cũng không phải Manager → chuyển về login
//        if (!"Admin".equalsIgnoreCase(role)) {
//            request.getRequestDispatcher("Login.jsp").forward(request, response);
//            return;
//        }
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");

        String pageParam = request.getParameter("page");
        int page = 1;
        int limit = 5; // Số lượng tour mỗi trang
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        int offset = (page - 1) * limit;

        // Require user login; redirect to Login.jsp if not authenticated
        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "getAll";
        }
        int totalFavorites = favDao.getTotalFavoriteCount(user.getId());
        int totalPages = (int) Math.ceil((double) totalFavorites / limit);

        if (action.equals("delete")) {
            String favId = request.getParameter("favId");
            if (favId != null) {
                int favoriteId = Integer.parseInt(favId);
                boolean isDelete = favDao.removeFavoriteById(favoriteId);
                if (isDelete) {
                    request.setAttribute("messageSuss", "Xóa khỏi tour yêu thích thành công");
                } else {
                    request.setAttribute("messageErr", "Xóa khỏi tour yêu thích thất bại");
                }
            }

        }
        int userId = user.getId();
        if (action.equals("getAll") || action.equals("delete")) {
            List<Favorite> favorites = favDao.getFavoritesWithPagination(userId, offset, limit);
            request.setAttribute("favorites", favorites);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("favorite.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Thiết lập kiểu phản hồi là JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Nếu chưa đăng nhập
        if (user == null) {
            out.write("{\"status\": \"warning\", \"message\": \"Bạn cần đăng nhập để sử dụng chức năng này\"}");
            return;
        }

        String action = request.getParameter("action");
        String tourIdParam = request.getParameter("tourId");

        if (tourIdParam == null || tourIdParam.isEmpty()) {
            out.write("{\"status\": \"error\", \"message\": \"Thiếu tourId\"}");
            return;
        }

        try {
            int tourId = Integer.parseInt(tourIdParam);
            Favorite fav = new Favorite();
            fav.setUserId(user.getId());
            fav.setTourId(tourId);

            if ("add".equals(action)) {
                boolean added = favDao.addFavorite(fav); // boolean return
                if (added) {
                    out.write("{\"status\": \"success\", \"message\": \"Đã thêm vào mục yêu thích thành công\"}");
                } else {
                    out.write("{\"status\": \"warning\", \"message\": \"Tour đã tồn tại trong yêu thích\"}");
                }
            } else if ("remove".equals(action)) {
                boolean removed = favDao.removeFavorite(fav);
                if (removed) {
                    out.write("{\"status\": \"success\", \"message\": \"Đã hủy yêu thích tour thành công\"}");
                } else {
                    out.write("{\"status\": \"error\", \"message\": \"Không thể hủy yêu thích tour\"}");
                }
            } else {
                out.write("{\"status\": \"error\", \"message\": \"Hành động không hợp lệ\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"status\": \"error\", \"message\": \"Lỗi xử lý yêu cầu\"}");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
