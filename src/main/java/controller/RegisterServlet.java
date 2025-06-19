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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.User;

/**
 * Servlet for handling user registration.
 *
 * @author hungn
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"}) // Maps the servlet to the "/register" URL
public class RegisterServlet extends HttpServlet {

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
        // Forward the request to Register.jsp to display the registration form
        request.getRequestDispatcher("Register.jsp").forward(request, response);
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
        try {
            // Retrieve user input from the registration form
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String fullName = request.getParameter("full_name");
            String phoneNumber = request.getParameter("phone_number");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirm_password");

            // Validate that all required fields are provided and not empty
            if (username == null || username.isEmpty()
                    || email == null || email.isEmpty()
                    || fullName == null || fullName.isEmpty()
                    || phoneNumber == null || phoneNumber.isEmpty()
                    || password == null || password.isEmpty()
                    || confirmPassword == null || confirmPassword.isEmpty()) {
                // Set an error message if any required field is missing
                request.setAttribute("error", "Please fill in all required fields!");
                // Forward back to Register.jsp to display the error
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            // Check if the email format is valid
            if (!isValidEmail(email)) {
                // Set an error message if the email format is invalid
                request.setAttribute("error", "Invalid email format!");
                // Forward back to Register.jsp to display the error
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            // Check if the phone number is exactly 10 digits
            if (!isValidPhoneNumber(phoneNumber)) {
                // Set an error message if the phone number is not 10 digits
                request.setAttribute("error", "Phone number must be exactly 10 digits!");
                // Forward back to Register.jsp to display the error
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            // Check if the password and confirm password match
            if (!password.equals(confirmPassword)) {
                // Set an error message if passwords do not match
                request.setAttribute("error", "Passwords do not match!");
                // Forward back to Register.jsp to display the error
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            // Create an instance of UserDAO to interact with the database
            UserDAO userDAO = new UserDAO();

            // Check if the username already exists in the database
            if (userDAO.checkUser(username)) {
                // Set an error message if the username is already taken
                request.setAttribute("error", "Username already exists!");
                // Forward back to Register.jsp to display the error
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            // Check if the email is already registered in the database
            if (userDAO.checkEmail(email)) {
                // Set an error message if the email is already in use
                request.setAttribute("error", "This email is already in use!");
                // Forward back to Register.jsp to display the error
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            // Hash the password using MD5 for security (Note: BCrypt is recommended for stronger hashing)
            String hashedPassword = userDAO.hashMd5(password);

            // Create a new User object with the provided details, defaulting the role to "User"
            User newUser = new User(username, hashedPassword, fullName, email, phoneNumber, "User");

            // Attempt to register the new user in the database
            boolean isRegistered = userDAO.registerUser(newUser);

            if (isRegistered) {
                // If registration is successful, set a success message
                request.setAttribute("success", "Registration successful! Please log in.");
                // Forward to Login.jsp to allow the user to log in
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            } else {
                // If registration fails, set an error message
                request.setAttribute("error", "Registration failed! Please try again.");
                // Forward back to Register.jsp to display the error
                request.getRequestDispatcher("Register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Log any unexpected errors for debugging purposes
            e.printStackTrace();
            // Set a generic error message for the user
            request.setAttribute("error", "System error! Please try again.");
            // Forward back to Register.jsp to display the error
            request.getRequestDispatcher("Register.jsp").forward(request, response);
        }
    }

    // Method to check if the email format is valid
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

        // Helper method to validate phone number (exactly 10 digits)
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
