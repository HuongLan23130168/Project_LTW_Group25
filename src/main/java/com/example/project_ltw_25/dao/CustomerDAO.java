package com.example.demoweb1.dao;

import com.example.demoweb1.model.Customer;
import com.example.demoweb1.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> getAllCustomers(String sortBy) {
        List<Customer> list = new ArrayList<>();
        String orderByClause = "ORDER BY u.id DESC";
        if ("oldest".equals(sortBy)) {
            orderByClause = "ORDER BY u.id ASC";
        }

        String sql = """
            SELECT 
                u.id,
                u.full_name,
                u.email,
                u.phone,
                u.created_at,
                COUNT(o.id) AS total_orders
            FROM users u
            LEFT JOIN orders o ON u.id = o.user_id
            WHERE u.role = 'user'
            GROUP BY u.id, u.full_name, u.email, u.phone, u.created_at
        """ + orderByClause;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setFull_name(rs.getString("full_name"));
                c.setEmail(rs.getString("email"));
                c.setPhone(rs.getString("phone"));
                c.setTotal_orders(rs.getInt("total_orders"));
                c.setCreated_at(rs.getString("created_at"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Customer getCustomerById(int id) {
        String sql = "SELECT id, full_name, email, phone, gender, birth, address, role FROM users WHERE id = ? AND role = 'user'";
        Customer c = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = new Customer();
                c.setId(rs.getInt("id"));
                c.setFull_name(rs.getString("full_name"));
                c.setEmail(rs.getString("email"));
                c.setPhone(rs.getString("phone"));
                c.setGender(rs.getString("gender"));
                c.setBirth(rs.getString("birth"));
                c.setAddress(rs.getString("address"));
                c.setRole(rs.getString("role"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
}
