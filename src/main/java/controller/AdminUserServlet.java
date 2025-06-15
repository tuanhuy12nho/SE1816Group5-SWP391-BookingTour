package controller;

import DAO.AdminUserDAO;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.AdminUser;
import util.SessionLogin;

@WebServlet("/AdminUserServlet")
public class AdminUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Create an instance of AdminUserDAO to interact with the database
        AdminUserDAO userDAO = new AdminUserDAO();

        // Check if the current user is an admin using the SessionLogin utility
        // If not an admin, redirect to the admin.jsp page and exit the method
        if (!SessionLogin.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin.jsp");
            return;
        }

        // Retrieve the "action" parameter from the request to determine what to do
        String action = request.getParameter("action");
        // If the action is "addnew", forward to addCustomer.jsp
        if ("addnew".equals(action)) {
            request.getRequestDispatcher("/Admin/addCustomer.jsp").forward(request, response);
        }

        // If the action is "edit", prepare the edit form for a specific user
        if ("edit".equals(action)) {
            // Get the user ID from the request parameters and parse it to an integer
            int userIdz = Integer.parseInt(request.getParameter("id"));

            // Fetch the user details from the database using the user ID
            AdminUser editUser = userDAO.getUserById(userIdz);

            // Store the user object in the request attributes to pass it to the JSP page
            request.setAttribute("editUser", editUser);

            // Forward the request to the editCustomer.jsp page for editing the user
            request.getRequestDispatcher("/Admin/editCustomer.jsp").forward(request, response);
        } // If the action is "confirmDelete", delete the specified user
        else if ("confirmDelete".equals(action)) {
            // Get the user ID from the request parameters and parse it to an integer
            int userId = Integer.parseInt(request.getParameter("id"));

            // Delete the user from the database using the user ID
            userDAO.deleteUser(userId);

            // Redirect back to the AdminUserServlet to refresh the user list
            response.sendRedirect("AdminUserServlet");
        } // If no specific action is provided, display the list of all users
        else {
            // Retrieve the list of all users from the database
            List<AdminUser> users = userDAO.getAllUsers();

            // Store the user list in the request attributes to pass it to the JSP page
            request.setAttribute("users", users);

            // Forward the request to the user.jsp page to display the user list
            request.getRequestDispatcher("/Admin/user.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Create an instance of AdminUserDAO to interact with the database
        AdminUserDAO userDAO = new AdminUserDAO();

        // Retrieve the "action" parameter from the request to determine what to do
        String action = request.getParameter("action");

        // If the action is "delete", remove the specified user from the database
        if ("delete".equals(action)) {
            // Get the user ID from the request parameters and parse it to an integer
            int id = Integer.parseInt(request.getParameter("id"));

            // Delete the user from the database using the user ID
            userDAO.deleteUser(id);

            // Redirect back to the AdminUserServlet to refresh the user list
            response.sendRedirect("AdminUserServlet");
        } // If the action is "update", modify the details of an existing user
        else if ("update".equals(action)) {
            // Get the user ID from the request parameters and parse it to an integer
            int id = Integer.parseInt(request.getParameter("id"));

            // Retrieve updated user details from the request parameters
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String gender = request.getParameter("gender");
            String country = request.getParameter("country");
            String dateOfBirth = request.getParameter("dateOfBirth");
            String workplace = request.getParameter("workplace");
            String jobTitle = request.getParameter("jobTitle");

            // Check if the phone number is exactly 10 digits
            if (!isValidPhoneNumber(phoneNumber)) {
                response.sendRedirect("error.jsp?message=Phone number must be exactly 10 digits");
                return;
            }

            // Update the user in the database with the new details
            userDAO.updateUser(id, phoneNumber, address, gender, country, dateOfBirth, workplace, jobTitle);

            // Redirect back to the AdminUserServlet to refresh the user list
            response.sendRedirect("AdminUserServlet");
        } // If the action is "add", create and store a new user in the database
        else if ("add".equals(action)) {
            // Retrieve all necessary details for the new user from the request parameters
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            String userRole = request.getParameter("userRole");
            String address = request.getParameter("address");
            String gender = request.getParameter("gender");
            String country = request.getParameter("country");
            String dateOfBirth = request.getParameter("dateOfBirth");
            String workplace = request.getParameter("workplace");
            String jobTitle = request.getParameter("jobTitle");

            // Check if the email is valid
            if (!isValidEmail(email)) {
                response.sendRedirect("error.jsp?message=Invalid email format");
                return;
            }

            // Check if the user is at least 18 years old
            if (!isOver18(dateOfBirth)) {
                response.sendRedirect("error.jsp?message=User must be at least 18 years old");
                return;
            }

            // Check if the phone number is exactly 10 digits
            if (!isValidPhoneNumber(phoneNumber)) {
                response.sendRedirect("error.jsp?message=Phone number must be exactly 10 digits");
                return;
            }

            // Hash the password using MD5 for security before storing it
            String hashedPassword = userDAO.hashMd5(password);

            // Create a new AdminUser object with the provided details
            AdminUser newUser = new AdminUser(username, hashedPassword, fullName, email, phoneNumber, userRole, address, gender, country, dateOfBirth, workplace, jobTitle);

            // Add the new user to the database
            userDAO.addUser(newUser);

            // Redirect back to the AdminUserServlet to refresh the user list
            response.sendRedirect("AdminUserServlet");
        }

    }

    // Method to check if the email is valid
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Method to check if the user is over 18 years old
    private boolean isOver18(String dateOfBirth) {
        try {
            // Convert the date of birth string to LocalDate
            LocalDate dob = LocalDate.parse(dateOfBirth);
            // Get the current date
            LocalDate currentDate = LocalDate.now();
            // Calculate the period between the two dates
            Period period = Period.between(dob, currentDate);
            // Get the number of years (age)
            int age = period.getYears();
            // Return true if age >= 18
            return age >= 18;
        } catch (DateTimeParseException e) {
            // If the date of birth is invalid, return false
            return false;
        }
    }

    // Helper method to validate phone number (exactly 10 digits)
    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        // Check if the phone number contains exactly 10 digits
        return phoneNumber.matches("\\d{10}");
    }
}
