package com.example.project_ltw_25.user.model;

public class DiscountCategories {
    private int id;
    private int discount_id;
    private int category_id;

    public DiscountCategories() {
    }

    public DiscountCategories(int id, int discount_id, int category_id) {
        this.id = id;
        this.discount_id = discount_id;
        this.category_id = category_id;
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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
