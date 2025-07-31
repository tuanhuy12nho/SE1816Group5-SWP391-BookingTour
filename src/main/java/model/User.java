/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 * Represents a user in the system with their personal and account details.
 *
 * @author Admin
 */
public class User {

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
    // Gender of the user (e.g., "Male", "Female")
    private String gender;
    // Residential address of the user
    private String address;
    // Country of the user
    private String country;
    // Date of birth of the user
    private Date date_of_birth;
    // Workplace or organization of the user
    private String workplace;
    // Job title or position of the user
    private String job_title;
    // Role of the user (e.g., "Admin", "Customer")
    private String userRole;

    // Default constructor
    public User() {
    }

    // Constructor with basic user details and role
    public User(int id, String username, String password, String fullName, String email, String phoneNumber, String userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }

    // Constructor without ID (e.g., for creating a new user)
    public User(String username, String password, String fullName, String email, String phoneNumber, String userRole) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }

    // Minimal constructor for authentication purposes
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Full constructor with all fields
    public User(int id, String username, String password, String fullName, String email, String phoneNumber, String gender, String address, String country, Date date_of_birth, String workplace, String job_title, String userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.address = address;
        this.country = country;
        this.date_of_birth = date_of_birth;
        this.workplace = workplace;
        this.job_title = job_title;
        this.userRole = userRole;
    }

    // Alternative full constructor with slightly different parameter naming
    public User(int id, String username, String password, String full_name, String email,
            String phone_number, String user_role, String address, String gender,
            String country, Date date_of_birth, String workplace, String job_title) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = full_name;
        this.email = email;
        this.phoneNumber = phone_number;
        this.userRole = user_role;
        this.address = address;
        this.gender = gender;
        this.country = country;
        this.date_of_birth = date_of_birth;
        this.workplace = workplace;
        this.job_title = job_title;
    }

    // Constructor with minimal contact information
    public User(int id, String username, String email, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
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

    // Getter for gender
    public String getGender() {
        return gender;
    }

    // Getter for address
    public String getAddress() {
        return address;
    }

    // Getter for country
    public String getCountry() {
        return country;
    }

    // Getter for date of birth
    public Date getDate_of_birth() {
        return date_of_birth;
    }

    // Getter for workplace
    public String getWorkplace() {
        return workplace;
    }

    // Getter for job title
    public String getJob_title() {
        return job_title;
    }

    // Setter for gender
    public void setGender(String gender) {
        this.gender = gender;
    }

    // Setter for address
    public void setAddress(String address) {
        this.address = address;
    }

    // Setter for country
    public void setCountry(String country) {
        this.country = country;
    }

    // Setter for date of birth
    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    // Setter for workplace
    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    // Setter for job title
    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }
}
