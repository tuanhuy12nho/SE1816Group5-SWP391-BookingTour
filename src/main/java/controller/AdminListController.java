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
import java.util.ArrayList;
import java.util.List;
import model.AdminTour;

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
        // Debug statement to indicate that the TourController is running
        System.out.println("📌 TourController is running!");

        // Create an instance of AdminTourDAO to interact with the tour database
        AdminTourDAO tourDAO = new AdminTourDAO();
        // Retrieve all tours from the database
        ArrayList<AdminTour> tours = tourDAO.getAllTours();

        // Initialize pagination parameters
        int page = 1;  // Default page is 1
        int pageSize = 5;  // Number of tours per page
        // Get the page parameter from the request, if provided
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            // Parse the page number from the request parameter
            page = Integer.parseInt(pageParam);
        }

        // Calculate the total number of pages
        int totalTours = tours.size();  // Total number of tours
        int totalPages = (int) Math.ceil((double) totalTours / pageSize);  // Total pages, rounded up

        // Determine the range of tours to display on the current page
        int fromIndex = (page - 1) * pageSize;  // Starting index for the current page
        int toIndex = Math.min(fromIndex + pageSize, totalTours);  // Ending index, ensuring it doesn't exceed the total number of tours
        // Extract the sublist of tours for the current page
        List<AdminTour> paginatedTours = tours.subList(fromIndex, toIndex);

        // Set attributes to pass to the JSP page
        request.setAttribute("list", paginatedTours);  // The paginated list of tours
        request.setAttribute("currentPage", page);     // The current page number
        request.setAttribute("totalPages", totalPages); // The total number of pages

        // Forward the request to the JSP page to display the paginated list of tours
        request.getRequestDispatcher("Admin/admin.jsp").forward(request, response);
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
