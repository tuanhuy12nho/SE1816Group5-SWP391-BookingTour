package controller;

import DAO.AdminTourDAO;
import model.Tour;
import java.io.IOException;
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

@WebServlet("/adminaddTour")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class AdminAddTourServlet extends HttpServlet {

    // Declare an instance of AdminTourDAO to interact with the tour database
    private AdminTourDAO tourDAO = new AdminTourDAO();
     @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         request.getRequestDispatcher("/Admin/addTour.jsp").forward(request, response);
    }

    // Method to handle HTTP POST requests for adding a new tour
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Set the character encoding of the request to UTF-8 to handle special characters
            request.setCharacterEncoding("UTF-8");

            // Retrieve form parameters from the request
            String name = request.getParameter("name");            // Tour name
            String location = request.getParameter("location");    // Tour location
            String transport = request.getParameter("transport");  // Mode of transport
            String description = request.getParameter("description"); // Tour description
            String type = request.getParameter("type");            // Tour type

            // Retrieve and clean the price string by removing all whitespace
            String priceStr = request.getParameter("price").replaceAll("\\s+", "");
            String startDateStr = request.getParameter("startDate"); // Start date as a string
            String endDateStr = request.getParameter("endDate");     // End date as a string

            // Store the form data in request attributes to preserve them in case of errors
            request.setAttribute("name", name);
            request.setAttribute("location", location);
            request.setAttribute("transport", transport);
            request.setAttribute("description", description);
            request.setAttribute("type", type);
            request.setAttribute("price", priceStr);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);

            // Convert the price string to a BigDecimal for numerical operations
            BigDecimal price = new BigDecimal(priceStr);

            // Validate that the price is at least 1,000,000 VND
            if (price.compareTo(new BigDecimal("1000000")) < 0) {
                // If price is less than 1,000,000 VND, set an error message and forward back to the form
                request.setAttribute("message", "❌ The tour price must be at least 1,000,000 VND!");
                request.getRequestDispatcher("/Admin/addTour.jsp").forward(request, response);
                return;
            }

            // Get the current date for comparison
            LocalDate currentDate = LocalDate.now();
            // Parse the start and end dates from their string representations
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            // Validate that the start date is at least 1 day after the current date
            if (!startDate.isAfter(currentDate.plusDays(1))) {
                // If the start date is not after the current date + 1 day, set an error message and forward back to the form
                request.setAttribute("message", "❌ The start date must be at least 1 day after the current date!");
                request.getRequestDispatcher("/Admin/addTour.jsp").forward(request, response);
                return;
            }

            // Validate that the end date is after the start date
            if (!endDate.isAfter(startDate)) {
                // If the end date is not after the start date, set an error message and forward back to the form
                request.setAttribute("message", "❌ The end date must be after the start date!");
                request.getRequestDispatcher("/Admin/addTour.jsp").forward(request, response);
                return;
            }

            // Check if an image file has been uploaded
            Part filePart = request.getPart("image");
            if (filePart == null || filePart.getSize() <= 0 || filePart.getSubmittedFileName().isEmpty()) {
                // If no image is uploaded, set an error message and forward back to the form
                request.setAttribute("message", "❌ You must upload a representative image for the tour!");
                request.getRequestDispatcher("/Admin/addTour.jsp").forward(request, response);
                return;
            }

            // Get the name of the uploaded file
            String fileName = filePart.getSubmittedFileName();
            // Define the upload directory path (inside the web application's directory)
            String uploadDir = getServletContext().getRealPath("/") + "uploads";

            // Create the upload directory if it doesn't exist
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdir();
            }

            // Define the relative path to the image for storage in the database
            String imagePath = "uploads/" + fileName;
            // Define the full file path for saving the uploaded image
            File file = new File(uploadDir + File.separator + fileName);
            // Save the uploaded image to the server
            filePart.write(file.getAbsolutePath());

            // Create a new AdminTour object with the provided data
            AdminTour tour = new AdminTour(
                    0, // ID (0 as it will be auto-generated by the database)
                    name, // Tour name
                    location, // Tour location
                    price, // Tour price
                    transport, // Mode of transport
                    description, // Tour description
                    java.sql.Date.valueOf(startDate), // Start date (converted to java.sql.Date)
                    java.sql.Date.valueOf(endDate), // End date (converted to java.sql.Date)
                    imagePath, // Image path
                    type // Tour type
            );
            // Add the tour to the database using the AdminTourDAO
            tourDAO.addTour(tour);

            // Set a success message in the session and redirect to the admin tour page
            request.getSession().setAttribute("message", "✅ Successfully added a new tour!");
            response.sendRedirect(request.getContextPath() + "/adminTour");

        } catch (NumberFormatException e) {
            // Handle invalid price format errors
            request.setAttribute("message", "❌ Invalid price format!");
            request.getRequestDispatcher("/Admin/addTour.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle any other exceptions that may occur
            request.setAttribute("message", "❌ An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/Admin/addTour.jsp").forward(request, response);
        }
    }
}
