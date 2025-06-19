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
import java.util.Vector;
import model.Review;

/**
 *
 * @author admin
 */
@WebServlet(name = "ManageReview", urlPatterns = {"/manageReview"})
public class ManageReview extends HttpServlet {

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

        ReviewDAO reviewDao = new ReviewDAO();
        if(request.getParameter("tourId") == null) {
            response.sendRedirect("home");
            return;
        }
         int tourId = Integer.parseInt(request.getParameter("tourId"));
        String commentKeyword = request.getParameter("commentKeyword") == null ? "" : request.getParameter("commentKeyword");
        String userKeyword = request.getParameter("userKeyword") == null ? "" : request.getParameter("userKeyword");
        String sortBy = request.getParameter("sortBy") == null ? "date_desc" : request.getParameter("sortBy");

        int pageIndex = 1;
        int pageSize = 5;

        try {
            pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        } catch (Exception e) {
            // giữ nguyên pageIndex = 1
        }

        Vector<Review> list = reviewDao.getReviewsByTour(tourId, commentKeyword, userKeyword, sortBy, pageIndex, pageSize);
        int total = reviewDao.countReviewsByTour(tourId, commentKeyword, userKeyword);
        int totalPage = (int) Math.ceil((double) total / pageSize);

        request.setAttribute("list", list);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("pageIndex", pageIndex);

        request.getRequestDispatcher("Admin/reviewManage.jsp").forward(request, response);
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reviewIdStr = request.getParameter("reviewId");
        String status = request.getParameter("status");
        if (reviewIdStr != null) {
            int reviewId = Integer.parseInt(reviewIdStr);

            ReviewDAO reviewDAO = new ReviewDAO(); // hoặc inject bean tùy cách bạn dùng
            reviewDAO.hideReview(reviewId, status);

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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

    
    public static void main(String[] args) {
        ReviewDAO reviewDao = new ReviewDAO();
        int total = reviewDao.countReviewsByTour(1, "", "");
        System.out.println(total);
//        System.out.println(reviewDao.getReviewsByTour(1, "", "", "date_desc", 1, 5));
    }

}