/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.AdminTourDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.AdminTour;
import model.User;

/**
 *
 * @author ACER
 */
@WebServlet(name = "ListController", urlPatterns = {"/adminlist"})
public class AdminListController extends HttpServlet {

    // Method to handle HTTP GET requests for displaying a paginated list of tours
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

        // Debug statement to indicate that the AdminListController is running
        System.out.println("📌 AdminListController is running!");

        // Create an instance of AdminTourDAO to interact with the tour database
        AdminTourDAO tourDAO = new AdminTourDAO();
        
        // Get filter and sort parameters from the request
        String filter = request.getParameter("filter");
        String sort = request.getParameter("sort");
        String tourType = null;

        // Determine the tour type based on the filter parameter
        if ("Trong nước".equals(filter)) {
            tourType = "Domestic";  // Set tour type to Domestic for "Trong nước" (within the country)
        } else if ("Ngoài nước".equals(filter)) {
            tourType = "International";  // Set tour type to International for "Ngoài nước" (outside the country)
        }

        // Check and set a default sort value if sort is null or invalid
        if (sort == null || (!sort.equals("priceAsc") && !sort.equals("priceDesc"))) {
            sort = "priceAsc";  // Default to ascending price order if sort is not specified or invalid
        }
        
        // Get tour counts for filter dropdown
        int totalToursCount = tourDAO.getTotalTours(null);
        int domesticToursCount = tourDAO.getTotalTours("Domestic");
        int internationalToursCount = tourDAO.getTotalTours("International");
        
        // Set the tour counts as request attributes
        request.setAttribute("totalTours", totalToursCount);
        request.setAttribute("domesticTours", domesticToursCount);
        request.setAttribute("internationalTours", internationalToursCount);
        
        // Initialize pagination parameters
        int page = 1;  // Default page is 1
        int pageSize = 5;  // Number of tours per page
        
        // Get the page parameter from the request, if provided
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                // Parse the page number from the request parameter
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1; // Ensure page is at least 1
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid page parameter: " + pageParam);
            }
        }

        // Calculate the offset for pagination (starting index of records for the current page)
        int offset = (page - 1) * pageSize;

        // Retrieve the list of tours with the specified filter, sort order, and pagination
        List<AdminTour> tours = tourDAO.getSortedTours(tourType, sort, offset, pageSize);

        // Get the total number of tours for the specified tour type (for pagination)
        int totalRecords = tourDAO.getTotalTours(tourType);
        
        // Calculate the total number of pages, rounding up
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // Set attributes to pass to the JSP page
        request.setAttribute("list", tours);  // The paginated, filtered, and sorted list of tours
        request.setAttribute("currentPage", page);  // The current page number
        request.setAttribute("totalPages", totalPages);  // The total number of pages
        request.setAttribute("filter", filter);  // The current filter value (for UI state)
        request.setAttribute("sort", sort);  // The current sort value (for UI state)

        // Forward the request to the correct JSP page to display the paginated list of tours
        request.getRequestDispatcher("Admin/adminTourList.jsp").forward(request, response);
    }

    // Method to handle HTTP POST requests (currently empty)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // No implementation provided for POST requests
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
