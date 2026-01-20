package com.example.project_ltw_25.user.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Discount {
    private int id;
    private String discount_code;
    private String discount_name;
    private BigDecimal discount_percent;
    private Timestamp start_date;
    private Timestamp end_date;

    public Discount() {
    }

    public Discount(int id, String discount_code, String discount_name, BigDecimal discount_percent, Timestamp start_date, Timestamp end_date) {
        this.id = id;
        this.discount_code = discount_code;
        this.discount_name = discount_name;
        this.discount_percent = discount_percent;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscount_code() {
        return discount_code;
    }

    public void setDiscount_code(String discount_code) {
        this.discount_code = discount_code;
    }

    public String getDiscount_name() {
        return discount_name;
    }

    public void setDiscount_name(String discount_name) {
        this.discount_name = discount_name;
    }

    public BigDecimal getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(BigDecimal discount_percent) {
        this.discount_percent = discount_percent;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    public Timestamp getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Timestamp end_date) {
        this.end_date = end_date;
    }

    public boolean isActive() {
        long now = System.currentTimeMillis();
        return start_date != null && end_date != null &&
                now >= start_date.getTime() && now <= end_date.getTime();
    }
}
