/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.DiscountSaveDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import model.Discount;
import model.User;

/**
 *
 * @author criss
 */
@WebServlet(name = "DiscountSaveController", urlPatterns = {"/discountSaveController"})
public class DiscountSaveController extends HttpServlet {

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
            out.println("<title>Servlet DiscountSaveController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DiscountSaveController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    DiscountSaveDAO dao = new DiscountSaveDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int pageSize = 5;
        // Lấy user từ session
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("home");
            return;
        }
        Integer userId = (Integer) session.getAttribute("userId");
        // Đọc các tham số lọc
        String keyword = request.getParameter("keyword") == null ? "" : request.getParameter("keyword");
        String startStr = request.getParameter("startDate");
        String endStr = request.getParameter("endDate");
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        Date startDate = null;
        Date endDate = null;
        try {
            if (startStr != null && !startStr.isEmpty()) {
                startDate = Date.valueOf(startStr);
            }
            if (endStr != null && !endStr.isEmpty()) {
                endDate = Date.valueOf(endStr);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        int total = dao.countSavedVouchers(userId, keyword, startDate, endDate);
        int offset = (page - 1) * pageSize;

        List<Discount> list = dao.getSavedVouchersByUser(userId, keyword, startDate, endDate, offset, pageSize);
        System.out.println("list" + list);
        request.setAttribute("savedVouchers", list);
        request.setAttribute("totalPages", (int) Math.ceil((double) total / pageSize));
        request.setAttribute("currentPage", page);
        request.setAttribute("keyword", keyword);
        request.setAttribute("start", startStr);
        request.setAttribute("end", endStr);

        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);

        request.getRequestDispatcher("myDiscount.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        int discountId = Integer.parseInt(request.getParameter("discountId"));
        String action = request.getParameter("action");
        if (action.equals("delete")) {
            boolean deleted = dao.deleteSavedVoucher(userId, discountId);
            if (deleted) {
                response.setStatus(HttpServletResponse.SC_OK); // 200
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // hoặc 500
            }
        }
        if (action.equals("save")) {
            boolean alreadySaved = dao.checkVoucherAlreadySaved(userId, discountId);
            if (alreadySaved) {
                response.setStatus(HttpServletResponse.SC_CONFLICT); // HTTP 409
                response.getWriter().write("Voucher đã được lưu trước đó.");
                return;
            }

            boolean success = dao.saveVoucher(userId, discountId);
            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Lưu voucher thành công.");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Lưu voucher thất bại.");
            }
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
