/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class Favorite {
    private int id, userId, tourId;
    
    private Tour tour;

    public Favorite() {
    }

    public Favorite(int id, int userId, Tour tour) {
        this.id = id;
        this.userId = userId;
        this.tour = tour;
    }

    public Favorite(int id, int userId, int tourId) {
        this.id = id;
        this.userId = userId;
        this.tourId = tourId;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

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

    @Override
    public String toString() {
        return "Favorite{" + "id=" + id + ", userId=" + userId + ", tourId=" + tourId + '}';
    }
}
