package com.example.project_ltw_25.user.model;

public class Inventories {
    private int id;
    private int variant_id;
    private int stock_quantity;

    public Inventories() {
    }

    public Inventories(int id, int variant_id, int stock_quantity) {
        this.id = id;
        this.variant_id = variant_id;
        this.stock_quantity = stock_quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(int variant_id) {
        this.variant_id = variant_id;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }
}
