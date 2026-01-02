package com.example.project_ltw_25.model;

import java.util.List;

public class Product {
    private int id;
    private String product_code;
    private String product_name;
    private int product_type_id;
    private int category_id;
    private String description;
    private List<Product_variant> variants;
    private List<Product_image> images; // Thêm danh sách hình ảnh

    public Product() {
    }

    // ... các getter/setter cũ ...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(int product_type_id) {
        this.product_type_id = product_type_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product_variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Product_variant> variants) {
        this.variants = variants;
    }

    // Getter và Setter cho images
    public List<Product_image> getImages() {
        return images;
    }

    public void setImages(List<Product_image> images) {
        this.images = images;
    }
}
