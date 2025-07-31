/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.FavoriteDAO;
import DAO.TourDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.Favorite;
import model.Tour;
import model.User;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "TourFilter", urlPatterns = {"/tourFilter"})
public class TourFilter extends HttpServlet {

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
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String transport = request.getParameter("transport");
        String type = request.getParameter("type");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");
        String sort = request.getParameter("sort"); // sort=price_asc, price_desc, newest, oldest
        String pageStr = request.getParameter("page");
        
        
        request.setAttribute("startDate", startDateStr);
        request.setAttribute("endDate", endDateStr);
        request.setAttribute("transport", transport);
        request.setAttribute("type", type);
        request.setAttribute("minPrice", minPriceStr);
        request.setAttribute("maxPrice", maxPriceStr);
        request.setAttribute("sort", sort);
        request.setAttribute("page", pageStr);
        
        
        
        
        
        // Chuyển đổi dữ liệu
        java.sql.Date startDate = (startDateStr != null && !startDateStr.isEmpty()) ? java.sql.Date.valueOf(startDateStr) : null;
        java.sql.Date endDate = (endDateStr != null && !endDateStr.isEmpty()) ? java.sql.Date.valueOf(endDateStr) : null;
        BigDecimal minPrice = (minPriceStr != null && !minPriceStr.isEmpty()) ? new BigDecimal(minPriceStr) : null;
        BigDecimal maxPrice = (maxPriceStr != null && !maxPriceStr.isEmpty()) ? new BigDecimal(maxPriceStr) : null;
        int page = (pageStr != null) ? Integer.parseInt(pageStr) : 1;
        int pageSize = 10;

        // Gọi DAO
        TourDAO dao = new TourDAO();
        List<Tour> tours = dao.filterTours(startDate, endDate, transport, type, minPrice, maxPrice, sort, page, pageSize);
        int totalRecords = dao.countFilteredTours(startDate, endDate, transport, type, minPrice, maxPrice);

        request.setAttribute("totalRecord", totalRecords);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        System.out.println("tours: "+tours);
        
         HttpSession session = request.getSession();
         User user = (User) session.getAttribute("user");

        if (user != null) {
           FavoriteDAO favDao = new FavoriteDAO();

            List<Favorite> favorites = favDao.getAllMyFavorites(user.getId());
            Set<Integer> favoriteTourIds = new HashSet<>();
            for (Favorite f : favorites) {
                favoriteTourIds.add(f.getTour().getId());
            }
            request.setAttribute("favorites", favorites);
            request.setAttribute("favoriteTourIds", favoriteTourIds);
        }
        
        // Gửi sang JSP
        request.setAttribute("tours", tours);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("sort", sort);
        request.getRequestDispatcher("tourFilter.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
