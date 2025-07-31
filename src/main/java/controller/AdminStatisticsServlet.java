package controller;

import DAO.AdminStatisticsDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.AdminBooking;
import model.User;
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("message", "Bạn cần đăng nhập để sử dụng chức năng này.");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }
        String role = user.getUserRole();

// Nếu không phải Admin và cũng không phải Manager → chuyển về login
        if (!"Admin".equalsIgnoreCase(role) && !"Manager".equalsIgnoreCase(role)) {
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

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

        System.out.println("DEBUG: Selected tourType = " + tourType);

        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);

        // Calculate statistics for both tour types
        double domesticRevenue = statisticsDAO.getRevenueByMonth(month, year, "Domestic");
        double internationalRevenue = statisticsDAO.getRevenueByMonth(month, year, "International");
        int domesticCount = statisticsDAO.getBookingCountByMonth(month, year, "Domestic");
        int internationalCount = statisticsDAO.getBookingCountByMonth(month, year, "International");

        System.out.println("DEBUG: domesticCount = " + domesticCount + ", internationalCount = " + internationalCount);
        System.out.println("DEBUG: domesticRevenue = " + domesticRevenue + ", internationalRevenue = " + internationalRevenue);

        // Get revenue details for the selected tour type
        List<AdminBooking> revenueDetails = statisticsDAO.getRevenueDetailsByMonth(month, year, tourType);
        System.out.println("DEBUG: revenueDetails size = " + (revenueDetails == null ? "null" : revenueDetails.size()));
        double totalRevenue = statisticsDAO.getTotalRevenueByMonth(month, year);

        // Set attributes for the JSP
        request.setAttribute("revenueDetails", revenueDetails);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("month", month);
        request.setAttribute("year", year);
        request.setAttribute("tourType", tourType);
        request.setAttribute("domesticRevenue", domesticRevenue);
        request.setAttribute("internationalRevenue", internationalRevenue);
        request.setAttribute("domesticCount", domesticCount);
        request.setAttribute("internationalCount", internationalCount);

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
