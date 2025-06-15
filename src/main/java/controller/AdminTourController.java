/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.AdminTourDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.AdminTour;
import model.Tour;

/**
 *
 * @author ACER
 */
@WebServlet(name = "TourController", urlPatterns = {"/admintourscontroller"})
public class AdminTourController extends HttpServlet {
// Method to handle HTTP GET requests for displaying tour details or a list of tours

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Debug statement to indicate that the TourController is running
        System.out.println("📌 TourController is running!");

        // Create an instance of AdminTourDAO to interact with the tour database
        AdminTourDAO tourDAO = new AdminTourDAO();
        // Get the tour ID parameter from the request
        String tourIdParam = request.getParameter("id");

        // Check if a tour ID is provided in the request
        if (tourIdParam != null) {
            try {
                // Parse the tour ID from the request parameter
                int tourId = Integer.parseInt(tourIdParam);
                // Retrieve the tour details from the database using the tour ID
                AdminTour tour = tourDAO.getTourById(tourId);
                if (tour != null) {
                    // If the tour exists, set it as a request attribute to pass to the JSP page
                    request.setAttribute("tour", tour);
                    // Forward the request to the tour list JSP page to display the tour details
                    request.getRequestDispatcher("tourList.jsp").forward(request, response);
                } else {
                    // If the tour does not exist, redirect to the tour list page
                    response.sendRedirect("list");
                }
            } catch (NumberFormatException e) {
                // If the tour ID is not a valid integer, redirect to the tour list page
                response.sendRedirect("list");
            }
        } else {
            // If no tour ID is provided, retrieve all tours from the database
            ArrayList<AdminTour> tours = tourDAO.getAllTours();
            // Set the list of tours as a request attribute to pass to the JSP page
            request.setAttribute("tours", tours);
            // Forward the request to the tour list JSP page to display the list of tours
            request.getRequestDispatcher("tourList.jsp").forward(request, response);
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
        // No implementation provided for POST requests
    }
}
