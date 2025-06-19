package controller;

import DAO.AdminTourDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.AdminTour;

@WebServlet(urlPatterns = {"/adminTour"})
public class AdminTourServlet extends HttpServlet {
    // Declare an instance of AdminTourDAO to interact with the tour database

    private AdminTourDAO tourDAO = new AdminTourDAO();

    // Method to handle HTTP GET requests for displaying a paginated, filtered, and sorted list of tours
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve filter and sort parameters from the request
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

        // Initialize pagination parameters
        int page = 1;  // Default page is 1
        int recordsPerPage = 5;  // Number of tours per page

        // Get the page parameter from the request, if provided
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));  // Parse the page number
        }

        // Calculate the offset for pagination (starting index of records for the current page)
        int offset = (page - 1) * recordsPerPage;

        // Retrieve the list of tours with the specified filter, sort order, and pagination
        List<AdminTour> tours = tourDAO.getSortedTours(tourType, sort, offset, recordsPerPage);

        // Get the total number of tours for the specified tour type (for pagination)
        int totalRecords = tourDAO.getTotalTours(tourType);
        // Calculate the total number of pages, rounding up
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        // Set attributes to pass to the JSP page
        request.setAttribute("admintours", tours);  // The paginated, filtered, and sorted list of tours
        request.setAttribute("currentPage", page);  // The current page number
        request.setAttribute("totalPages", totalPages);  // The total number of pages
        request.setAttribute("filter", filter);  // The current filter value (for UI state)
        request.setAttribute("sort", sort);  // The current sort value (to maintain sorting state on the UI)

        // Forward the request to the admin tour list JSP page to display the results
        request.getRequestDispatcher("Admin/adminTourList.jsp").forward(request, response);
    }
}
