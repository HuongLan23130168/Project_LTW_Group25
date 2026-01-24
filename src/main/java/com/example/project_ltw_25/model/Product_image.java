package com.example.project_ltw_25.user.model;

public class Product_image {
    private int id;
    private int product_id;
    private String image_url;
        public Product_image() {
        }

        // --- Constructor có tham số cũ của bạn (Giữ nguyên) ---
        public Product_image(String image_url, int product_id, int id) {
            this.image_url = image_url;
            this.product_id = product_id;
            this.id = id;
        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

}


