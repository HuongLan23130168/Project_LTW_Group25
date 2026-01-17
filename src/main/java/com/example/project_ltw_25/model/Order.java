package com.example.project_ltw_25.admin.model;

import java.sql.Timestamp;

public class Order {
    // --- 1. Các trường khớp với cột trong bảng Database 'orders' ---
    private int id;
    private int user_id;
    private String order_code;
    private Timestamp order_date;
    private double total_price;
    private String status;
    private String recipient_name;
    private String recipient_phone;   // Bạn cần trường này
    private String shipping_address;  // Bạn cần trường này
    private String note;              // Bạn cần trường này
    private int payment_method_id;

    // --- 2. Các trường hiển thị thêm (DTO) - Lấy từ bảng Users/Payments ---
    private String customerName;      // Tên tài khoản (từ bảng users)
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    private String paymentMethod;     // Tên phương thức thanh toán
    private double shippingFee;
    private double grandTotal;

    public Order() {
    }

    // ================= GETTERS & SETTERS (ĐẦY ĐỦ) =================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

    public String getOrder_code() { return order_code; }
    public void setOrder_code(String order_code) { this.order_code = order_code; }

    public Timestamp getOrder_date() { return order_date; }
    public void setOrder_date(Timestamp order_date) { this.order_date = order_date; }

    public double getTotal_price() { return total_price; }
    public void setTotal_price(double total_price) { this.total_price = total_price; }

    public void setStatus(String status) {
        this.status = status;
    }
    // Sửa lại dòng 49 trong file Order.java của bạn thành thế này:
    public String getStatus() {
        if (this.status == null || this.status.trim().isEmpty()) {
            return "Chờ xử lý"; // Nếu null thì trả về chữ này
        }
        return this.status;
    }
    public String getRecipient_name() { return recipient_name; }
    public void setRecipient_name(String recipient_name) { this.recipient_name = recipient_name; }

    public String getRecipient_phone() { return recipient_phone; }
    public void setRecipient_phone(String recipient_phone) { this.recipient_phone = recipient_phone; }

    public String getShipping_address() { return shipping_address; }
    public void setShipping_address(String shipping_address) { this.shipping_address = shipping_address; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public int getPayment_method_id() { return payment_method_id; }
    public void setPayment_method_id(int payment_method_id) { this.payment_method_id = payment_method_id; }

    // --- Getters cho các trường hiển thị ---
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getShippingFee() { return shippingFee; }
    public void setShippingFee(double shippingFee) { this.shippingFee = shippingFee; }

    public double getGrandTotal() { return grandTotal; }
    public void setGrandTotal(double grandTotal) { this.grandTotal = grandTotal; }
}