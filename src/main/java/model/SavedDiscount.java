/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author criss
 */
public class SavedDiscount {
    private int id, user_id, discount_id;
    private LocalDateTime saved_at;

    public SavedDiscount() {
    }

    public SavedDiscount(int id, int user_id, int discount_id, LocalDateTime saved_at) {
        this.id = id;
        this.user_id = user_id;
        this.discount_id = discount_id;
        this.saved_at = saved_at;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public LocalDateTime getSaved_at() {
        return saved_at;
    }

    public void setSaved_at(LocalDateTime saved_at) {
        this.saved_at = saved_at;
    }

    @Override
    public String toString() {
        return "SavedDiscount{" + "id=" + id + ", user_id=" + user_id + ", discount_id=" + discount_id + ", saved_at=" + saved_at + '}';
    }
    
    
}
