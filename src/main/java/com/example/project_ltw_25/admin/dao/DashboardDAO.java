package com.example.project_ltw_25.admin.dao;

import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.user.dao.DBDAO;
import com.example.project_ltw_25.user.model.Product;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardDAO {
    private Jdbi jdbi = DBDAO.get();

    // 1. Tổng doanh thu tháng hiện tại
    public double getTotalRevenue() {
//        return jdbi.withHandle(handle ->
//                // Sửa lại thành 'D' hoặc 'Delivered' tùy theo cách bạn lưu trong DB
//                handle.createQuery("SELECT SUM(total_price) FROM orders WHERE status = 'D' OR status = 'Delivered'")
//                        .mapTo(Double.class)
//                        .findOne()
//                        .orElse(0.0)
//        );
        return 1000000000;
    }

    // 2. Danh sách 8 đơn hàng mới nhất
    public List<Order> getRecentOrders() {
//        return jdbi.withHandle(handle ->
//                handle.createQuery("SELECT id, order_code AS orderCode, recipient_name AS recipientName, " +
//                                "total_price AS totalPrice, order_date AS orderDate " +
//                                "FROM orders ORDER BY order_date DESC LIMIT 8")
//                        .mapToBean(Order.class)
//                        .list()
//        );
        return new ArrayList<>();

    }

    // 3. Top 4 sản phẩm bán chạy nhất (Để hiển thị bên phải Dashboard)
    // Sửa trong DashboardDAO.java
    public List<Product> getBestSellers() {
//        String sql = "SELECT p.id, p.product_name AS name, p.price, '' AS image, SUM(od.quantity) as total_sold " +
//                "FROM order_details od " +
//                "JOIN products p ON od.product_id = p.id " +
//                "GROUP BY p.id, p.product_name, p.price " +
//                "ORDER BY total_sold DESC " +
//                "LIMIT 4"; // Thay SELECT TOP 4 bằng LIMIT 4 ở cuối
//
//        return jdbi.withHandle(handle ->
//                handle.createQuery(sql)
//                        .mapToBean(Product.class)
//                        .list()
//        );
        return new ArrayList<>();
    }

    // 4. Đếm số đơn hàng chờ xử lý
    public int countPendingOrders() {
//        return jdbi.withHandle(handle ->
//                handle.createQuery("SELECT COUNT(*) FROM orders WHERE status = 'Processing'")
//                        .mapTo(Integer.class)
//                        .one()
//        );
        return 0;
    }
    public List<Map<String, Object>> getRevenueLast7Days() {
//        String sql =
//                "WITH Last7Days AS (" +
//                        "    SELECT CAST(GETDATE() AS DATE) AS DisplayDate, 0 AS DaysAgo " +
//                        "    UNION ALL " +
//                        "    SELECT DATEADD(day, -1, DisplayDate), DaysAgo + 1 " +
//                        "    FROM Last7Days WHERE DaysAgo < 6" +
//                        ") " +
//                        "SELECT FORMAT(d.DisplayDate, 'dd/MM') as date, " +
//                        "       ISNULL(SUM(o.total_price), 0) as daily_revenue " +
//                        "FROM Last7Days d " +
//                        "LEFT JOIN orders o ON CAST(o.order_date AS DATE) = d.DisplayDate AND o.status = 'Delivered' " +
//                        "GROUP BY d.DisplayDate " +
//                        "ORDER BY d.DisplayDate ASC";
//
//        return jdbi.withHandle(handle ->
//                handle.createQuery(sql)
//                        .mapToMap()
//                        .list()
//        );
        return new ArrayList<>();

    }
    // Đếm sản phẩm sắp hết hàng (tồn kho < 10)
    public int getLowStockCount() {
//        String sql = "SELECT COUNT(*) FROM inventories WHERE stock_quantity < 10";
//        return jdbi.withHandle(handle ->
//                handle.createQuery(sql)
//                        .mapTo(Integer.class)
//                        .one()
//        );
        return 0;
    }

    // Lấy tổng số sản phẩm trong kho
    public int getTotalStock() {
//        String sql = "SELECT SUM(stock_quantity) FROM inventories";
//        return jdbi.withHandle(handle ->
//                handle.createQuery(sql)
//                        .mapTo(Integer.class)
//                        .findOne()
//                        .orElse(0)
//        );
        return 0;
    }

}
