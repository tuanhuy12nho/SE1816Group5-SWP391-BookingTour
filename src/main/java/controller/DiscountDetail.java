/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.DiscountDAO;
import DAO.TourDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.Discount;
import model.Tour;

/**
 *
 * @author criss
 */
@WebServlet(name = "DiscountDetail", urlPatterns = {"/discountDetail"})
public class DiscountDetail extends HttpServlet {

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
            out.println("<title>Servlet DiscountDetail</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DiscountDetail at " + request.getContextPath() + "</h1>");
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (request.getParameter("id") == null) {
                response.sendRedirect("home");
                return;
            }
            // Lấy discountId từ request
            int discountId = Integer.parseInt(request.getParameter("id"));
            int pageIndex = request.getParameter("pageIndex") == null ? 1 : Integer.parseInt(request.getParameter("pageIndex"));
            int pageSize = 8;

            TourDAO tourDao = new TourDAO();
            DiscountDAO discountDao = new DiscountDAO();

            // 1. Lấy toàn bộ tour (KHÔNG phân trang)
            List<Tour> allTours = tourDao.getAllToursOrderId();

            // 2. Lấy danh sách tourId có trong DiscountDetail theo discountId
            List<Integer> ids = discountDao.getAllTourIdHaveInDiscount(discountId);

            int totalTour = allTours.size();
            int totalPage = (int) Math.ceil((double) totalTour / pageSize);
            List<Tour> tours = tourDao.getAllToursPaging(pageIndex, pageSize);

            // 5. Lấy thông tin discount nếu cần hiển thị
            Discount discount = discountDao.getDiscountById(discountId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            request.setAttribute("startDateStr", discount.getStart_date().format(formatter));
            request.setAttribute("endDateStr", discount.getEnd_date().format(formatter));

            // 6. Đặt thuộc tính để hiển thị trên JSP
            request.setAttribute("tours", tours);
            request.setAttribute("ids", ids); // dùng để kiểm tra checked
            request.setAttribute("discount", discount);
            request.setAttribute("pageIndex", pageIndex);
            request.setAttribute("totalPage", totalPage);
            request.setAttribute("discountId", discountId);

            // 7. Forward tới JSP
            request.getRequestDispatcher("Admin/discountDetail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
        String action = request.getParameter("action");
        TourDAO tourDao = new TourDAO();
        DiscountDAO discountDao = new DiscountDAO();
        if (action.equals("update")) {
            int id = Integer.parseInt(request.getParameter("id"));

            String errorDescription = null;
            String errorDiscountPercent = null;
            String errorDate = null;
            String errorMax_usage = null;

            boolean hasError = false;
            String des_raw = request.getParameter("description");
            String dis_raw = request.getParameter("discount_percent");
            String max_usage_raw = request.getParameter("max_usage");

            LocalDateTime startDate = LocalDate.parse(request.getParameter("start_date")).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(request.getParameter("end_date")).atStartOfDay();
            if (des_raw == null || des_raw.trim().isEmpty()) {
                errorDescription = "Mô tả không được để trống";
                hasError = true;
            }
            if (dis_raw == null || dis_raw.trim().isEmpty()) {
                errorDescription = "discount percent không được để trống";
                hasError = true;
            } else if (Float.parseFloat(dis_raw) <= 0 || Float.parseFloat(dis_raw) >= 100) {
                errorDiscountPercent = "Phần trăm giảm giá phải lớn hơn 0 và nhỏ hơn 100";
                hasError = true;
            }
            if (max_usage_raw == null || max_usage_raw.trim().isEmpty()) {
                errorMax_usage = "số lượng giới hạn không được để trống";
                hasError = true;
            } else if (Integer.parseInt(max_usage_raw) <= 0) {
                errorMax_usage = "số lượng giới hạn phải lớn hơn 0";
                hasError = true;
            }

            if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
                errorDate = "Ngày kết thúc phải sau ngày bắt đầu";
                hasError = true;
            }
            Discount discount = discountDao.getDiscountById(id);
            if (hasError) {
                System.out.println("eee");
                System.out.println("sss:" + errorMax_usage);
                int pageIndex = 1, pageSize = 6;
                request.setAttribute("discount", discount);
                request.setAttribute("errorDescription", errorDescription);
                request.setAttribute("errorDiscountPercent", errorDiscountPercent);
                request.setAttribute("errorMax_usage", errorMax_usage);
                request.setAttribute("errorDate", errorDate);
                request.setAttribute("startDateStr", startDate.toLocalDate().toString());
                request.setAttribute("endDateStr", endDate.toLocalDate().toString());

                // 1. Lấy toàn bộ tour (KHÔNG phân trang)
                List<Tour> allTours = tourDao.getAllTours();

                // 2. Lấy danh sách tourId có trong DiscountDetail theo discountId
                List<Integer> ids = discountDao.getAllTourIdHaveInDiscount(1);

                int totalTour = allTours.size();
                int totalPage = (int) Math.ceil((double) totalTour / pageSize);
                List<Tour> tours = tourDao.getAllToursPaging(pageIndex, pageSize);

                // 5. Lấy thông tin discount nếu cần hiển thị
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                request.setAttribute("startDateStr", discount.getStart_date().format(formatter));
                request.setAttribute("endDateStr", discount.getEnd_date().format(formatter));

                // 6. Đặt thuộc tính để hiển thị trên JSP
                request.setAttribute("tours", tours);
                request.setAttribute("ids", ids); // dùng để kiểm tra checked
                request.setAttribute("discount", discount);
                request.setAttribute("pageIndex", pageIndex);
                request.setAttribute("totalPage", totalPage);
                request.setAttribute("discountId", 1);
                request.getRequestDispatcher("Admin/discountDetail.jsp").forward(request, response);
                return;
            }
            String description = request.getParameter("description");

            float discountPercent = Float.parseFloat(request.getParameter("discount_percent"));

            int maxUsage = Integer.parseInt(request.getParameter("max_usage"));

            Discount updated = new Discount(id, maxUsage, description, discountPercent, startDate, endDate);

            DiscountDAO dao = new DiscountDAO();
            dao.updateDiscount(updated);

            response.sendRedirect("discountDetail?id=" + id); // hoặc reload lại trang hiện tại
            return;

        } else if (action.equals("updateTour")) {
            String tourId_raw = request.getParameter("tourId");
            String discount_raw = request.getParameter("discountId");
            String status = request.getParameter("status");

            if (tourId_raw != null && status != null && discount_raw != null) {
                try {
                    int tourId = Integer.parseInt(tourId_raw);
                    int discountId = Integer.parseInt(discount_raw);
                    DiscountDAO dao = new DiscountDAO();

                    if (status.equals("add")) {
                        dao.addTourToDiscount(tourId, discountId);
                    } else if (status.equals("remove")) {
                        dao.removeTourFromDiscount(tourId, discountId);
                    }

                    response.setStatus(HttpServletResponse.SC_OK);
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
