package com.example.project_ltw_25.user.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int detailId;       // ID dòng trong giỏ hàng (để xóa)
    private int variantId;
    private String productName;
    private String code;
    private String color;
    private String size;
    private String imageUrl;
    private double price;
    private int quantity;       // Số lượng khách mua
    private int stock;          // Số lượng tồn kho thực tế

    public CartItem() {
    }

    public CartItem(int detailId, int variantId, String productName, String code, String color, String size, String imageUrl, double price, int quantity, int stock) {
        this.detailId = detailId;
        this.variantId = variantId;
        this.productName = productName;
        this.code = code;
        this.color = color;
        this.size = size;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
        this.stock = stock;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getTotalPrice() {
        return this.price * this.quantity;
    }
}
