package controller;

import DAO.TourDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Tour;

/**
 * Servlet HomeServlet for handling tour information.
 */
@WebServlet(name = "HomeServlet", urlPatterns = { "/home" })
public class HomeServlet extends HttpServlet {

    private TourDAO tourDAO = new TourDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String location = request.getParameter("location");
        String startDateStr = request.getParameter("start_date");
        String endDateStr = request.getParameter("end_date");
        java.sql.Date startDate = null, endDate = null;
        if (startDateStr != null && !startDateStr.isEmpty()) {
            startDate = java.sql.Date.valueOf(startDateStr);
        }
        if (endDateStr != null && !endDateStr.isEmpty()) {
            endDate = java.sql.Date.valueOf(endDateStr);
        }
        if (location != null && !location.isEmpty()) {
            List<Tour> tours = tourDAO.searchTours(location, startDate, endDate);
            request.setAttribute("tours", tours);
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
            request.getRequestDispatcher("/List.jsp").forward(request, response);
            return;
        }
        List<Tour> allTours = tourDAO.getAllTours();

        List<Tour> domesticToursNorth = filterToursByLocation(allTours,
                "Hạ Long|Sapa|Hà Nội|Ninh Bình|Hải Phòng|Thanh Hóa");
        List<Tour> domesticToursCentral = filterToursByLocation(allTours,
                "Đà Nẵng|Hội An|Huế|Quảng Bình|Nha Trang|Đà Lạt");
        List<Tour> domesticToursSouth = filterToursByLocation(allTours, "Phú Quốc|Châu Đốc|Côn Đảo");

        List<Tour> internationalToursAsia = filterToursByLocation(allTours, "Bangkok|Singapore|Tokyo|Seoul|Dubai");
        List<Tour> internationalToursEurope = filterToursByLocation(allTours, "Paris|London|Rome");
        List<Tour> internationalToursAmerica = filterToursByLocation(allTours, "New York");
        List<Tour> internationalToursAustralia = filterToursByLocation(allTours, "Sydney");

        List<Tour> tourByPlane = filterToursByTransport(allTours, "Máy Bay");
        List<Tour> tourByBus = filterToursByTransport(allTours, "Xe Khách|Xe Du Lịch");

        List<Tour> featuredTours = getFeaturedTours(allTours, 5);

        request.setAttribute("domesticToursNorth", domesticToursNorth);
        request.setAttribute("domesticToursCentral", domesticToursCentral);
        request.setAttribute("domesticToursSouth", domesticToursSouth);
        request.setAttribute("internationalToursAsia", internationalToursAsia);
        request.setAttribute("internationalToursEurope", internationalToursEurope);
        request.setAttribute("internationalToursAmerica", internationalToursAmerica);
        request.setAttribute("internationalToursAustralia", internationalToursAustralia);
        request.setAttribute("featuredTours", featuredTours);
        request.setAttribute("tourByPlane", tourByPlane);
        request.setAttribute("tourByBus", tourByBus);

        request.getRequestDispatcher("/List.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String location = request.getParameter("location");
        String startDateStr = request.getParameter("start_date");
        String endDateStr = request.getParameter("end_date");
        java.sql.Date startDate = null, endDate = null;
        if (startDateStr != null && !startDateStr.isEmpty()) {
            startDate = java.sql.Date.valueOf(startDateStr);
        }
        if (endDateStr != null && !endDateStr.isEmpty()) {
            endDate = java.sql.Date.valueOf(endDateStr);
        }
        if (location != null && !location.isEmpty()) {
            List<Tour> tours = tourDAO.searchTours(location, startDate, endDate);
            request.setAttribute("tours", tours);
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
            request.getRequestDispatcher("/List.jsp").forward(request, response);
            return;
        }
        String searchQuery = request.getParameter("search");

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            List<Tour> searchResults = new ArrayList<>();
            for (Tour t : tourDAO.getAllTours()) {
                if (t.getName().toLowerCase().contains(searchQuery.toLowerCase())
                        || t.getLocation().toLowerCase().contains(searchQuery.toLowerCase())) {
                    searchResults.add(t);
                }
            }
            request.setAttribute("searchResults", searchResults);
        }

        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "HomeServlet handles tour data retrieval and filtering.";
    }

    /**
     * Filters tours by matching location using a regex pattern.
     */
    private List<Tour> filterToursByLocation(List<Tour> tours, String regex) {
        List<Tour> result = new ArrayList<>();
        for (Tour t : tours) {
            if (t.getLocation().matches("(?i)" + regex)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Filters tours by transport type.
     */
    private List<Tour> filterToursByTransport(List<Tour> tours, String regex) {
        List<Tour> result = new ArrayList<>();
        for (Tour t : tours) {
            if (t.getTransport().matches("(?i)" + regex)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Retrieves the first N featured tours.
     */
    private List<Tour> getFeaturedTours(List<Tour> tours, int limit) {
        List<Tour> featured = new ArrayList<>();
        for (int i = 0; i < Math.min(tours.size(), limit); i++) {
            featured.add(tours.get(i));
        }
        return featured;
    }
}
