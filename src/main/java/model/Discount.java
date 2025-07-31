/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author admin
 */
public class Discount {
    private int id, max_usage, usage_count;
    private String code, description;
    private float discount_percent;
    private LocalDateTime start_date;
    private LocalDateTime end_date;

    public Discount() {
    }
           
    public Discount(String code, int max_usage, String description, float discount_percent,
            LocalDateTime start_date, LocalDateTime end_date) {
        this.code = code;
        this.max_usage = max_usage;
        this.description = description;
        this.discount_percent = discount_percent;
        this.start_date = start_date;
        this.end_date = end_date;
    }
        
    public Discount(int id, int max_usage, int usage_count, 
            String code, String description, float discount_percent,
            LocalDateTime start_date, LocalDateTime end_date) {
        this.id = id;
        this.max_usage = max_usage;
        this.usage_count = usage_count;
        this.code = code;
        this.description = description;
        this.discount_percent = discount_percent;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public Discount(int id, int max_usage, String description, float discount_percent, LocalDateTime start_date, LocalDateTime end_date) {
        this.id = id;
        this.max_usage = max_usage;
        this.description = description;
        this.discount_percent = discount_percent;
        this.start_date = start_date;
        this.end_date = end_date;
    }
    
    
    public int getId() {
        return id;
    }

    public int getMax_usage() {
        return max_usage;
    }

    public int getUsage_count() {
        return usage_count;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public float getDiscount_percent() {
        return discount_percent;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }

 
    public void setId(int id) {
        this.id = id;
    }

    public void setMax_usage(int max_usage) {
        this.max_usage = max_usage;
    }

    public void setUsage_count(int usage_count) {
        this.usage_count = usage_count;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDiscount_percent(float discount_percent) {
        this.discount_percent = discount_percent;
    }
    @Override
    public String toString() {
        return "Discount{" + "id=" + id + ", max_usage=" + max_usage + ", usage_count=" + usage_count + ", code=" + code + ", description=" + description + ", discount_percent=" + discount_percent + ", start_date=" + start_date + ", end_date=" + end_date + '}';
    }
    
}