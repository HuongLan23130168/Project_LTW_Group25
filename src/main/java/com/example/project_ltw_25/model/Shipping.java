package com.example.project_ltw_25.user.model;

public class Shipping {
    private int id;
    private int order_id;
    private int shipping_fee;
    private String shipping_status;
    private String tracking_number;

    public Shipping() {
    }

    public Shipping(int id, int order_id, int shipping_fee, String shipping_status, String tracking_number) {
        this.id = id;
        this.order_id = order_id;
        this.shipping_fee = shipping_fee;
        this.shipping_status = shipping_status;
        this.tracking_number = tracking_number;
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

    public int getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(int shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }

    public String getTracking_number() {
        return tracking_number;
    }

    public void setTracking_number(String tracking_number) {
        this.tracking_number = tracking_number;
    }
}
