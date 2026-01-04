package com.example.demoweb1.model;

import java.sql.Timestamp;

public class Customer {
    private int id;
    private String full_name;
    private String email;
    private String phone;
    private int total_orders;
    private String created_at;
    private String gender;
    private String birth;
    private String role;
    private String address;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFull_name() { return full_name; }
    public void setFull_name(String full_name) { this.full_name = full_name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getTotal_orders() { return total_orders; }
    public void setTotal_orders(int total_orders) { this.total_orders = total_orders; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String  created_at) { this.created_at = created_at; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirth() { return birth; }
    public void setBirth(String birth) { this.birth = birth; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
