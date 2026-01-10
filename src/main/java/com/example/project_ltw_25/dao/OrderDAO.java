package com.example.project_ltw_25.admin.dao;

import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.admin.model.OrderItem;
import com.example.project_ltw_25.admin.model.OrderStatusHistory;
import com.example.project_ltw_25.admin.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                try {
                    order.setOrder_code(rs.getString("order_code"));
                } catch (Exception e) {
                    // Ignore if column does not exist
                }
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
                try {
                    order.setOrder_code(rs.getString("order_code"));
                } catch (Exception e) {
                    // Ignore if column does not exist
                }
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
        String sql = "SELECT o.*, u.fullName, u.email, u.phone, pm.name AS paymentMethod, (SELECT h.status FROM order_status_history h WHERE h.order_id = o.id ORDER BY h.created_at DESC LIMIT 1) AS current_status " +
                     "FROM orders o " +
                     "LEFT JOIN users u ON o.user_id = u.id " +
                     "LEFT JOIN payment_methods pm ON o.payment_method_id = pm.id " +
                     "WHERE o.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                order = new Order();
                order.setId(rs.getInt("id"));
                order.setOrder_code(rs.getString("order_code"));
                order.setOrder_date(rs.getTimestamp("order_date"));
                String status = rs.getString("current_status");
                order.setStatus(status != null ? status : "Chờ xử lý");
                order.setCustomerName(rs.getString("fullName"));
                order.setCustomerEmail(rs.getString("email"));
                order.setCustomerPhone(rs.getString("phone"));
                order.setCustomerAddress(rs.getString("shipping_address"));
                order.setPaymentMethod(rs.getString("paymentMethod"));
                order.setShippingFee(rs.getDouble("shipping_fee"));
                order.setGrandTotal(rs.getDouble("total_price"));
            }
        } catch (Exception e) {
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
}
