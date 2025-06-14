package controller;

import DAO.AdminStatisticsDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.AdminBooking;
import util.SessionLogin;

@WebServlet(name = "AdminStatisticsServlet", urlPatterns = { "/AdminStatisticsServlet" })
public class AdminStatisticsServlet extends HttpServlet {

    private AdminStatisticsDAO statisticsDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        statisticsDAO = new AdminStatisticsDAO();
    }

    // Validate tháng năm hợp lệ
    private boolean validateDateParams(String monthStr, String yearStr) {
        if (monthStr == null || yearStr == null || monthStr.isEmpty() || yearStr.isEmpty()) {
            return false;
        }
        try {
            int month = Integer.parseInt(monthStr);
            int year = Integer.parseInt(yearStr);
            return month >= 1 && month <= 12 && year >= 2000 && year <= 2100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra admin
        if (!SessionLogin.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin.jsp");
            return;
        }

        String type = request.getParameter("type");

        try {
            if (type == null || type.isEmpty()) {
                // Mặc định hiển thị toàn bộ dữ liệu
                showAllRevenueDetails(request, response);
                return;
            }

            switch (type) {
                case "revenueAll":
                    showAllRevenueDetails(request, response);
                    break;
                case "revenueDetails":
                    showRevenueDetailsByFilter(request, response);
                    break;
                case "statistics":
                    showBookingAndRevenueStatistics(request, response);
                    break;
                default:
                    showAllRevenueDetails(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi xử lý yêu cầu. Vui lòng thử lại.");
            request.getRequestDispatcher("Admin/statistics.jsp").forward(request, response);
        }
    }

    private void showAllRevenueDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<AdminBooking> revenueDetails = statisticsDAO.getAllRevenueDetails();

        if (revenueDetails != null && !revenueDetails.isEmpty()) {
            request.setAttribute("revenueDetails", revenueDetails);
        } else {
            request.setAttribute("message", "Không có dữ liệu doanh thu.");
        }

        request.getRequestDispatcher("Admin/statistics.jsp").forward(request, response);
    }

    private void showRevenueDetailsByFilter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String monthStr = request.getParameter("month");
        String yearStr = request.getParameter("year");
        String tourType = request.getParameter("tourType");

        if (!validateDateParams(monthStr, yearStr)) {
            request.setAttribute("error", "Vui lòng chọn tháng và năm hợp lệ.");
            request.getRequestDispatcher("Admin/statistics.jsp").forward(request, response);
            return;
        }

        if (tourType == null || !(tourType.equals("Domestic") || tourType.equals("International"))) {
            tourType = "Domestic";
        }

        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);

        List<AdminBooking> revenueDetails = statisticsDAO.getRevenueDetailsByMonth(month, year, tourType);
        System.out.println("DEBUG: revenueDetails size = " + (revenueDetails == null ? "null" : revenueDetails.size()));
        double totalRevenue = statisticsDAO.getTotalRevenueByMonth(month, year);

        request.setAttribute("revenueDetails", revenueDetails);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("month", month);
        request.setAttribute("year", year);
        request.setAttribute("tourType", tourType);

        request.getRequestDispatcher("Admin/statistics.jsp").forward(request, response);
    }

    private void showBookingAndRevenueStatistics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String monthStr = request.getParameter("month");
        String yearStr = request.getParameter("year");

        if (!validateDateParams(monthStr, yearStr)) {
            request.setAttribute("error", "Vui lòng chọn tháng và năm hợp lệ.");
            request.getRequestDispatcher("Admin/statistics.jsp").forward(request, response);
            return;
        }

        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);

        double revenueDomestic = statisticsDAO.getRevenueByMonth(month, year, "Domestic");
        double revenueInternational = statisticsDAO.getRevenueByMonth(month, year, "International");
        int bookingsDomestic = statisticsDAO.getBookingCountByMonth(month, year, "Domestic");
        int bookingsInternational = statisticsDAO.getBookingCountByMonth(month, year, "International");

        request.setAttribute("revenueDomestic", revenueDomestic);
        request.setAttribute("revenueInternational", revenueInternational);
        request.setAttribute("bookingsDomestic", bookingsDomestic);
        request.setAttribute("bookingsInternational", bookingsInternational);
        request.setAttribute("month", month);
        request.setAttribute("year", year);

        request.getRequestDispatcher("Admin/statistics.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ở đây bạn có thể tùy chỉnh nếu cần xử lý POST, hoặc gọi doGet luôn
        doGet(request, response);
    }
}
