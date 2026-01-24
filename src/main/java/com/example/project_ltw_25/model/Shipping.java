package com.example.project_ltw_25.user.model;

public class Shipping {
    private int id;
    private int orderId;
    private String shippingType;
    private int shippingFee;
    private String shippingStatus;
    private String trackingNumber;

    public Shipping() {
    }

    public Shipping(int id, int orderId, String shippingType, int shippingFee, String shippingStatus, String trackingNumber) {
        this.id = id;
        this.orderId = orderId;
        this.shippingType = shippingType;
        this.shippingFee = shippingFee;
        this.shippingStatus = shippingStatus;
        this.trackingNumber = trackingNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}
