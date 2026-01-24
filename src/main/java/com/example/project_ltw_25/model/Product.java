package com.example.project_ltw_25.user.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {

    // ===== FIELD TỪ DATABASE =====
    private int id;
    private String product_code;
    private String product_name;
    private String category_id;
    private int product_type_id;
    private String description;
    private Timestamp created_at;

    // ===== FIELD TÍNH TOÁN / HIỂN THỊ =====
    private double price; // Giá gốc
    private double price_new; // Giá sau khi giảm
    private String type_name;
    private String category_name; // Sửa từ category_name_display
    private int stock;
    private double discountPercent;
    private String image_url;

    private boolean newProduct;
    private boolean bestSeller;

    // ===== RELATION =====
    private List<Product_variant> variants = new ArrayList<>();
    private List<Product_image> images = new ArrayList<>();

    public Product() {
        this.price = 0.0;
        this.price_new = 0.0;
    }

    // ================= GETTER / SETTER =================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getProduct_code() { return product_code; }
    public void setProduct_code(String product_code) { this.product_code = product_code; }

    public String getProduct_name() { return product_name; }
    public void setProduct_name(String product_name) { this.product_name = product_name; }

    public String getCategory_id() { return category_id; }
    public void setCategory_id(String category_id) { this.category_id = category_id; }

    public int getProduct_type_id() { return product_type_id; }
    public void setProduct_type_id(int product_type_id) { this.product_type_id = product_type_id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getPrice_new() { return price_new; }
    public void setPrice_new(double price_new) { this.price_new = price_new; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }

    public String getType_name() { return type_name; }
    public void setType_name(String type_name) { this.type_name = type_name; }

    public String getCategory_name() { return category_name; }
    public void setCategory_name(String category_name) { this.category_name = category_name; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }

    public boolean isNewProduct() { return newProduct; }
    public void setNewProduct(boolean newProduct) { this.newProduct = newProduct; }

    public boolean isBestSeller() { return bestSeller; }
    public void setBestSeller(boolean bestSeller) { this.bestSeller = bestSeller; }



    public List<Product_variant> getVariants() { return variants; }
    public void setVariants(List<Product_variant> variants) { this.variants = variants; }

    public List<Product_image> getImages() { return images; }
    public void setImages(List<Product_image> images) { this.images = images; }
}