package controller;

import DAO.AdminBookingDAO;
import DAO.AdminStatisticsDAO;
import DAO.AdminTourDAO;
import DAO.AdminUserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.AdminBooking;
import model.User;
import util.SessionLogin;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin-dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private AdminStatisticsDAO statisticsDAO;
    private AdminBookingDAO bookingDAO;
    private AdminUserDAO userDAO;
    private AdminTourDAO tourDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        statisticsDAO = new AdminStatisticsDAO();
        bookingDAO = new AdminBookingDAO();
        userDAO = new AdminUserDAO();
        tourDAO = new AdminTourDAO();
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

        // Check if user is admin
//        if (!SessionLogin.isAdmin(request)) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }

        try {
            // Get total bookings
            List<AdminBooking> bookings = bookingDAO.getAllBookings();
            int totalBookings = bookings.size();

            // Calculate total revenue from all bookings regardless of status
            double totalRevenue = 0;
            for (AdminBooking booking : bookings) {
                totalRevenue += booking.getTotalAmount();
            }

            // Get total users
            int totalUsers = userDAO.getAllUsers().size();

            // Get total tours
            int totalTours = tourDAO.getTotalTours(null);

            // Get booking stats by status for chart
            Map<String, Integer> bookingStatusCounts = new HashMap<>();
            bookingStatusCounts.put("Confirmed", 0);
            bookingStatusCounts.put("Pending", 0);
            bookingStatusCounts.put("Cancelled", 0);
            bookingStatusCounts.put("Processing", 0);

            for (AdminBooking booking : bookings) {
                String status = booking.getStatus();
                if (status != null) {
                    bookingStatusCounts.put(status, bookingStatusCounts.getOrDefault(status, 0) + 1);
                }
            }

            // Get the last 7 days (including today)
            List<Date> last7Days = new ArrayList<>();
            List<String> last7DaysLabels = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

            Calendar cal = Calendar.getInstance();
            for (int i = 6; i >= 0; i--) {
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_YEAR, -i);
                Date date = cal.getTime();
                last7Days.add(date);
                last7DaysLabels.add(sdf.format(date));
            }

            // Get revenue data by day for chart (last 7 days)
            Map<String, Double> revenueByDay = new LinkedHashMap<>();
            Map<String, Map<String, Integer>> statusByDay = new LinkedHashMap<>();

            // Initialize maps for each day
            for (String dayLabel : last7DaysLabels) {
                revenueByDay.put(dayLabel, 0.0);

                Map<String, Integer> statusCounts = new HashMap<>();
                statusCounts.put("Confirmed", 0);
                statusCounts.put("Pending", 0);
                statusCounts.put("Cancelled", 0);
                statusCounts.put("Processing", 0);
                statusByDay.put(dayLabel, statusCounts);
            }

            // Calculate revenue and status counts for each day
            for (AdminBooking booking : bookings) {
                if (booking.getBookingDate() != null) {
                    // Check if the booking date is within the last 7 days
                    for (int i = 0; i < last7Days.size(); i++) {
                        Date day = last7Days.get(i);
                        String dayLabel = last7DaysLabels.get(i);

                        // Compare booking date with current day (ignoring time)
                        Calendar bookingCal = Calendar.getInstance();
                        bookingCal.setTime(booking.getBookingDate());
                        bookingCal.set(Calendar.HOUR_OF_DAY, 0);
                        bookingCal.set(Calendar.MINUTE, 0);
                        bookingCal.set(Calendar.SECOND, 0);
                        bookingCal.set(Calendar.MILLISECOND, 0);

                        Calendar dayCal = Calendar.getInstance();
                        dayCal.setTime(day);
                        dayCal.set(Calendar.HOUR_OF_DAY, 0);
                        dayCal.set(Calendar.MINUTE, 0);
                        dayCal.set(Calendar.SECOND, 0);
                        dayCal.set(Calendar.MILLISECOND, 0);

                        if (bookingCal.getTimeInMillis() == dayCal.getTimeInMillis()) {
                            // Add to revenue for all bookings regardless of status
                            double currentRevenue = revenueByDay.get(dayLabel);
                            revenueByDay.put(dayLabel, currentRevenue + booking.getTotalAmount());

                            // Count by status
                            String status = booking.getStatus();
                            if (status != null) {
                                Map<String, Integer> statusCounts = statusByDay.get(dayLabel);
                                statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
                            }

                            break; // Found the matching day, no need to check other days
                        }
                    }
                }
            }

            // Log detailed revenue data for the last 7 days
            System.out.println("=== REVENUE DATA FOR LAST 7 DAYS ===");
            double totalWeekRevenue = 0.0;
            for (String day : last7DaysLabels) {
                double dailyRevenue = revenueByDay.get(day);
                totalWeekRevenue += dailyRevenue;
                System.out.println("Date: " + day + " - Revenue: " + String.format("%,.0f", dailyRevenue) + " - Orders: [Confirmed: " + statusByDay.get(day).get("Confirmed")
                        + ", Pending: " + statusByDay.get(day).get("Pending")
                        + ", Cancelled: " + statusByDay.get(day).get("Cancelled")
                        + ", Processing: " + statusByDay.get(day).get("Processing") + "]");
            }
            System.out.println("TOTAL 7-DAY REVENUE: " + String.format("%,.0f", totalWeekRevenue));
            System.out.println("=====================================");

            // Set attributes for the JSP
            request.setAttribute("totalBookings", totalBookings);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalTours", totalTours);
            request.setAttribute("bookingStatusCounts", bookingStatusCounts);
            request.setAttribute("revenueByDay", revenueByDay);
            request.setAttribute("statusByDay", statusByDay);
            request.setAttribute("last7DaysLabels", last7DaysLabels);

            // Forward to admin dashboard
            request.getRequestDispatcher("Admin/admin.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
