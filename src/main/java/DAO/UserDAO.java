package DAO;

import db.DBContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

public class UserDAO extends DBContext {

    // Method to register a new user in the database
    public boolean registerUser(User user) {
        // SQL query to insert a new user into the User table
        String sql = "INSERT INTO [User] (username, password, full_name, email, phone_number, user_role) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        // Use try-with-resources to automatically close database resources
        try (Connection conn = new DBContext().getConnection(); // Establish database connection
                PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL query

            // Set the parameters for the new user
            stmt.setString(1, user.getUsername()); // Username
            stmt.setString(2, user.getPassword()); // Password (Note: Should be hashed before saving)
            stmt.setString(3, user.getFullName()); // Full name
            stmt.setString(4, user.getEmail()); // Email
            stmt.setString(5, user.getPhoneNumber()); // Phone number
            stmt.setString(6, user.getUserRole()); // User role

            // Execute the insert query and get the number of affected rows
            int rowsAffected = stmt.executeUpdate();
            // Return true if the user was successfully added (at least one row affected)
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
            return false;
        }
    }

    // Method to authenticate a user by username and password
    public User loginUser(String username, String password) {
        // SQL query to select a user by username and password
        String sql = "SELECT * FROM [User] WHERE username = ? AND password = ?";

        // Use try-with-resources to automatically close database resources
        try (Connection conn = getConnection(); // Establish database connection
                PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL query
            // Set the username and password as parameters in the query
            stmt.setString(1, username);
            stmt.setString(2, password);
            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Check if a user is found
            if (rs.next()) {
                // Create and return a new User object with the retrieved data
                return new User(
                        rs.getInt("id"), // User ID
                        rs.getString("username"), // Username
                        rs.getString("password"), // Password
                        rs.getString("full_name"), // Full name
                        rs.getString("email"), // Email
                        rs.getString("phone_number"), // Phone number
                        rs.getString("gender"), // Gender
                        rs.getString("address"), // Address
                        rs.getString("country"), // Country
                        rs.getDate("date_of_birth"), // Date of birth
                        rs.getString("workplace"), // Workplace
                        rs.getString("job_title"), // Job title
                        rs.getString("user_role") // User role
                );
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return null if no user is found or an error occurs
        return null;
    }

    // Method to check if a username already exists in the database
    public boolean checkUser(String username) {
        // SQL query to check if a user with the given username exists
        String sql = "SELECT id FROM [User] WHERE username = ?";

        // Use try-with-resources to automatically close database resources
        try (Connection conn = getConnection(); // Establish database connection
                PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL query
            // Set the username as the parameter in the query
            stmt.setString(1, username);
            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();
            // Return true if a user with the given username exists
            return rs.next();
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return false if an error occurs
        return false;
    }

    // Method to check if an email already exists in the database
    public boolean checkEmail(String email) {
        // SQL query to check if a user with the given email exists
        String sql = "SELECT id FROM [User] WHERE email = ?";

        // Use try-with-resources to automatically close database resources
        try (Connection conn = getConnection(); // Establish database connection
                PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL query
            // Set the email as the parameter in the query
            stmt.setString(1, email);
            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();
            // Return true if a user with the given email exists
            return rs.next();
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return false if an error occurs
        return false;
    }

    // Method to update a user's details in the database
    public boolean updateUser(User user) {
        // SQL query to update user details based on their ID
        String sql = "UPDATE [User] SET full_name = ?, email = ?, phone_number = ?, gender = ?, address = ?, "
                + "country = ?, date_of_birth = ?, workplace = ?, job_title = ? WHERE id = ?";

        // Use try-with-resources to automatically close database resources
        try (Connection conn = getConnection(); // Establish database connection
                PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL query
            // Set the parameters for the updated user details
            stmt.setString(1, user.getFullName()); // Full name
            stmt.setString(2, user.getEmail()); // Email
            stmt.setString(3, user.getPhoneNumber()); // Phone number
            stmt.setString(4, user.getGender()); // Gender
            stmt.setString(5, user.getAddress()); // Address
            stmt.setString(6, user.getCountry()); // Country
            stmt.setDate(7, user.getDate_of_birth()); // Date of birth
            stmt.setString(8, user.getWorkplace()); // Workplace
            stmt.setString(9, user.getJob_title()); // Job title
            stmt.setInt(10, user.getId()); // User ID

            // Execute the update query and get the number of affected rows
            int rowsAffected = stmt.executeUpdate();
            // Return true if the user was successfully updated (at least one row affected)
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return false if an error occurs
        return false;
    }

    // Method to update a user's password in the database
    public boolean updatePassword(User user) {
        // SQL query to update the user's password based on their ID
        String sql = "UPDATE [User] SET password = ? WHERE id = ?";

        // Use try-with-resources to automatically close database resources
        try (Connection conn = getConnection(); // Establish database connection
                PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL query
            // Set the parameters for the updated password
            stmt.setString(1, user.getPassword()); // New password (Note: Should be hashed before saving)
            stmt.setInt(2, user.getId()); // User ID

            // Execute the update query and get the number of affected rows
            int rowsAffected = stmt.executeUpdate();
            // Return true if the password was successfully updated (at least one row
            // affected)
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return false if an error occurs
        return false;
    }

    // Method to update a user's password by userId and hashedPassword (for password
    // reset)
    public boolean updatePassword(int userId, String hashedPassword) {
        // SQL query to update the user's password based on their ID
        String sql = "UPDATE [User] SET password = ? WHERE id = ?";
        System.out.println("UserDAO.updatePassword - Attempting to update password for userId: " + userId);
        System.out.println("UserDAO.updatePassword - Using hashedPassword length: "
                + (hashedPassword != null ? hashedPassword.length() : "null"));

        // Use try-with-resources to automatically close database resources
        try (Connection conn = getConnection(); // Establish database connection
                PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL query
            // Set the parameters for the updated password
            stmt.setString(1, hashedPassword); // New hashed password
            stmt.setInt(2, userId); // User ID

            // Execute the update query and get the number of affected rows
            int rowsAffected = stmt.executeUpdate();
            System.out.println("UserDAO.updatePassword - Rows affected: " + rowsAffected);

            // Return true if the password was successfully updated (at least one row
            // affected)
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            System.out.println("UserDAO.updatePassword - SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("UserDAO.updatePassword - General Error: " + e.getMessage());
            e.printStackTrace();
        }
        // Return false if an error occurs
        return false;
    }

    // Method to get a user by email address
    public User getUserByEmail(String email) {
        // SQL query to select a user by email
        String sql = "SELECT * FROM [User] WHERE email = ?";

        // Use try-with-resources to automatically close database resources
        try (Connection conn = getConnection(); // Establish database connection
                PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL query
            // Set the email as the parameter in the query
            stmt.setString(1, email);
            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Check if a user is found
            if (rs.next()) {
                // Create and return a new User object with the retrieved data
                return new User(
                        rs.getInt("id"), // User ID
                        rs.getString("username"), // Username
                        rs.getString("password"), // Password
                        rs.getString("full_name"), // Full name
                        rs.getString("email"), // Email
                        rs.getString("phone_number"), // Phone number
                        rs.getString("gender"), // Gender
                        rs.getString("address"), // Address
                        rs.getString("country"), // Country
                        rs.getDate("date_of_birth"), // Date of birth
                        rs.getString("workplace"), // Workplace
                        rs.getString("job_title"), // Job title
                        rs.getString("user_role") // User role
                );
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return null if no user is found or an error occurs
        return null;
    }

    // Method to get a user by ID
    public User getUserById(int userId) {
        // SQL query to select a user by ID
        String sql = "SELECT * FROM [User] WHERE id = ?";

        // Use try-with-resources to automatically close database resources
        try (Connection conn = getConnection(); // Establish database connection
                PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepare the SQL query
            // Set the user ID as the parameter in the query
            stmt.setInt(1, userId);
            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Check if a user is found
            if (rs.next()) {
                // Create and return a new User object with the retrieved data
                return new User(
                        rs.getInt("id"), // User ID
                        rs.getString("username"), // Username
                        rs.getString("password"), // Password
                        rs.getString("full_name"), // Full name
                        rs.getString("email"), // Email
                        rs.getString("phone_number"), // Phone number
                        rs.getString("gender"), // Gender
                        rs.getString("address"), // Address
                        rs.getString("country"), // Country
                        rs.getDate("date_of_birth"), // Date of birth
                        rs.getString("workplace"), // Workplace
                        rs.getString("job_title"), // Job title
                        rs.getString("user_role") // User role
                );
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return null if no user is found or an error occurs
        return null;
    }

    // Method to hash a string using the MD5 algorithm
    public String hashMd5(String raw) {
        try {
            // Create an MD5 MessageDigest instance
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Compute the MD5 hash of the input string
            byte[] mess = md.digest(raw.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : mess) {
                sb.append(String.format("%02x", b)); // Format each byte as a two-digit hexadecimal number
            }

            // Return the hashed string
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            // Log an error if the MD5 algorithm is not available
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            // Return an empty string if an error occurs
            return "";
        }
    }

}
