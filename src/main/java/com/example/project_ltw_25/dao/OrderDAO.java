// Re-saving file to remove BOM
package com.example.project_ltw_25.admin.dao;

import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.admin.model.OrderItem;
import com.example.project_ltw_25.admin.model.OrderStatusHistory;
import com.example.project_ltw_25.admin.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public List<Order> getAllOrders(String sortBy) {
        List<Order> orderList = new ArrayList<>();
        String orderBy;
        if ("oldest".equals(sortBy)) {
            orderBy = "o.order_date ASC";
        } else {
            orderBy = "o.order_date DESC";
        }
        
        String sql = "SELECT o.*, (SELECT h.status FROM order_status_history h WHERE h.order_id = o.id ORDER BY h.created_at DESC LIMIT 1) AS current_status FROM orders o ORDER BY " + orderBy;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUser_id(rs.getInt("user_id"));
                order.setOrder_code(rs.getString("order_code"));
                order.setOrder_date(rs.getTimestamp("order_date"));
                order.setTotal_price(rs.getDouble("total_price"));
                String status = rs.getString("current_status");
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
    
    public List<Order> getOrdersByCustomerId(int userId) {
        List<Order> orderList = new ArrayList<>();
        String sql =
                "SELECT o.*, " +
                        "(SELECT h.status " +
                        "FROM order_status_history h " +
                        "WHERE h.order_id = o.id " +
                        "ORDER BY h.created_at DESC LIMIT 1) " +
                        "AS current_status " +
                        "FROM orders o " +
                        "WHERE o.user_id = ? " +
                        "ORDER BY o.order_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUser_id(rs.getInt("user_id"));
                order.setOrder_code(rs.getString("order_code"));
                order.setOrder_date(rs.getTimestamp("order_date"));
                order.setTotal_price(rs.getDouble("total_price"));
                String status = rs.getString("current_status");
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

    public Order getOrderById(int orderId) {
        Order order = null;
        String sql = "SELECT * FROM orders WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                order = new Order();
                order.setId(rs.getInt("id"));
                order.setUser_id(rs.getInt("user_id"));
                order.setOrder_code(rs.getString("order_code"));
                order.setOrder_date(rs.getTimestamp("order_date"));
                order.setTotal_price(rs.getDouble("total_price"));
                order.setRecipient_name(rs.getString("recipient_name"));
                order.setRecipient_phone(rs.getString("recipient_phone"));
                order.setShipping_address(rs.getString("shipping_address"));
                order.setNote(rs.getString("note"));
                order.setPayment_method_id(rs.getInt("payment_method_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT p.name, oi.quantity, oi.price, oi.discount, (oi.quantity * oi.price * (1 - oi.discount/100)) as total " +
                     "FROM order_items oi " +
                     "LEFT JOIN products p ON oi.product_id = p.id " +
                     "WHERE oi.order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();
                String productName = rs.getString("name");
                item.setName(productName != null ? productName : "Sản phẩm không tồn tại");
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                item.setDiscount(rs.getDouble("discount"));
                item.setTotal(rs.getDouble("total"));
                orderItems.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    public List<OrderStatusHistory> getStatusHistoryByOrderId(int orderId) {
        List<OrderStatusHistory> statusHistory = new ArrayList<>();
        String sql = "SELECT * FROM order_status_history WHERE order_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderStatusHistory history = new OrderStatusHistory();
                history.setId(rs.getInt("id"));
                history.setOrderId(rs.getInt("order_id"));
                history.setStatus(rs.getString("status"));
                history.setCreatedAt(rs.getTimestamp("created_at"));
                statusHistory.add(history);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHistory;
    }
    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        String historySql = "INSERT INTO order_status_history (order_id, status, created_at) VALUES (?, ?, NOW())";

        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement psHistory = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            ps = conn.prepareStatement(sql);
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                try {
                    psHistory = conn.prepareStatement(historySql);
                    psHistory.setInt(1, orderId);
                    psHistory.setString(2, newStatus);
                    psHistory.executeUpdate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                conn.commit(); 
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (psHistory != null) psHistory.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

}

