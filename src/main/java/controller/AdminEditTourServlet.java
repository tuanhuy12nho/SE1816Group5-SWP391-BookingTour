package controller;

import DAO.AdminTourDAO;
import model.Tour;
import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.time.LocalDate;
import model.AdminTour;
import java.math.BigDecimal;

@WebServlet(name = "EditTourServlet", urlPatterns = {"/admineditTour"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class AdminEditTourServlet extends HttpServlet {

    // Declare an instance of AdminTourDAO to interact with the tour database
    private AdminTourDAO tourDAO = new AdminTourDAO();

    // Method to handle HTTP GET requests for displaying the tour edit form
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Parse the tour ID from the request parameter
        int id = Integer.parseInt(request.getParameter("id"));
        // Retrieve the tour details from the database using the tour ID
        AdminTour tour = tourDAO.getTourById(id);
        // Set the tour object as a request attribute to pass to the JSP page
        request.setAttribute("tour", tour);
        // Forward the request to the edit tour JSP page to display the form
        request.getRequestDispatcher("Admin/editTour.jsp").forward(request, response);
    }

    // Method to handle HTTP POST requests for updating a tour
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Initialize variables to store tour details
        int id = 0;
        String name = null;
        String location = null;
        String transport = null;
        BigDecimal price = null; // Use BigDecimal instead of double for precise monetary values
        String description = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        String type = null;

        try {
            // Set the character encoding of the request to UTF-8 to handle special characters
            request.setCharacterEncoding("UTF-8");

            // Retrieve form parameters from the request
            id = Integer.parseInt(request.getParameter("id"));         // Tour ID
            name = request.getParameter("name");                       // Tour name
            location = request.getParameter("location");               // Tour location
            transport = request.getParameter("transport");             // Mode of transport
            String priceStr = request.getParameter("price");           // Price as a string
            description = request.getParameter("description");         // Tour description
            startDate = LocalDate.parse(request.getParameter("startDate")); // Start date
            endDate = LocalDate.parse(request.getParameter("endDate"));     // End date
            type = request.getParameter("type");                       // Tour type

            // Get the current date for comparison
            LocalDate currentDate = LocalDate.now();

            // Convert the price string to BigDecimal and handle invalid format
            try {
                price = new BigDecimal(priceStr);
            } catch (NumberFormatException e) {
                // If the price format is invalid, set an error message and forward back to the form
                request.setAttribute("message", "❌ Invalid price format!");
                forwardWithInputs(request, response, id, name, location, transport, null, description, startDate, endDate, type);
                return;
            }

            // Validate that the price is at least 1,000,000 VND
            if (price.compareTo(new BigDecimal("1000000")) < 0) {
                // If price is less than 1,000,000 VND, set an error message and forward back to the form
                request.setAttribute("message", "❌ The price must be at least 1,000,000 VND!");
                forwardWithInputs(request, response, id, name, location, transport, price, description, startDate, endDate, type);
                return;
            }

            // Validate that the start date is at least 1 day after the current date
            if (!startDate.isAfter(currentDate.plusDays(1))) {
                // If the start date is not after the current date + 1 day, set an error message and forward back to the form
                request.setAttribute("message", "❌ The start date must be at least 1 day after the current date!");
                forwardWithInputs(request, response, id, name, location, transport, price, description, startDate, endDate, type);
                return;
            }

            // Validate that the end date is after the start date
            if (!endDate.isAfter(startDate)) {
                // If the end date is not after the start date, set an error message and forward back to the form
                request.setAttribute("message", "❌ The end date must be after the start date!");
                forwardWithInputs(request, response, id, name, location, transport, price, description, startDate, endDate, type);
                return;
            }

            // Retrieve the existing tour to get the current image URL
            AdminTour existingTour = tourDAO.getTourById(id);
            String imageUrl = existingTour != null ? existingTour.getImageUrl() : "";

            // Check if a new image file has been uploaded
            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                // Get the name of the uploaded file
                String fileName = filePart.getSubmittedFileName();
                // Define the upload directory path (inside the web application's directory)
                String uploadDir = getServletContext().getRealPath("/") + "uploads";
                // Create the upload directory if it doesn't exist
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdir();
                }

                // Update the image URL to the new file path
                imageUrl = "uploads/" + fileName;
                // Save the uploaded image to the server
                filePart.write(uploadDir + File.separator + fileName);
            }

            // Create a new AdminTour object with the updated details
            AdminTour tour = new AdminTour(
                    id, // Tour ID
                    name, // Tour name
                    location, // Tour location
                    price, // Tour price
                    transport, // Mode of transport
                    description, // Tour description
                    Date.valueOf(startDate), // Start date (converted to java.sql.Date)
                    Date.valueOf(endDate), // End date (converted to java.sql.Date)
                    imageUrl, // Image URL (updated if a new image was uploaded)
                    type // Tour type
            );
            // Update the tour in the database using AdminTourDAO
            tourDAO.editTour(tour);

            // Set a success message in the session and redirect to the admin tour list page
            request.getSession().setAttribute("message", "✅ Tour updated successfully!");
            response.sendRedirect(request.getContextPath() + "/adminTour");

        } catch (Exception e) {
            // Handle any exceptions that occur during the update process
            e.printStackTrace();
            // Set an error message and forward back to the form with the current inputs
            request.setAttribute("message", "❌ Error updating tour: " + e.getMessage());
            forwardWithInputs(request, response, id, name, location, transport, price, description, startDate, endDate, type);
        }
    }

    // Method to forward the request back to the edit form with the current input values
    private void forwardWithInputs(HttpServletRequest request, HttpServletResponse response,
            int id, String name, String location,
            String transport, BigDecimal price, String description, // Use BigDecimal instead of double
            LocalDate startDate, LocalDate endDate, String type)
            throws ServletException, IOException {

        // Retrieve the existing tour to get the current image URL
        AdminTour existingTour = tourDAO.getTourById(id);
        String imageUrl = existingTour != null ? existingTour.getImageUrl() : "";

        // Create a new Tour object with the current input values
        Tour tour = new Tour(
                id, // Tour ID
                name, // Tour name
                location, // Tour location
                price, // Tour price
                transport, // Mode of transport
                description, // Tour description
                Date.valueOf(startDate), // Start date (converted to java.sql.Date)
                Date.valueOf(endDate), // End date (converted to java.sql.Date)
                imageUrl, // Image URL (retained from the existing tour)
                type // Tour type
        );

        // Set the tour object as a request attribute to pass to the JSP page
        request.setAttribute("tour", tour);
        // Forward the request to the edit tour JSP page to display the form with the current values
        request.getRequestDispatcher("/Admin/editTour.jsp").forward(request, response);
    }
}
