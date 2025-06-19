/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.DiscountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Vector;
import model.Discount;

/**
 *
 * @author admin
 */
@WebServlet(name = "DiscountServlet", urlPatterns = {"/discountManage"})
public class DiscountServlet extends HttpServlet {

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
        DiscountDAO disDao = new DiscountDAO();

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

    int pageSize = 5; // số bản ghi trên 1 trang
    int pageIndex = 1;

    String pageParam = request.getParameter("pageIndex");
    if (pageParam != null && !pageParam.isEmpty()) {
        try {
            pageIndex = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            pageIndex = 1;
        }
    }

    DiscountDAO dao = new DiscountDAO();

    int totalRecords = dao.getTotalDiscountCount(); // Đếm tất cả bản ghi
    int totalPage = (int) Math.ceil((double) totalRecords / pageSize);

        Vector<Discount> discounts = dao.getAllDiscountWithPaging(pageIndex, pageSize);
                
        request.setAttribute("discounts", discounts);
        request.setAttribute("pageIndex", pageIndex);
        request.setAttribute("totalPage", totalPage);

    request.getRequestDispatcher("Admin/discountManage.jsp").forward(request, response);
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
        String disId_raw = request.getParameter("discountId");
        if (disId_raw != null) {
            int disId = Integer.parseInt(disId_raw);

            DiscountDAO disDao = new DiscountDAO();
            boolean isDelete = disDao.deleteDiscount(disId);
            if(isDelete) {
                 response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
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
        DiscountDAO disDao = new DiscountDAO();
        System.out.println(disDao.getAllDiscountWithPaging(1, 5));
    }
}