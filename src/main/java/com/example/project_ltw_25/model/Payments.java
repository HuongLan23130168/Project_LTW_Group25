package com.example.project_ltw_25.user.model;

import java.sql.Timestamp;

public class Payments {
    private int id;
    private int order_id;
    private String payment_method;
    private double amount;
    private Timestamp payment_date;
    private String status;

    public Payments() {
    }

    public Payments(int id, int order_id, String payment_method, double amount, Timestamp payment_date, String status) {
        this.id = id;
        this.order_id = order_id;
        this.payment_method = payment_method;
        this.amount = amount;
        this.payment_date = payment_date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Timestamp payment_date) {
        this.payment_date = payment_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
