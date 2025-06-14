/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import DAO.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import model.User;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Servlet for handling user profile management, including updating profile
 * details and changing passwords.
 *
 * @author Admin
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet"}) // Maps the servlet to the "/UserServlet" URL
public class UserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. This method is not currently used but serves as a template for
     * direct HTML output.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set the response content type to HTML with UTF-8 encoding
        response.setContentType("text/html;charset=UTF-8");

        // Use PrintWriter to directly output an HTML page (currently a placeholder)
        try ( PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        // Forward the request to Profile.jsp to display the user's profile page
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }
        if ("showEditProfile".equals(action)) {
            request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
        } else if ("showChangePassword".equals(action)) {
            request.getRequestDispatcher("ChangePassword.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("Profile.jsp").forward(request, response);
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
        // Retrieve the "action" parameter to determine the requested operation
        String action = request.getParameter("action");

        // Get the current session to access the logged-in user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Create an instance of UserDAO to interact with the database
        UserDAO dao = new UserDAO();

        // If no user is logged in, redirect to the login page
        if (user == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // Handle the "updateProfile" action to modify user profile details
        if ("updateProfile".equals(action)) {
            // Retrieve profile details from the request
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            String gender = request.getParameter("gender");
            String address = request.getParameter("address");
            String country = request.getParameter("country");
            String dateOfBirth = request.getParameter("dateOfBirth");
            String workplace = request.getParameter("workplace");
            String jobTitle = request.getParameter("jobTitle");

            // Check if full name is provided and not empty
            if (fullName == null || fullName.trim().isEmpty()) {
                request.setAttribute("error", "Full name cannot be empty!");
                request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
                return;
            }

            // Check if email is valid
            if (!isValidEmail(email)) {
                request.setAttribute("error", "Invalid email format!");
                request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
                return;
            }

            // Check if email is already in use by another user
            if (!email.equals(user.getEmail()) && dao.checkEmail(email)) { // Skip if email unchanged
                request.setAttribute("error", "This email is already in use!");
                request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
                return;
            }

            // Check if phone number is valid (basic format: 10-11 digits)
            if (!isValidPhoneNumber(phoneNumber)) {
                request.setAttribute("error", "Invalid phone number format! Must be 10-11 digits.");
                request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
                return;
            }

            // Check if date of birth is valid and user is at least 18 years old
            if (!isOver18(dateOfBirth)) {
                request.setAttribute("error", "Invalid date of birth or user must be at least 18 years old!");
                request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
                return;
            }

            // Update the user object with new profile details from the request
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setGender(gender);
            user.setAddress(address);
            user.setCountry(country);
            user.setDate_of_birth(Date.valueOf(dateOfBirth));
            user.setWorkplace(workplace);
            user.setJob_title(jobTitle);

            // Attempt to update the user profile in the database
            boolean updated = dao.updateUser(user);
            if (updated) {
                // If successful, update the session with the modified user object
                session.setAttribute("user", user);
                // Redirect to the profile page to show the updated details
                response.sendRedirect("Profile.jsp");
            } else {
                // If update fails, set an error message and forward to the edit profile page
                request.setAttribute("error", "Profile update failed!");
                request.getRequestDispatcher("EditProfile.jsp").forward(request, response);
            }
        } // Handle the "changePassword" action to update the user's password
        else if ("changePassword".equals(action)) {
            // Retrieve password-related parameters from the request
            String currentPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            // Create a new instance of UserDAO (redundant, as 'dao' is already initialized)
            UserDAO userDAO = new UserDAO();

            // Hash the current password input for comparison with the stored password
            String hashedCurrentPassword = userDAO.hashMd5(currentPassword);

            // Check if the provided current password matches the stored hashed password
            if (!user.getPassword().equals(hashedCurrentPassword)) {
                // If incorrect, set an error message and forward to the change password page
                request.setAttribute("error", "Current password is incorrect!");
                request.getRequestDispatcher("ChangePassword.jsp").forward(request, response);
            } // Check if the new password matches the confirmation
            else if (!newPassword.equals(confirmPassword)) {
                // If they don't match, set an error message and forward to the change password page
                request.setAttribute("error", "Confirmation password does not match!");
                request.getRequestDispatcher("ChangePassword.jsp").forward(request, response);
            } else {
                // Hash the new password for storage
                String hashedNewPassword = userDAO.hashMd5(newPassword);
                user.setPassword(hashedNewPassword);

                // Attempt to update the password in the database
                boolean updated = userDAO.updatePassword(user);
                if (updated) {
                    // If successful, redirect to the profile page
                    response.sendRedirect("Profile.jsp");
                } else {
                    // If update fails, set an error message and forward to the change password page
                    request.setAttribute("error", "Password change failed!");
                    request.getRequestDispatcher("ChangePassword.jsp").forward(request, response);
                }
            }
        }
    }
    // Method to check if the email format is valid

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Method to check if phone number is valid (10-11 digits)
    private boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        String phoneRegex = "^\\d{10,11}$"; // Matches 10 or 11 digits
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    // Method to check if the user is over 18 years old
    private boolean isOver18(String dateOfBirth) {
        try {
            LocalDate dob = LocalDate.parse(dateOfBirth); // Convert string to LocalDate
            LocalDate currentDate = LocalDate.now();      // Get current date
            Period period = Period.between(dob, currentDate); // Calculate period between dates
            int age = period.getYears();                  // Get age in years
            return age >= 18;                             // Return true if age >= 18
        } catch (DateTimeParseException e) {
            return false; // Return false if date format is invalid
        }
    }
}
