package com.example.project_ltw_25.admin.service;

import com.example.project_ltw_25.admin.dao.DBDAO;
import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.admin.model.OrderItem;
import com.example.project_ltw_25.admin.model.OrderStatusHistory;
import org.jdbi.v3.core.JdbiException;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderService {
    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());

    /**
     * 1. Lấy danh sách tất cả đơn hàng
     */
    public List<Order> getAllOrders(String sortBy) {
        String sortColumn = "order_date";
        String sortDirection = "DESC";

        if ("oldest".equalsIgnoreCase(sortBy)) {
            sortDirection = "ASC";
        }

        // Logic COALESCE để xử lý null status
        String sql = "SELECT o.*, " +
                "COALESCE(o.status, 'Chờ xử lý') as status, " +
                "u.full_name as customerName " +
                "FROM orders o " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "ORDER BY " + sortColumn + " " + sortDirection;

        try {
            return DBDAO.get().withHandle(handle ->
                    handle.createQuery(sql)
                            .registerRowMapper(BeanMapper.factory(Order.class))
                            .mapTo(Order.class)
                            .list()
            );
        } catch (JdbiException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách đơn hàng", e);
            return Collections.emptyList();
        }
    }

    /**
     * 2. Lấy chi tiết đơn hàng theo ID (Header)
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT o.*, " +
                "u.full_name as customerName, u.email as customerEmail, u.phone_number as customerPhone " +
                "FROM orders o " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "WHERE o.id = :id";

        try {
            return DBDAO.get().withHandle(handle ->
                    handle.createQuery(sql)
                            .bind("id", orderId)
                            .registerRowMapper(BeanMapper.factory(Order.class))
                            .mapTo(Order.class)
                            .findOne()
                            .orElse(null)
            );
        } catch (JdbiException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy chi tiết đơn hàng ID: " + orderId, e);
            return null;
        }
    }

    /**
     * 3. (MỚI) Lấy danh sách sản phẩm trong đơn hàng
     * Hàm này sửa lỗi đỏ: orderService.getOrderItemsByOrderId(id)
     */
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        // Join với bảng products để lấy tên sản phẩm
        String sql = "SELECT p.name, oi.quantity, oi.price, oi.discount, " +
                "(oi.quantity * oi.price * (1 - oi.discount/100)) as total " +
                "FROM order_items oi " +
                "LEFT JOIN products p ON oi.product_id = p.id " +
                "WHERE oi.order_id = :orderId";

        try {
            return DBDAO.get().withHandle(handle ->
                    handle.createQuery(sql)
                            .bind("orderId", orderId)
                            .registerRowMapper(BeanMapper.factory(OrderItem.class))
                            .mapTo(OrderItem.class)
                            .list()
            );
        } catch (JdbiException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy sản phẩm đơn hàng ID: " + orderId, e);
            return Collections.emptyList();
        }
    }

    /**
     * 4. (MỚI) Lấy lịch sử trạng thái đơn hàng
     * Hàm này sửa lỗi đỏ: orderService.getStatusHistoryByOrderId(id)
     */
    public List<OrderStatusHistory> getStatusHistoryByOrderId(int orderId) {
        String sql = "SELECT * FROM order_status_history WHERE order_id = :orderId ORDER BY created_at DESC";

        try {
            return DBDAO.get().withHandle(handle ->
                    handle.createQuery(sql)
                            .bind("orderId", orderId)
                            .registerRowMapper(BeanMapper.factory(OrderStatusHistory.class))
                            .mapTo(OrderStatusHistory.class)
                            .list()
            );
        } catch (JdbiException e) {
            // Nếu bảng chưa tồn tại thì trả về list rỗng chứ không crash
            LOGGER.warning("Lỗi lấy lịch sử (có thể chưa tạo bảng order_status_history): " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 5. Cập nhật trạng thái đơn hàng
     */
    public boolean updateOrderStatus(int orderId, String newStatus) {
        try {
            return DBDAO.get().inTransaction(handle -> {
                // Update bảng orders
                int updated = handle.createUpdate("UPDATE orders SET status = :status WHERE id = :id")
                        .bind("status", newStatus)
                        .bind("id", orderId)
                        .execute();

                if (updated > 0) {
                    // Thêm vào lịch sử
                    try {
                        handle.createUpdate("INSERT INTO order_status_history (order_id, status, created_at) " +
                                        "VALUES (:id, :status, NOW())")
                                .bind("id", orderId)
                                .bind("status", newStatus)
                                .execute();
                    } catch (Exception ex) {
                        LOGGER.warning("Không thể ghi log lịch sử: " + ex.getMessage());
                    }
                    return true;
                }
                return false;
            });
        } catch (JdbiException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật trạng thái ID: " + orderId, e);
            return false;
        }
    }
}