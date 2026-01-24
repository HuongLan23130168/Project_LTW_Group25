package com.example.project_ltw_25.user.model;

import java.math.BigDecimal;

public class Product_variant {
    private int id;
    private String variant_code;
    private int product_id;
    private String style;
    private String color;
    private String size;
    private String material;
    private BigDecimal price; // Sử dụng BigDecimal cho tiền tệ
    private int stock_quantity;

    public Product_variant() {
    }

    // Constructor đầy đủ
    public Product_variant(int id, String variant_code, int product_id, String style, String color, String size, String material, BigDecimal price, int stock_quantity) {
        this.id = id;
        this.variant_code = variant_code;
        this.product_id = product_id;
        this.style = style;
        this.color = color;
        this.size = size;
        this.material = material;
        this.price = price;
        this.stock_quantity = stock_quantity;
    }

    // --- GETTER & SETTER ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getVariant_code() { return variant_code; }
    public void setVariant_code(String variant_code) { this.variant_code = variant_code; }

    public int getProduct_id() { return product_id; }
    public void setProduct_id(int product_id) { this.product_id = product_id; }

    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStock_quantity() { return stock_quantity; }
    public void setStock_quantity(int stock_quantity) { this.stock_quantity = stock_quantity; }
}