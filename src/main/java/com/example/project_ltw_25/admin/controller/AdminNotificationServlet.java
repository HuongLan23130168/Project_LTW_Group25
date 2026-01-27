package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.NotificationDAO;
import com.example.project_ltw_25.admin.model.Notification;
import com.example.project_ltw_25.user.model.Product;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminNotificationServlet", value = "/admin/notifications")
public class AdminNotificationServlet extends HttpServlet {
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("markAllRead")) {
                notificationDAO.markAllAsRead();
                response.sendRedirect(request.getContextPath() + "/admin/notifications");
                return;
            } else if (action.equals("deleteAll")) {
                notificationDAO.deleteAllNotifications();
                response.sendRedirect(request.getContextPath() + "/admin/notifications");
                return;
            }
        }

        String filter = request.getParameter("filter");
        if (filter == null || filter.isEmpty()) {
            filter = "all";
        }
        request.setAttribute("notifications", notificationDAO.getNotifications(filter));
        request.setAttribute("currentFilter", filter);

        List<Notification> notifications = notificationDAO.getNotifications(filter);


        request.getRequestDispatcher("/admin/notifi.jsp").forward(request, response);
    }
    public void notifyNewProductAdded(Product product, User admin) {
        Notification notif = new Notification();
        notif.setUserId(admin.getId());
        notif.setTitle("Sản phẩm mới");
        notif.setContent("Sản phẩm '" + product.getProduct_name() + "' vừa được thêm vào kho.");
        notif.setType("product");
        notif.setStatus("unread");

        notif.setEntityId(product.getId());

        notificationDAO.addNotification(notif);
    }
}