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
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import model.Discount;
import model.Tour;

/**
 *
 * @author admin
 */
@WebServlet(name = "DiscountCreate", urlPatterns = {"/discountCreate"})
public class DiscountCreate extends HttpServlet {

    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*";
    private final int CODE_LENGTH = 5;
    private final Random random = new Random();

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DiscountCreate</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DiscountCreate at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    private DiscountDAO discountDAO = new DiscountDAO();
    private TourDAO tourDAO = new TourDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy tham số phân trang
        try {
            // Lấy discountId từ request
            int pageIndex = request.getParameter("pageIndex") == null ? 1 : Integer.parseInt(request.getParameter("pageIndex"));
            int pageSize = 6;

            TourDAO tourDao = new TourDAO();
            DiscountDAO discountDao = new DiscountDAO();

            // 1. Lấy toàn bộ tour (KHÔNG phân trang)
            List<Tour> allTours = tourDao.getAllTours();

            // 2. Lấy danh sách tourId có trong DiscountDetail theo discountId
            int totalTour = allTours.size();
            int totalPage = (int) Math.ceil((double) totalTour / pageSize);
            List<Tour> tours = tourDao.getAllToursPaging(pageIndex, pageSize);

            // 5. Lấy thông tin discount nếu cần hiển thị
            // 6. Đặt thuộc tính để hiển thị trên JSP
            request.setAttribute("tours", tours);
            request.setAttribute("pageIndex", pageIndex);
            request.setAttribute("totalPage", totalPage);
            // 7. Forward tới JSP
            request.getRequestDispatcher("Admin/createDiscount.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        TourDAO tourDao = new TourDAO();
        DiscountDAO discountDao = new DiscountDAO();
        if (action.equals("create")) {
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
            } else {
                 request.setAttribute("des", des_raw);
            }
            if (dis_raw == null || dis_raw.trim().isEmpty()) {
                errorDescription = "discount percent không được để trống";
                hasError = true;
            } else if (Float.parseFloat(dis_raw) <= 0 || Float.parseFloat(dis_raw) >= 100) {
                errorDiscountPercent = "Phần trăm giảm giá phải lớn hơn 0 và nhỏ hơn 100";
                hasError = true;
            } else {
                 request.setAttribute("dis", dis_raw);
            }
            if (max_usage_raw == null || max_usage_raw.trim().isEmpty()) {
                errorMax_usage = "số lượng giới hạn không được để trống";
                hasError = true;
            } else if (Integer.parseInt(max_usage_raw) <= 0) {
                errorMax_usage = "số lượng giới hạn phải lớn hơn 0";
                hasError = true;
            } else {
                 request.setAttribute("max_use", max_usage_raw);
            }

            if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
                errorDate = "Ngày kết thúc phải sau ngày bắt đầu";
                hasError = true;
            }
            if (hasError) {
                System.out.println("eee");
                System.out.println("sss:" + errorMax_usage);
                int pageIndex = 1, pageSize = 6;
                request.setAttribute("errorDescription", errorDescription);
                request.setAttribute("errorDiscountPercent", errorDiscountPercent);
                request.setAttribute("errorMax_usage", errorMax_usage);
                request.setAttribute("errorDate", errorDate);
                request.setAttribute("startDateStr", startDate.toLocalDate().toString());
                request.setAttribute("endDateStr", endDate.toLocalDate().toString());

                request.getRequestDispatcher("Admin/createDiscount.jsp").forward(request, response);
                return;
            } else {
                String code = generateUniqueCode(discountDao);
                Discount newDiscount = new Discount(code, Integer.parseInt(max_usage_raw), des_raw, Float.parseFloat(dis_raw), startDate, endDate);
                int disId = discountDao.createDiscount(newDiscount);
                if (disId > 0) {
                    response.sendRedirect("discountDetail?id=" + disId);
                    return;
                } else {
                    request.setAttribute("mess", "Xảy ra lỗi, vui lòng thử lại");
                    request.getRequestDispatcher("Admin/createDiscount.jsp").forward(request, response);
                    return;
                }
            }
        }

    }

    // Tạo code ngẫu nhiên
    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    // Hàm sinh code không trùng
    public String generateUniqueCode(DiscountDAO dao) {
        String code;
        do {
            code = generateRandomCode();
        } while (dao.checkCodeExists(code)); // Kiểm tra trùng
        return code;
    }

}