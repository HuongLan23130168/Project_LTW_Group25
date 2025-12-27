package com.example.demoweb1.dao;

import com.example.demoweb1.model.Customer;
import com.example.demoweb1.util.DBConnection; // Giả sử bạn có lớp này để kết nối DB

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> getAllCustomers(String sortBy) {
        List<Customer> list = new ArrayList<>();

        // Mặc định sắp xếp theo tài khoản mới nhất
        String orderByClause = "ORDER BY u.id DESC";
        if ("oldest".equals(sortBy)) {
            orderByClause = "ORDER BY u.id ASC"; // Sắp xếp theo tài khoản cũ nhất
        }

        // Câu lệnh SQL để lấy thông tin khách hàng và đếm tổng số đơn hàng
        String sql = """
                SELECT 
                    u.id,
                    u.full_name,
                    u.email,
                    u.phone,
                    COUNT(o.id) AS total_orders
                FROM users u
                LEFT JOIN orders o ON u.id = o.user_id
                WHERE u.role = 'user'
                GROUP BY u.id, u.full_name, u.email, u.phone
            """ + orderByClause;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            // Lặp qua từng dòng kết quả từ database
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setFullName(rs.getString("full_name"));
                c.setEmail(rs.getString("email"));
                c.setPhone(rs.getString("phone"));
                c.setTotalOrders(rs.getInt("total_orders"));
                list.add(c); // Thêm khách hàng vào danh sách
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console nếu có sự cố
        }
        return list; // Trả về danh sách khách hàng
    }
}
    