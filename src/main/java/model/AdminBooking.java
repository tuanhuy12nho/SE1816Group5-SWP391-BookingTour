/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

public class AdminBooking {

    // Unique identifier for the booking
    private int id;
    // ID of the user who made the booking
    private int userId;
    // ID of the tour being booked
    private int tourId;
    // Date when the booking was made
    private Date bookingDate;
    // Name of the customer making the booking
    private String customerName;
    // Customer's phone number
    private String phone;
    // Customer's email address
    private String email;
    // Name of the tour
    private String tourName;
    // Destination of the tour
    private String destination;
    // Price per person for the tour
    private double price;
    // Number of people in the booking
    private int numberOfPeople;
    // Total amount for the booking (price * numberOfPeople)
    private double totalAmount;
    // Status of payment (e.g., "Paid", "Pending")
    private String paymentStatus;
    // Status of the booking (e.g., "Confirmed", "Cancelled")
    private String status;
    // Departure date of the tour
    private Date departure;
    // End date of the tour
    private Date endDate;
    // Type of tour (e.g., "Domestic" or "International")
    private String tourType;
    // Mode of transportation for the tour
    private String transport;

    // Default constructor
    public AdminBooking() {
    }

    // Parameterized constructor with most fields
    public AdminBooking(int id, int userId, int tourId, Date bookingDate,
            String customerName, String phone, String email,
            String tourName, String destination, double price,
            int numberOfPeople, String paymentStatus, String status,
            Date departure, Date endDate, String tourType) {
        this.id = id;
        this.userId = userId;
        this.tourId = tourId;
        this.bookingDate = bookingDate;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.tourName = tourName;
        this.destination = destination;
        this.price = price;
        this.numberOfPeople = numberOfPeople;
        this.paymentStatus = paymentStatus;
        this.status = status;
        this.departure = departure;
        this.endDate = endDate;
        this.tourType = tourType;
        // Calculate total amount based on price and number of people
        this.totalAmount = price * numberOfPeople;
    }

    // Constructor with only transport parameter
    public AdminBooking(String transport) {
        this.transport = transport;
    }

    // Getter for booking ID
    public int getId() {
        return id;
    }

    // Setter for booking ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for user ID
    public int getUserId() {
        return userId;
    }

    // Setter for user ID
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter for tour ID
    public int getTourId() {
        return tourId;
    }

    // Setter for tour ID
    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    // Getter for booking date
    public Date getBookingDate() {
        return bookingDate;
    }

    // Setter for booking date
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    // Getter for customer name
    public String getCustomerName() {
        return customerName;
    }

    // Setter for customer name
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Getter for phone number
    public String getPhone() {
        return phone;
    }

    // Setter for phone number
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for tour name
    public String getTourName() {
        return tourName;
    }

    // Setter for tour name
    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    // Getter for destination
    public String getDestination() {
        return destination;
    }

    // Setter for destination
    public void setDestination(String destination) {
        this.destination = destination;
    }

    // Getter for price per person
    public double getPrice() {
        return price;
    }

    // Setter for price, updates total amount when changed
    public void setPrice(double price) {
        this.price = price;
        // Recalculate total amount when price changes
        this.totalAmount = price * this.numberOfPeople;
    }

    // Getter for number of people
    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    // Setter for number of people, updates total amount when changed
    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
        // Recalculate total amount when number of people changes
        this.totalAmount = this.price * numberOfPeople;
    }

    // Getter for total amount
    public double getTotalAmount() {
        return totalAmount;
    }

    // Setter for total amount
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Getter for payment status
    public String getPaymentStatus() {
        return paymentStatus;
    }

    // Setter for payment status
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Getter for booking status
    public String getStatus() {
        return status;
    }

    // Setter for booking status
    public void setStatus(String status) {
        this.status = status;
    }

    // Getter for departure date
    public Date getDeparture() {
        return departure;
    }

    // Setter for departure date
    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    // Getter for end date
    public Date getEndDate() {
        return endDate;
    }

    // Setter for end date
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // Getter for tour type
    public String getTourType() {
        return tourType;
    }

    // Setter for tour type
    public void setTourType(String tourType) {
        this.tourType = tourType;
    }

    // Getter for transport
    public String getTransport() {
        return transport;
    }

    // Setter for transport
    public void setTransport(String transport) {
        this.transport = transport;
    }

    // Override toString method to provide a string representation of the object
    @Override
    public String toString() {
        return "Booking{"
                + "id=" + id
                + ", userId=" + userId
                + ", tourId=" + tourId
                + ", bookingDate=" + bookingDate
                + ", customerName='" + customerName + '\''
                + ", phone='" + phone + '\''
                + ", email='" + email + '\''
                + ", tourName='" + tourName + '\''
                + ", destination='" + destination + '\''
                + ", price=" + price
                + ", numberOfPeople=" + numberOfPeople
                + ", totalAmount=" + totalAmount
                + ", paymentStatus='" + paymentStatus + '\''
                + ", status='" + status + '\''
                + ", departure=" + departure
                + ", endDate=" + endDate
                + ", tourType='" + tourType + '\''
                + ", transport='" + transport + '\''
                + '}';
    }
}
