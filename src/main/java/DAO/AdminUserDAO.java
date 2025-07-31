package DAO;

import db.DBContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.AdminUser;

public class AdminUserDAO extends DBContext {

    // Method to retrieve all users from the database
    public List<AdminUser> getAllUsers() {
        // Initialize an empty list to store the user records
        List<AdminUser> users = new ArrayList<>();
        // SQL query to select user details from the User table
        String query = "SELECT id, username, full_name, email, phone_number, address, gender, country, date_of_birth, workplace, job_title FROM [User]";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement ps = conn.prepareStatement(query); // Prepare the SQL query
                  ResultSet rs = ps.executeQuery()) {  // Execute the query and get the result set

            // Loop through each row in the result set
            while (rs.next()) {
                // Create a new AdminUser object with the retrieved data
                AdminUser user = new AdminUser(
                        rs.getInt("id"), // User ID
                        rs.getString("username"), // Username
                        rs.getString("full_name"), // Full name
                        rs.getString("email"), // Email
                        rs.getString("phone_number"), // Phone number
                        rs.getString("address"), // Address
                        rs.getString("gender"), // Gender
                        rs.getString("country"), // Country
                        rs.getString("date_of_birth"),// Date of birth
                        rs.getString("workplace"), // Workplace
                        rs.getString("job_title") // Job title
                );
                // Add the user object to the list
                users.add(user);
                // Debug statement to print the username of the retrieved user
                System.out.println("📢 [DEBUG] Retrieved user: " + user.getUsername());
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
            // Print an error message if a SQL error occurs
            System.out.println("❌ [ERROR] SQL error while retrieving user list.");
        }
        // Debug statement to print the total number of users retrieved
        System.out.println("📢 [DEBUG] Total users retrieved: " + users.size());
        // Return the list of users
        return users;
    }

    // Method to add a new user to the database
    public void addUser(AdminUser user) {
        // SQL query to insert a new user into the User table
        String query = "INSERT INTO [User] (username, password, full_name, email, phone_number, user_role, address, gender, country, date_of_birth, workplace, job_title) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement ps = conn.prepareStatement(query)) {  // Prepare the SQL query
            // Set the parameters for the new user
            ps.setString(1, user.getUsername());      // Username
            ps.setString(2, user.getPassword());      // Password (Note: Should be hashed before saving)
            ps.setString(3, user.getFullName());      // Full name
            ps.setString(4, user.getEmail());         // Email
            ps.setString(5, user.getPhoneNumber());   // Phone number
            ps.setString(6, user.getUserRole());      // User role
            ps.setString(7, user.getAddress());       // Address
            ps.setString(8, user.getGender());        // Gender
            ps.setString(9, user.getCountry());       // Country
            ps.setString(10, user.getDateOfBirth());  // Date of birth
            ps.setString(11, user.getWorkplace());    // Workplace
            ps.setString(12, user.getJobTitle());     // Job title

            // Execute the insert query
            ps.executeUpdate();
            // Print a success message after adding the user
            System.out.println("✅ User added: " + user.getUsername());
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
            // Print an error message if the user could not be added
            System.out.println("❌ Error adding user!");
        }
    }

    // Method to retrieve a user by their ID
    public AdminUser getUserById(int id) {
        // Initialize the user variable as null
        AdminUser user = null;
        // SQL query to select a user by their ID
        String query = "SELECT id, username, full_name, email, phone_number, address, gender, country, date_of_birth, workplace, job_title FROM [User] WHERE id = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement ps = conn.prepareStatement(query)) {  // Prepare the SQL query
            // Set the user ID as the parameter in the query
            ps.setInt(1, id);
            // Execute the query and get the result set
            ResultSet rs = ps.executeQuery();

            // Check if a user is found
            if (rs.next()) {
                // Create a new AdminUser object with the retrieved data
                user = new AdminUser(
                        rs.getInt("id"), // User ID
                        rs.getString("username"), // Username
                        rs.getString("full_name"), // Full name
                        rs.getString("email"), // Email
                        rs.getString("phone_number"), // Phone number
                        rs.getString("address"), // Address
                        rs.getString("gender"), // Gender
                        rs.getString("country"), // Country
                        rs.getString("date_of_birth"),// Date of birth
                        rs.getString("workplace"), // Workplace
                        rs.getString("job_title") // Job title
                );
            }
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
        // Return the user object (or null if not found)
        return user;
    }

    // Method to update a user's details in the database
    public void updateUser(int id, String phoneNumber, String address, String gender, String country, String dateOfBirth, String workplace, String jobTitle) {
        // SQL query to update user details based on their ID
        String query = "UPDATE [User] SET phone_number = ?, address = ?, gender = ?, country = ?, date_of_birth = ?, workplace = ?, job_title = ? WHERE id = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement ps = conn.prepareStatement(query)) {  // Prepare the SQL query
            // Set the parameters for the updated user details
            ps.setString(1, phoneNumber);  // Phone number
            ps.setString(2, address);      // Address
            ps.setString(3, gender);       // Gender
            ps.setString(4, country);      // Country
            ps.setString(5, dateOfBirth);  // Date of birth
            ps.setString(6, workplace);    // Workplace
            ps.setString(7, jobTitle);     // Job title
            ps.setInt(8, id);              // User ID

            // Execute the update query
            ps.executeUpdate();
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
        }
    }

    // Method to delete a user and their associated bookings from the database
    public void deleteUser(int id) {
        // SQL query to delete bookings associated with the user
        String deleteBookingsQuery = "DELETE FROM Booking WHERE userId = ?";
        // SQL query to delete the user
        String deleteUserQuery = "DELETE FROM [User] WHERE id = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection()) {
            // Start a transaction to ensure data consistency
            conn.setAutoCommit(false);

            // Delete all bookings related to the user first
            try ( PreparedStatement ps1 = conn.prepareStatement(deleteBookingsQuery)) {
                // Set the user ID as the parameter in the query
                ps1.setInt(1, id);
                // Execute the delete query and get the number of deleted bookings
                int bookingsDeleted = ps1.executeUpdate();
                // Print the number of bookings deleted
                System.out.println("🗑️ Deleted " + bookingsDeleted + " bookings for user ID: " + id);
            }

            // Delete the user after deleting their bookings
            try ( PreparedStatement ps2 = conn.prepareStatement(deleteUserQuery)) {
                // Set the user ID as the parameter in the query
                ps2.setInt(1, id);
                // Execute the delete query and get the number of affected rows
                int rowsAffected = ps2.executeUpdate();

                // Check if the user was deleted successfully
                if (rowsAffected > 0) {
                    // Print a success message if the user was deleted
                    System.out.println("✅ Successfully deleted user ID: " + id);
                } else {
                    // Print an error message if the user was not found
                    System.out.println("❌ User ID not found: " + id);
                }
            }

            // Commit the transaction to confirm all deletions
            conn.commit();
        } catch (SQLException e) {
            // Print the stack trace if a database error occurs
            e.printStackTrace();
            // Print an error message if the user could not be deleted
            System.out.println("❌ Error deleting user ID: " + id);
        }
    }

    // Method to retrieve a user by their username
    public AdminUser getUserByUsername(String username) {
        // Initialize the user variable as null
        AdminUser user = null;
        // SQL query to select a user by their username (Note: Table name 'users' might be incorrect; should be '[User]' based on previous queries)
        String sql = "SELECT * FROM users WHERE username = ?";

        // Use try-with-resources to automatically close database resources
        try ( Connection conn = new DBContext().getConnection(); // Establish database connection
                  PreparedStatement stmt = conn.prepareStatement(sql)) {  // Prepare the SQL query

            // Set the username as the parameter in the query
            stmt.setString(1, username);
            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Check if a user is found
            if (rs.next()) {
                // Create a new AdminUser object with the retrieved data
                user = new AdminUser(
                        rs.getInt("id"), // User ID
                        rs.getString("username"), // Username
                        rs.getString("password"), // Password
                        rs.getString("userRole") // User role
                );
            }
        } catch (Exception e) {
            // Print the stack trace if an error occurs
            e.printStackTrace();
        }
        // Return the user object (or null if not found)
        return user;
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
                sb.append(String.format("%02x", b));  // Format each byte as a two-digit hexadecimal number
            }

            // Return the hashed string
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            // Log an error if the MD5 algorithm is not available
            Logger.getLogger(AdminUserDAO.class.getName()).log(Level.SEVERE, null, ex);
            // Return an empty string if an error occurs
            return "";
        }
    }
}
