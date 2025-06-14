/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import DAO.ItineraryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Itinerary;

/**
 *
 * @author USA
 */
@WebServlet(name="AdminItineraryServlet", urlPatterns={"/adminItinerary"})
public class AdminItineraryServlet extends HttpServlet {
     private ItineraryDAO itineraryDAO = new ItineraryDAO();
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminItineraryServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminItineraryServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
       String action = request.getParameter("action");
        String tourIdParam = request.getParameter("tourId");

        int tourId;
        try {
            tourId = Integer.parseInt(tourIdParam);
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() + "/adminTour");
            return;
        }

        if ("edit".equalsIgnoreCase(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                try {
                    int id = Integer.parseInt(idParam);
                    // Thay stream bằng gọi DAO trực tiếp
                    Itinerary toEdit = itineraryDAO.getItineraryById(id);
                    request.setAttribute("itineraryEdit", toEdit);
                } catch (NumberFormatException e) {
                    // Nếu id không hợp lệ thì bỏ qua edit
                }
            }
            request.setAttribute("tourId", tourId);
            List<Itinerary> itineraries = itineraryDAO.getItinerariesByTourId(tourId);
            request.setAttribute("itineraries", itineraries);
            request.getRequestDispatcher("/Admin/itinerary.jsp").forward(request, response);
        } else if ("delete".equalsIgnoreCase(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                try {
                    int id = Integer.parseInt(idParam);
                    itineraryDAO.deleteItinerary(id);
                } catch (NumberFormatException e) {
                    // Ignore lỗi xóa
                }
            }
            response.sendRedirect(request.getContextPath() + "/adminItinerary?tourId=" + tourId);
        } else {
            // Mặc định hiển thị danh sách hành trình
            List<Itinerary> itineraries = itineraryDAO.getItinerariesByTourId(tourId);
            request.setAttribute("tourId", tourId);
            request.setAttribute("itineraries", itineraries);
            request.getRequestDispatcher("/Admin/itinerary.jsp").forward(request, response);
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
     request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        String tourIdParam = request.getParameter("tourId");
        String dayNumberParam = request.getParameter("dayNumber");
        String description = request.getParameter("description");

        int tourId, dayNumber;

        try {
            tourId = Integer.parseInt(tourIdParam);
            dayNumber = Integer.parseInt(dayNumberParam);
        } catch (NumberFormatException | NullPointerException e) {
            // Nếu dữ liệu không hợp lệ, redirect về danh sách tour
            response.sendRedirect(request.getContextPath() + "/adminTour");
            return;
        }

        if (idParam == null || idParam.isEmpty()) {
            // Thêm mới
            Itinerary newItinerary = new Itinerary();
            newItinerary.setTourId(tourId);
            newItinerary.setDayNumber(dayNumber);
            newItinerary.setDescription(description);
            itineraryDAO.addItinerary(newItinerary);
        } else {
            try {
                int id = Integer.parseInt(idParam);
                Itinerary updateItinerary = new Itinerary();
                updateItinerary.setId(id);
                updateItinerary.setTourId(tourId);
                updateItinerary.setDayNumber(dayNumber);
                updateItinerary.setDescription(description);
                itineraryDAO.updateItinerary(updateItinerary);
            } catch (NumberFormatException e) {
                // Nếu id không hợp lệ, bỏ qua cập nhật
            }
        }
        response.sendRedirect(request.getContextPath() + "/adminItinerary?tourId=" + tourId);
    
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
