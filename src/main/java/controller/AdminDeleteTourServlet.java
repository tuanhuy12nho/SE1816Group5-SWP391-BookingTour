package controller;

import DAO.AdminTourDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet("/admindeleteTour")
public class AdminDeleteTourServlet extends HttpServlet {

    // Declare an instance of AdminTourDAO to interact with the tour database
    private AdminTourDAO tourDAO = new AdminTourDAO();

    // Method to handle HTTP GET requests for deleting a tour
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

        try {
            // Parse the tour ID from the request parameter
            int tourId = Integer.parseInt(request.getParameter("id"));

            // Delete the tour from the database using AdminTourDAO
            tourDAO.deleteTour(tourId);

            // Set a success message in the session to notify the user
            request.getSession().setAttribute("message", "✅ Tour has been successfully deleted!");
            // Redirect to the admin tour list page to refresh the list
            response.sendRedirect(request.getContextPath() + "/adminlist");
        } catch (Exception e) {
            // Print the stack trace if an error occurs
            e.printStackTrace();
            // Set an error message in the session to notify the user of the failure
            request.getSession().setAttribute("message", "❌ Error deleting tour: " + e.getMessage());
            // Redirect to the admin tour list page (Note: There might be a typo in the path "Admin/adminTour")
            response.sendRedirect(request.getContextPath() + "adminlist");
        }
    }
}
