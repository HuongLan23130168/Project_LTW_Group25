package com.example.project_ltw_25.user.model;

public class DiscountProductType {
    private int id;
    private int discount_id;
    private int product_type_id;

    public DiscountProductType() {
    }

    public DiscountProductType(int id, int discount_id, int product_type_id) {
        this.id = id;
        this.discount_id = discount_id;
        this.product_type_id = product_type_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public int getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(int product_type_id) {
        this.product_type_id = product_type_id;
    }
}
