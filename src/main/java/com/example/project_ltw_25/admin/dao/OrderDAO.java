package com.example.project_ltw_25.admin.dao;

import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.admin.model.OrderItem;
import com.example.project_ltw_25.user.dao.DBDAO;
import com.example.project_ltw_25.user.model.CartItem;
import com.example.project_ltw_25.user.model.Shipping;
import com.example.project_ltw_25.user.model.UserOrder;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class OrderDAO {
    private final Jdbi jdbi = DBDAO.get();

    // ==========================================
    // 1. TẠO ĐƠN HÀNG (TRANSACTION)
    // ==========================================
    public String createOrder(int userId, String name, String phone, String address, String note,
                              int paymentId, List<CartItem> items, double total,
                              String shippingType, double shippingFee) {
        try {
            return jdbi.inTransaction(handle -> {
                // 1. Tạo Order Code
                String orderCode = "NLT@" + (System.currentTimeMillis() % 100000);

                // 2. Chèn vào bảng orders (Không có cột status)
                int orderId = handle.createUpdate("INSERT INTO orders (order_code, user_id, recipient_name, recipient_phone, total_price, note, shipping_address, order_date) "
                                + "VALUES (:code, :uid, :name, :phone, :total, :note, :addr, NOW())")
                        .bind("code", orderCode)
                        .bind("uid", userId)
                        .bind("name", name)
                        .bind("phone", phone)
                        .bind("total", total + shippingFee) // Tổng tiền bao gồm ship
                        .bind("note", note)
                        .bind("addr", address)
                        .executeAndReturnGeneratedKeys()
                        .mapTo(Integer.class)
                        .one();

                // [MỚI] 2.1. Ghi trạng thái khởi tạo vào lịch sử ngay lập tức
                handle.createUpdate("INSERT INTO order_status_history (order_id, status, created_at) VALUES (:oid, 'Chờ xử lý', NOW())")
                        .bind("oid", orderId)
                        .execute();

                // 3. Batch insert chi tiết đơn hàng và Batch update trừ kho
                var detailBatch = handle.prepareBatch("INSERT INTO order_details (order_id, variant_id, quantity, unit_price) VALUES (?, ?, ?, ?)");
                var stockBatch = handle.prepareBatch("UPDATE inventories SET stock_quantity = stock_quantity - ? WHERE variant_id = ? AND stock_quantity >= ?");

                for (CartItem item : items) {
                    detailBatch.add(orderId, item.getVariantId(), item.getQuantity(), item.getPrice());
                    stockBatch.add(item.getQuantity(), item.getVariantId(), item.getQuantity());
                }

                detailBatch.execute();
                int[] updateCounts = stockBatch.execute();

                // Kiểm tra xem có sản phẩm nào bị thiếu kho không
                for (int count : updateCounts) {
                    if (count == 0) {
                        throw new RuntimeException("Sản phẩm đã hết hàng hoặc không đủ số lượng trong kho!");
                    }
                }

                // 4. Chèn thông tin vận chuyển
                handle.createUpdate("INSERT INTO shipping (order_id, shipping_type, shipping_fee, shipping_status) VALUES (:oid, :stype, :sfee, 'Chờ lấy hàng')")
                        .bind("oid", orderId)
                        .bind("stype", shippingType)
                        .bind("sfee", shippingFee)
                        .execute();

                // 5. Chèn thông tin thanh toán
                handle.createUpdate("INSERT INTO payments (order_id, payment_method, status) VALUES (:oid, :method, 'Chưa thanh toán')")
                        .bind("oid", orderId)
                        .bind("method", (paymentId == 1 ? "COD" : "Chuyển khoản"))
                        .execute();

                return orderCode;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ==========================================
    // 2. LẤY CHI TIẾT ĐƠN HÀNG (THEO MÃ HOẶC ID)
    // ==========================================

    // Lấy theo Order Code (Chi tiết đầy đủ kèm User, Shipping, Payment)
    // [ĐÃ SỬA]: Thêm sub-query lấy status từ lịch sử
    public Order getOrderByCode(String code) {
        String sql = """
            SELECT o.*, 
                   COALESCE(
                       (SELECT status 
                        FROM order_status_history h 
                        WHERE h.order_id = o.id 
                        ORDER BY h.created_at DESC 
                        LIMIT 1), 
                   'Chờ xử lý') AS status,
                   u.email as customerEmail, 
                   s.shipping_type, s.shipping_fee, s.shipping_status, s.tracking_number, 
                   p.payment_method as paymentMethodName 
            FROM orders o 
            LEFT JOIN users u ON o.user_id = u.id 
            LEFT JOIN shipping s ON o.id = s.order_id 
            LEFT JOIN payments p ON o.id = p.order_id 
            WHERE o.order_code = :code
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("code", code)
                        .map((rs, ctx) -> {
                            Order order = new Order();
                            // Set thông tin cơ bản
                            order.setId(rs.getInt("id"));
                            order.setOrderCode(rs.getString("order_code"));
                            order.setOrderDate(rs.getTimestamp("order_date"));
                            order.setTotalPrice(rs.getDouble("total_price"));
                            order.setRecipientName(rs.getString("recipient_name"));
                            order.setRecipientPhone(rs.getString("recipient_phone"));
                            order.setCustomerEmail(rs.getString("customerEmail"));
                            order.setShippingAddress(rs.getString("shipping_address"));
                            order.setNote(rs.getString("note"));

                            // [MỚI] Set Status lấy từ sub-query
                            order.setStatus(rs.getString("status"));

                            // Hình thức thanh toán
                            order.setPaymentMethod(rs.getString("paymentMethodName"));

                            // Thông tin vận chuyển
                            Shipping ship = new Shipping();
                            ship.setShippingType(rs.getString("shipping_type"));
                            ship.setShippingFee(rs.getInt("shipping_fee"));
                            ship.setShippingStatus(rs.getString("shipping_status"));
                            ship.setTrackingNumber(rs.getString("tracking_number"));
                            order.setShipping(ship);

                            return order;
                        })
                        .findOne().orElse(null)
        );
    }

    // Lấy theo ID (Dùng cho Admin view chi tiết)
    public Order getOrderById(int orderId) {
        String sql = "SELECT o.id, " +
                "o.user_id AS userId, " +
                "o.order_code AS orderCode, " +
                "o.order_date AS orderDate, " +
                "o.total_price AS totalPrice, " +
                "s.shipping_fee AS shippingFee, " +
                "o.recipient_name AS recipientName, " +
                "o.recipient_phone AS recipientPhone, " +
                "o.shipping_address AS shippingAddress, " +
                "o.note, " +
                "u.full_name AS customerName, " +
                "u.email AS customerEmail, " +
                "u.phone AS customerPhone, " +
                "(SELECT h.status FROM order_status_history h WHERE h.order_id = o.id ORDER BY h.created_at DESC LIMIT 1) AS status " +
                "FROM orders o " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "LEFT JOIN shipping s ON o.id = s.order_id " +
                "WHERE o.id = :id";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", orderId)
                        .mapToBean(Order.class)
                        .findOne()
                        .orElse(null)
        );
    }

    // ==========================================
    // 3. DANH SÁCH ĐƠN HÀNG (ADMIN & USER)
    // ==========================================

    // Lấy tất cả đơn hàng (Chỉ sắp xếp)
    public List<Order> getAllOrders(String sortBy) {
        return getAllOrders(sortBy, null);
    }

    // Lấy tất cả đơn hàng (Sắp xếp + Tìm kiếm)
    public List<Order> getAllOrders(String sortBy, String search) {
        String orderBy = "oldest".equals(sortBy) ? "o.order_date ASC" : "o.order_date DESC";

        StringBuilder sql = new StringBuilder("SELECT o.id, " +
                "o.order_code AS orderCode, " +
                "o.order_date AS orderDate, " +
                "o.total_price AS totalPrice, " +
                "o.recipient_name AS recipientName, " +
                "o.recipient_phone AS recipientPhone, " +
                "o.shipping_address AS shippingAddress, " +
                "o.note, " +
                "(SELECT h.status FROM order_status_history h WHERE h.order_id = o.id ORDER BY h.created_at DESC LIMIT 1) AS status " +
                "FROM orders o ");

        boolean hasSearch = (search != null && !search.trim().isEmpty());
        if (hasSearch) {
            sql.append(" WHERE o.order_code LIKE :search OR o.recipient_name LIKE :search ");
        }

        sql.append(" ORDER BY ").append(orderBy);

        return jdbi.withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            if (hasSearch) {
                query.bind("search", "%" + search.trim() + "%");
            }
            return query.mapToBean(Order.class).list();
        });
    }

    // Lấy danh sách đơn hàng của một User cụ thể (Lịch sử mua hàng chi tiết)
    public List<Order> getOrdersByCustomerId(int userId) {
        String sql = "SELECT o.*, (SELECT h.status FROM order_status_history h WHERE h.order_id = o.id ORDER BY h.created_at DESC LIMIT 1) AS status " +
                "FROM orders o WHERE o.user_id = :userId ORDER BY o.order_date DESC";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    // Hàm Hủy đơn: Chỉ Insert vào history
    public void cancelOrder(int orderId) {
        Jdbi jdbi = DBDAO.get();
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO order_status_history (order_id, status, created_at) VALUES (:orderId, :status, NOW())")
                    .bind("orderId", orderId)
                    .bind("status", "Đã hủy")
                    .execute();
        });
    }

    // Lấy danh sách đơn hàng theo UserId (Dùng sub-query lấy status mới nhất)
    public List<Order> getOrdersByUserId(int userId) {
        Jdbi jdbi = DBDAO.get();
        String sql = """
        SELECT o.*, 
               COALESCE(
                   (SELECT status 
                    FROM order_status_history h 
                    WHERE h.order_id = o.id 
                    ORDER BY h.created_at DESC 
                    LIMIT 1), 
                   'Chờ xử lý') AS status
        FROM orders o
        WHERE o.user_id = :userId
        ORDER BY o.order_date DESC
    """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    // Lấy danh sách đơn hàng cho Client (UserOrder view)
    public List<UserOrder> getMyOrders(int userId) {
        String sql = "SELECT " +
                "o.id, " +
                "o.order_code AS orderCode, " +
                "o.order_date AS orderDate, " +
                "o.total_price AS totalPrice, " +
                "(SELECT status FROM order_status_history WHERE order_id = o.id ORDER BY created_at DESC LIMIT 1) AS status, " +
                "p.product_name AS productName, " +
                "pv.image_url AS imageUrl, " +
                "pv.color, " +
                "pv.size, " +
                "od.quantity, " +
                "(SELECT COUNT(*) - 1 FROM order_details WHERE order_id = o.id) AS otherItemsCount " +
                "FROM orders o " +
                "JOIN order_details od ON o.id = od.order_id " +
                "JOIN product_variants pv ON od.variant_id = pv.id " +
                "JOIN products p ON pv.product_id = p.id " +
                "WHERE o.user_id = :uid " +
                "GROUP BY o.id " +
                "ORDER BY o.order_date DESC";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("uid", userId)
                        .mapToBean(UserOrder.class)
                        .list()
        );
    }

    // ==========================================
    // 4. LẤY SẢN PHẨM TRONG ĐƠN (ITEMS)
    // ==========================================
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        String sql = "SELECT od.variant_id AS variantId, pv.variant_code AS variantCode, p.product_name AS name, " +
                "od.quantity AS quantity, " +
                "od.unit_price AS price, " +
                "pv.color AS color, " +
                "pv.size AS size, " +
                "pv.image_url AS imageUrl, " +
                "COALESCE(d2.discount_percent, d1.discount_percent, 0) AS discount " +
                "FROM order_details od " +
                "JOIN product_variants pv ON od.variant_id = pv.id " +
                "JOIN products p ON pv.product_id = p.id " +
                "LEFT JOIN product_types t ON p.product_type_id = t.id " +
                "LEFT JOIN categories c ON p.category_id = c.id " +
                "LEFT JOIN discount_product_types dt ON t.id = dt.product_type_id " +
                "LEFT JOIN discount_categories dc ON c.id = dc.category_id " +
                "LEFT JOIN discounts d1 ON dc.discount_id = d1.id " +
                "LEFT JOIN discounts d2 ON dt.discount_id = d2.id " +
                "WHERE od.order_id = :orderId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("orderId", orderId)
                        .mapToBean(OrderItem.class)
                        .list()
        );
    }


    public boolean updateOrderStatus(int orderId, String newStatus, String newShippingStatus) {
        try {
            return jdbi.inTransaction(handle -> {
                // 1. Ghi nhận lịch sử trạng thái (Order Status History)
                handle.createUpdate("INSERT INTO order_status_history (order_id, status, created_at) VALUES (:id, :status, NOW())")
                        .bind("id", orderId)
                        .bind("status", newStatus)
                        .execute();

                // 2. Cập nhật trạng thái vận chuyển trong bảng shipping (Để trang Tracking hiển thị đúng)
                int rowCount = handle.createUpdate("UPDATE shipping SET shipping_status = :shipStatus WHERE order_id = :id")
                        .bind("shipStatus", newShippingStatus)
                        .bind("id", orderId)
                        .execute();

                // Trả về true nếu không có lỗi
                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}