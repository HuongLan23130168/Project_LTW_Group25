package com.example.project_ltw_25.admin.services;

import com.example.project_ltw_25.admin.dao.NotificationDAO;
import com.example.project_ltw_25.admin.model.Notification;
import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.user.model.Product;
import com.example.project_ltw_25.user.model.Product_variant;
import com.example.project_ltw_25.user.model.User;

import java.text.NumberFormat;
import java.util.Locale;

public class NotificationService {

    private final NotificationDAO notificationDAO = new NotificationDAO();

    public void notifyNewUserRegistration(User user) {
        Notification notif = new Notification();
        notif.setUserId(user.getId());
        notif.setTitle("Tài khoản mới");
        notif.setContent("Tài khoản '" + user.getFullName() + "' vừa được đăng ký.");
        notif.setType("account");
        notif.setStatus("unread");
        notif.setEntityId(user.getId());
        notificationDAO.addNotification(notif);
    }

    public void notifyNewProductAdded(Product product, User adminUser) {
        Notification notif = new Notification();
        if (adminUser != null) {
            notif.setUserId(adminUser.getId());
        }
        notif.setTitle("Sản phẩm mới");
        notif.setContent("Sản phẩm '" + product.getProduct_name() + "' vừa được thêm vào kho.");
        notif.setType("product");
        notif.setStatus("unread");
        notif.setEntityId(product.getId());
        notificationDAO.addNotification(notif);
    }

    public void notifyProductLowStock(Product product, Product_variant variant, int currentStock) {
        Notification notif = new Notification();
        notif.setTitle("Cảnh báo tồn kho thấp");
        notif.setContent("Sản phẩm '" + product.getProduct_name() + "' (Màu: " + variant.getColor() + ", Size: " + variant.getSize() + ") chỉ còn " + currentStock + " sản phẩm.");
        notif.setType("inventory");
        notif.setStatus("unread");
        notif.setEntityId(product.getId());
        notificationDAO.addNotification(notif);
    }

    public void notifyProductOutOfStock(Product product, Product_variant variant) {
        Notification notif = new Notification();
        notif.setTitle("Hết hàng");
        notif.setContent("Sản phẩm '" + product.getProduct_name() + "' (Màu: " + variant.getColor() + ", Size: " + variant.getSize() + ") đã hết hàng.");
        notif.setType("inventory");
        notif.setStatus("unread");
        notif.setEntityId(product.getId());
        notificationDAO.addNotification(notif);
    }

    public void notifyOrderUpdate(Order order, String newStatus) {
        Notification notif = new Notification();
        notif.setUserId(order.getUserId());
        notif.setTitle("Cập nhật đơn hàng");
        notif.setContent("Đơn hàng #" + order.getOrderCode() + " đã được chuyển sang trạng thái: " + newStatus);
        notif.setType("order");
        notif.setStatus("unread");
        notif.setEntityId(order.getId());

        notificationDAO.addNotification(notif);
    }

    public void notifyNewOrder(Order order) {
        if (order == null || order.getUserId() <= 0) {
            System.out.println("Lỗi: Không thể gửi thông báo vì Order null hoặc UserId không hợp lệ!");
            return;
        }

        Notification notif = new Notification();
        notif.setUserId(order.getUserId()); 
        notif.setTitle("Đơn hàng mới");

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotal = currencyFormatter.format(order.getTotalPrice());

        notif.setContent("Đơn hàng #" + order.getOrderCode() + " giá trị " + formattedTotal + " đã đặt thành công.");
        notif.setType("order");
        notif.setStatus("unread");
        notif.setEntityId(order.getId());

        System.out.println("DEBUG: Dang insert thong bao cho User ID: " + order.getUserId());
        notificationDAO.addNotification(notif);
    }
}
