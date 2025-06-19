/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents a tour managed by an admin.
 *
 * @author Admin
 */
public class AdminTour {

    // Unique identifier for the tour
    private int id;
    // Name of the tour
    private String name;
    // Location or destination of the tour
    private String location;
    // Price of the tour, using BigDecimal for precise monetary calculations
    private BigDecimal price;
    // Mode of transportation used in the tour
    private String transport;
    // Description of the tour
    private String description;
    // Start date of the tour
    private Date startDate;
    // End date of the tour
    private Date endDate;
    // Type of tour (e.g., "Domestic" or "International")
    private String type;
    // URL of the tour's image
    private String imageUrl;

    // Constructor 1: Initializes a tour without specifying the type
    public AdminTour(int id, String name, String location, BigDecimal price, String transport,
            String description, Date startDate, Date endDate, String imageUrl) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.price = price;
        this.transport = transport;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
    }

    // Constructor 2: Initializes a tour with all fields, including type
    public AdminTour(int id, String name, String location, BigDecimal price, String transport,
            String description, Date startDate, Date endDate, String imageUrl, String type) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.price = price;
        this.transport = transport;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    // Getter for tour ID
    public int getId() {
        return id;
    }

    // Setter for tour ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for tour name
    public String getName() {
        return name;
    }

    // Setter for tour name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for tour location
    public String getLocation() {
        return location;
    }

    // Setter for tour location
    public void setLocation(String location) {
        this.location = location;
    }

    // Getter for tour price (using BigDecimal for precision)
    public BigDecimal getPrice() {
        return price;
    }

    // Setter for tour price (using BigDecimal for precision)
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // Getter for transportation mode
    public String getTransport() {
        return transport;
    }

    // Setter for transportation mode
    public void setTransport(String transport) {
        this.transport = transport;
    }

    // Getter for tour description
    public String getDescription() {
        return description;
    }

    // Setter for tour description
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter for tour start date
    public Date getStartDate() {
        return startDate;
    }

    // Setter for tour start date
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    // Getter for tour end date
    public Date getEndDate() {
        return endDate;
    }

    // Setter for tour end date
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // Getter for tour image URL
    public String getImageUrl() {
        return imageUrl;
    }

    // Setter for tour image URL
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Getter for tour type (e.g., "Domestic" or "International")
    public String getType() {
        return type;
    }

    // Setter for tour type
    public void setType(String type) {
        this.type = type;
    }
}
