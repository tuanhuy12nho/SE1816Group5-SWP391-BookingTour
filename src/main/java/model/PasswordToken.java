/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 * Model class representing a password reset token
 * 
 * @author ACER
 */
public class PasswordToken {
    private int id;
    private int userId;
    private String token;
    private Timestamp expiredAt;
    private boolean isUsed;

   
    public PasswordToken() {
    }

    
    public PasswordToken(int userId, String token, Timestamp expiredAt) {
        this.userId = userId;
        this.token = token;
        this.expiredAt = expiredAt;
        this.isUsed = false;
    }

   
    public PasswordToken(int id, int userId, String token, Timestamp expiredAt, boolean isUsed) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.expiredAt = expiredAt;
        this.isUsed = isUsed;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Timestamp expiredAt) {
        this.expiredAt = expiredAt;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    // Check if token is expired
    public boolean isExpired() {
        return System.currentTimeMillis() > expiredAt.getTime();
    }

    // Check if token is valid (not used and not expired)
    public boolean isValid() {
        return !isUsed && !isExpired();
    }

    @Override
    public String toString() {
        return "PasswordToken{" +
                "id=" + id +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", expiredAt=" + expiredAt +
                ", isUsed=" + isUsed +
                '}';
    }
}
