package com.example.project_ltw_25.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int variantId; // Lấy từ bảng product_variants
    private String name;
    private String image;
    private double price;
    private int quantity;
    private String color; // Từ bảng product_variants
    private String size;

    public CartItem() {
    }

    public CartItem(int variantId, String name, String image, double price, int quantity, String color, String size) {
        this.variantId = variantId;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.color = color;
        this.size = size;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    // Tính thành tiền cho mỗi item
    public double getSubTotal() {
        return price * quantity;
    }
}
