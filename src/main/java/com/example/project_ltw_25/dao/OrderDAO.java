package com.example.demoweb1.dao;

import com.example.demoweb1.model.Order;
import com.example.demoweb1.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public List<Order> getOrdersByCustomerId(int userId) {
        List<Order> orderList = new ArrayList<>();

        // CÂU SQL MỚI:
        // Lấy tất cả thông tin từ bảng orders (o)
        // Và lấy field 'status' từ bảng order_status_history (h) bằng cách chọn dòng mới nhất (ORDER BY created_at DESC LIMIT 1)
        String sql = """
            SELECT 
                o.*,
                (SELECT h.status 
                 FROM order_status_history h 
                 WHERE h.order_id = o.id 
                 ORDER BY h.created_at DESC 
                 LIMIT 1) AS current_status
            FROM orders o
            WHERE o.user_id = ?
            ORDER BY o.order_date DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUser_id(rs.getInt("user_id"));

                // Cập nhật lấy order_code (nếu bảng orders có cột này)
                // Nếu bảng orders chưa có cột order_code thì bạn cần thêm vào DB hoặc bỏ dòng này
                try {
                    order.setOrder_code(rs.getString("order_code"));
                } catch (Exception e) {
                    // Bỏ qua nếu cột không tồn tại
                }

                order.setOrder_date(rs.getTimestamp("order_date"));
                order.setTotal_price(rs.getDouble("total_price"));

                // QUAN TRỌNG: Lấy status từ alias 'current_status' chúng ta đã đặt trong câu SQL
                String status = rs.getString("current_status");
                // Nếu chưa có lịch sử nào, gán mặc định là "Chờ xử lý" hoặc giá trị tùy ý
                order.setStatus(status != null ? status : "Chờ xử lý");

                order.setRecipient_name(rs.getString("recipient_name"));
                order.setRecipient_phone(rs.getString("recipient_phone"));
                order.setShipping_address(rs.getString("shipping_address"));
                order.setNote(rs.getString("note"));

                orderList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }
}