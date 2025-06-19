/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

public class Booking {

    // Customer's full name
    private String fullName;
    // Customer's email address
    private String email;
    // Customer's phone number
    private String phone;
    // Name of the booked tour
    private String tourName;
    // Location or destination of the tour
    private String location;
    // Mode of transportation for the tour
    private String transport;
    // Status of the booking (e.g., "Confirmed", "Pending", "Cancelled")
    private String status;
    // Price of the tour per person
    private double price;
    // Start date of the tour
    private Date startDate;
    // End date of the tour
    private Date endDate;
    // Unique identifier for the booking
    private int id;
    // ID of the tour being booked
    private int tourId;
    // ID of the user who made the booking
    private int userId;
    // Gender of the customer
    private String gender;
    // Country of the customer
    private String country;
    // Payment status (e.g., "Paid", "Unpaid")
    private String pay;
    // Date when the booking was made
    private Date bookingDate;
    // Number of people included in the booking
    private int numberOfPeople;

    // Constructor for minimal booking information with user and tour IDs
    public Booking(int userId, int tourId, Date bookingDate, String status, int numberOfPeople) {
        this.userId = userId;
        this.tourId = tourId;
        this.bookingDate = bookingDate;
        this.status = status;
        this.numberOfPeople = numberOfPeople;
    }

    // Constructor with payment status included
    public Booking(int userId, int tourId, Date bookingDate, String status, int numberOfPeople, String pay) {
        this.tourId = tourId;
        this.userId = userId;
        this.bookingDate = bookingDate;
        this.status = status;
        this.numberOfPeople = numberOfPeople;
        this.pay = pay;
    }

    // Default constructor
    public Booking() {
    }

    // Full constructor without payment status
    public Booking(int id, String fullName, String email, String phone, String gender, String country,
            String tourName, String location, String transport, String status, double price,
            Date startDate, Date endDate, Date bookingDate, int numberOfPeople) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.tourName = tourName;
        this.location = location;
        this.transport = transport;
        this.status = status;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        this.gender = gender;
        this.country = country;
        this.bookingDate = bookingDate;
        this.numberOfPeople = numberOfPeople;
    }

    // Full constructor with payment status
    public Booking(int id, String fullName, String email, String phone, String gender, String country,
            String tourName, String location, String transport, String status, double price,
            Date startDate, Date endDate, Date bookingDate, int numberOfPeople, String pay) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.tourName = tourName;
        this.location = location;
        this.transport = transport;
        this.status = status;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        this.gender = gender;
        this.country = country;
        this.pay = pay;
        this.bookingDate = bookingDate;
        this.numberOfPeople = numberOfPeople;
    }

    // Constructor with booking and tour details (without customer personal info)
    public Booking(int id, Date bookingDate, String status, int numberOfPeople, String tourName, String location,
            double price, String transport, Date startDate, Date endDate) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.status = status;
        this.numberOfPeople = numberOfPeople;
        this.tourName = tourName;
        this.location = location;
        this.price = price;
        this.transport = transport;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Constructor with booking and tour details including payment status
    public Booking(int id, Date bookingDate, String status, int numberOfPeople,
            String tourName, String location, double price, String transport,
            Date startDate, Date endDate, String pay) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.status = status;
        this.numberOfPeople = numberOfPeople;
        this.tourName = tourName;
        this.location = location;
        this.price = price;
        this.transport = transport;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pay = pay;
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
    public String getPhone() {
        return phone;
    }

    // Setter for phone number
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter for tour name
    public String getTourName() {
        return tourName;
    }

    // Setter for tour name
    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    // Getter for location
    public String getLocation() {
        return location;
    }

    // Setter for location
    public void setLocation(String location) {
        this.location = location;
    }

    // Getter for transportation mode
    public String getTransport() {
        return transport;
    }

    // Setter for transportation mode
    public void setTransport(String transport) {
        this.transport = transport;
    }

    // Getter for booking status
    public String getStatus() {
        return status;
    }

    // Setter for booking status
    public void setStatus(String status) {
        this.status = status;
    }

    // Getter for price
    public double getPrice() {
        return price;
    }

    // Setter for price
    public void setPrice(double price) {
        this.price = price;
    }

    // Getter for start date
    public Date getStartDate() {
        return startDate;
    }

    // Setter for start date
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    // Getter for end date
    public Date getEndDate() {
        return endDate;
    }

    // Setter for end date
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // Getter for booking ID
    public int getId() {
        return id;
    }

    // Setter for booking ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for tour ID
    public int getTourId() {
        return tourId;
    }

    // Setter for tour ID
    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    // Getter for user ID
    public int getUserId() {
        return userId;
    }

    // Setter for user ID
    public void setUserId(int userId) {
        this.userId = userId;
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

    // Getter for booking date
    public Date getBookingDate() {
        return bookingDate;
    }

    // Setter for booking date
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    // Getter for number of people
    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    // Setter for number of people
    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    // Getter for payment status
    public String getPay() {
        return pay;
    }

    // Setter for payment status
    public void setPay(String pay) {
        this.pay = pay;
    }
}
