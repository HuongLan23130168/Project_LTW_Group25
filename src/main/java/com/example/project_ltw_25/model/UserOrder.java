package com.example.project_ltw_25.user.model;

import java.sql.Timestamp;

public class UserOrder {
    // Thông tin chung đơn hàng
    private int id;
    private String orderCode;      // map cột order_code
    private Timestamp orderDate;   // map cột order_date
    private double totalPrice;     // map cột total_price

    // Thông tin xử lý (Lấy từ bảng history)
    private String status;

    // Thông tin sản phẩm đại diện (Lấy từ bảng variants/products)
    private String productName;
    private String imageUrl;
    private String color;
    private String size;
    private int quantity;
    private int otherItemsCount;   // Số sản phẩm còn lại

    public UserOrder() {}

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getOrderCode() { return orderCode; }
    public void setOrderCode(String orderCode) { this.orderCode = orderCode; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    // Nếu status chưa có (đơn mới tạo chưa có history), mặc định là Chờ xử lý
    public String getStatus() { return status != null ? status : "Chờ xử lý"; }
    public void setStatus(String status) { this.status = status; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getOtherItemsCount() { return otherItemsCount; }
    public void setOtherItemsCount(int otherItemsCount) { this.otherItemsCount = otherItemsCount; }
}