/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Represents a review submitted by a user for a tour.
 *
 * @author Admin
 */
public class Review {

    // Unique identifier for the review
    private int id;
    // ID of the user who submitted the review
    private int userId;
    // ID of the tour being reviewed
    private int tourId;
    // Rating given by the user (e.g., 1 to 5 stars)
    private int rating;
    // Comment or feedback provided by the user
    private String comment;
    // Date when the review was submitted (stored as a String)
    private String reviewDate;
    // Username of the user who submitted the review (for display purposes)
    private String username;

    // Full constructor including all fields
    public Review(int id, int userId, int tourId, int rating, String comment, String reviewDate, String username) {
        this.id = id;
        this.userId = userId;
        this.tourId = tourId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.username = username;
    }

    // Constructor without ID (used for creating a new review)
    public Review(int userId, int tourId, int rating, String comment, String reviewDate, String username) {
        this.userId = userId;
        this.tourId = tourId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.username = username;
    }

    // Getter for review ID
    public int getId() {
        return id;
    }

    // Setter for review ID
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

    // Getter for rating
    public int getRating() {
        return rating;
    }

    // Setter for rating
    public void setRating(int rating) {
        this.rating = rating;
    }

    // Getter for comment
    public String getComment() {
        return comment;
    }

    // Setter for comment
    public void setComment(String comment) {
        this.comment = comment;
    }

    // Getter for review date
    public String getReviewDate() {
        return reviewDate;
    }

    // Setter for review date
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }
}
