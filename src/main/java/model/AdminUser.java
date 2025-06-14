package model;

public class AdminUser {

    // Unique identifier for the user
    private int id;
    // Username for user login
    private String username;
    // Password for user authentication
    private String password;
    // Full name of the user
    private String fullName;
    // Email address of the user
    private String email;
    // Phone number of the user
    private String phoneNumber;
    // Role of the user (e.g., "Admin", "Customer")
    private String userRole;
    // Residential address of the user
    private String address;
    // Gender of the user (e.g., "Male", "Female")
    private String gender;
    // Country of the user
    private String country;
    // Date of birth of the user (stored as String)
    private String dateOfBirth;
    // Workplace or organization of the user
    private String workplace;
    // Job title or position of the user
    private String jobTitle;
    // Preferred tour type: "Domestic" or "International"
    private String tourType;

    // Simplified constructor (used when retrieving a list of users)
    public AdminUser(int id, String username, String fullName, String email, String phoneNumber,
            String address, String gender, String country, String dateOfBirth, String workplace, String jobTitle) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.workplace = workplace;
        this.jobTitle = jobTitle;
    }

    // Full constructor with userRole (excluding tourType)
    public AdminUser(int id, String username, String password, String fullName, String email, String phoneNumber,
            String userRole, String address, String gender, String country, String dateOfBirth, String workplace, String jobTitle) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.address = address;
        this.gender = gender;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.workplace = workplace;
        this.jobTitle = jobTitle;
    }

    // Constructor without userRole (excluding tourType)
    public AdminUser(int id, String username, String password, String fullName, String email, String phoneNumber,
            String address, String gender, String country, String dateOfBirth, String workplace, String jobTitle) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.workplace = workplace;
        this.jobTitle = jobTitle;
    }

    // Constructor without ID (e.g., for creating new users)
    public AdminUser(String username, String password, String fullName, String email, String phoneNumber,
            String userRole, String address, String gender, String country, String dateOfBirth,
            String workplace, String jobTitle) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.address = address;
        this.gender = gender;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.workplace = workplace;
        this.jobTitle = jobTitle;
    }

    // Minimal constructor with essential fields (ID, username, password, userRole)
    public AdminUser(int id, String username, String password, String userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    // Getter for user ID
    public int getId() {
        return id;
    }

    // Setter for user ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for full name
    public String getFullName() {
        return fullName;
    }

    // Setter for full name
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for phone number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setter for phone number
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getter for user role
    public String getUserRole() {
        return userRole;
    }

    // Setter for user role
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    // Getter for address
    public String getAddress() {
        return address;
    }

    // Setter for address
    public void setAddress(String address) {
        this.address = address;
    }

    // Getter for gender
    public String getGender() {
        return gender;
    }

    // Setter for gender
    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getter for country
    public String getCountry() {
        return country;
    }

    // Setter for country
    public void setCountry(String country) {
        this.country = country;
    }

    // Getter for date of birth
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    // Setter for date of birth
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Getter for workplace
    public String getWorkplace() {
        return workplace;
    }

    // Setter for workplace
    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    // Getter for job title
    public String getJobTitle() {
        return jobTitle;
    }

    // Setter for job title
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    // Getter for tour type
    public String getTourType() {
        return tourType;
    }

    // Setter for tour type
    public void setTourType(String tourType) {
        this.tourType = tourType;
    }
}
