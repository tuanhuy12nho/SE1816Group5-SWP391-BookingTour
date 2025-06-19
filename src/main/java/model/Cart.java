package model;

import java.sql.Timestamp;
import java.math.BigDecimal;

public class Cart {
    private int id;
    private int userId;
    private int tourId;
    private int numberOfPeople;
    private Timestamp addedDate;
    private BigDecimal totalPrice;
    private String status;
    private String paymentMethod;
    private String tourImage;

    // Default constructor
    public Cart() {
    } // Full constructor

    public Cart(int id, int userId, int tourId, int numberOfPeople,
            Timestamp addedDate, BigDecimal totalPrice, String status, String paymentMethod, String tourImage) {
        this.id = id;
        this.userId = userId;
        this.tourId = tourId;
        this.numberOfPeople = numberOfPeople;
        this.addedDate = addedDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.tourImage = tourImage;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public Timestamp getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Timestamp addedDate) {
        this.addedDate = addedDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTourImage() {
        return tourImage;
    }

    public void setTourImage(String tourImage) {
        this.tourImage = tourImage;
    }
}
