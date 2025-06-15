/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.TourDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.Tour;

/**
 * Servlet for sorting and filtering tours based on location, transport, and
 * other criteria.
 *
 * @author Admin
 */
@WebServlet(name = "SortTour", urlPatterns = {"/SortTour"}) // Maps the servlet to the "/SortTour" URL
public class SortTour extends HttpServlet {

    // Instance variable for TourDAO to interact with the database
    private TourDAO tourDAO = new TourDAO();

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

        // Retrieve all tours from the database
        List<Tour> allTours = tourDAO.getAllTours();

        // Filter tours by specific locations using regex patterns for different regions
        List<Tour> domesticToursNorth = filterToursByLocation(allTours, "Hạ Long|Sapa|Hà Nội|Ninh Bình|Hải Phòng|Thanh Hóa");
        List<Tour> domesticToursCentral = filterToursByLocation(allTours, "Đà Nẵng|Hội An|Huế|Quảng Bình|Nha Trang|Đà Lạt");
        List<Tour> domesticToursSouth = filterToursByLocation(allTours, "Phú Quốc|Châu Đốc|Côn Đảo");

        List<Tour> internationalToursAsia = filterToursByLocation(allTours, "Bangkok|Singapore|Tokyo|Seoul|Dubai");
        List<Tour> internationalToursEurope = filterToursByLocation(allTours, "Paris|London|Rome");
        List<Tour> internationalToursAmerica = filterToursByLocation(allTours, "New York");
        List<Tour> internationalToursAustralia = filterToursByLocation(allTours, "Sydney");

        // Filter tours by transport type
        List<Tour> tourByPlane = filterToursByTransport(allTours, "Máy Bay");
        List<Tour> tourByBus = filterToursByTransport(allTours, "Xe Khách|Xe Du Lịch");

        // Get a limited number of featured tours (first 5)
        List<Tour> featuredTours = getFeaturedTours(allTours, 5);

        // Set filtered tour lists as request attributes to pass to the JSP
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

        // Log a message to indicate the servlet is running
        System.out.println("📌 TourController has been executed!");

        // Create a new instance of TourDAO (redundant, as it's already an instance variable)
        TourDAO tourDAO = new TourDAO();

        // Retrieve filter and sort parameters from the request
        String filter = request.getParameter("filter");
        String sort = request.getParameter("sort");
        String tourType = null;

        // Log the received filter and sort parameters for debugging
        System.out.println("Filter: " + filter);
        System.out.println("Sort: " + sort);

        // Determine the tour type based on the filter parameter
        if ("Trong nước".equals(filter)) {
            tourType = "Domestic"; // Domestic tours
        } else if ("Ngoài nước".equals(filter)) {
            tourType = "International"; // International tours
        }

        // Log the mapped tour type for debugging
        System.out.println("Tour Type: " + tourType);

        // Set a default sort order (price ascending) if sort is null or invalid
        if (sort == null || (!sort.equals("priceAsc") && !sort.equals("priceDesc"))) {
            sort = "priceAsc"; // Default to ascending price
        }

        // Initialize pagination variables
        int page = 1; // Default to the first page
        int recordsPerPage = 5; // Number of tours per page

        // Retrieve the requested page number from the request parameters
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1; // Default to page 1 if the page parameter is invalid
            }
        }

        // Calculate the total number of tours and pages for pagination
        int totalRecords = tourDAO.getTotalTours(tourType);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        // Ensure the page number is within valid bounds
        if (page > totalPages) {
            page = totalPages; // Set to last page if requested page exceeds total
        }
        if (page < 1) {
            page = 1; // Set to first page if requested page is less than 1
        }

        // Calculate the offset for database query based on the current page
        int offset = (page - 1) * recordsPerPage;

        // Retrieve sorted and filtered tours from the database
        List<Tour> tours = tourDAO.getSortedTours(tourType, sort, offset, recordsPerPage);

        // Log the number of retrieved tours and their details for debugging
        System.out.println("Number of tours retrieved: " + (tours != null ? tours.size() : 0));
        if (tours != null) {
            for (Tour tour : tours) {
                System.out.println("Tour: " + tour.getName() + ", Type: " + tour.getType());
            }
        }

        // Check if no tours were found and handle the error case
        if (tours == null || tours.isEmpty()) {
            request.setAttribute("error", "No tours available.");
            request.getRequestDispatcher("/List.jsp").forward(request, response);
            return;
        }

        // Set pagination and tour data as request attributes to pass to the JSP
        request.setAttribute("tours", tours); // List of tours to display
        request.setAttribute("currentPage", page); // Current page number
        request.setAttribute("totalPages", totalPages); // Total number of pages
        request.setAttribute("filter", filter); // Current filter value
        request.setAttribute("sort", sort); // Current sort order

        // Forward the request to List.jsp to display the sorted and filtered tours
        request.getRequestDispatcher("List.jsp").forward(request, response);
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
        // Currently empty; no POST functionality implemented
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

    /**
     * Filters tours by matching location using a regex pattern.
     *
     * @param tours List of all tours to filter
     * @param regex Regular expression pattern for matching locations
     * @return List of tours that match the location pattern
     */
    private List<Tour> filterToursByLocation(List<Tour> tours, String regex) {
        List<Tour> result = new ArrayList<>();
        for (Tour t : tours) {
            // Use case-insensitive matching for location
            if (t.getLocation().matches("(?i)" + regex)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Filters tours by transport type.
     *
     * @param tours List of all tours to filter
     * @param regex Regular expression pattern for matching transport types
     * @return List of tours that match the transport pattern
     */
    private List<Tour> filterToursByTransport(List<Tour> tours, String regex) {
        List<Tour> result = new ArrayList<>();
        for (Tour t : tours) {
            // Use case-insensitive matching for transport
            if (t.getTransport().matches("(?i)" + regex)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Retrieves the first N featured tours.
     *
     * @param tours List of all tours
     * @param limit Maximum number of featured tours to return
     * @return List of up to 'limit' featured tours
     */
    private List<Tour> getFeaturedTours(List<Tour> tours, int limit) {
        List<Tour> featured = new ArrayList<>();
        // Add up to 'limit' tours or the total number of tours, whichever is smaller
        for (int i = 0; i < Math.min(tours.size(), limit); i++) {
            featured.add(tours.get(i));
        }
        return featured;
    }
}
