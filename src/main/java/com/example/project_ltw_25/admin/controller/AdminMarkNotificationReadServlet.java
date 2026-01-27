package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminMarkNotificationReadServlet", value = "/admin/mark-notification-read")
public class AdminMarkNotificationReadServlet extends HttpServlet {
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idStr = request.getParameter("id");
            String redirectUrl = request.getParameter("redirectUrl");

            if (idStr != null) {
                int notificationId = Integer.parseInt(idStr);
                notificationDAO.markAsRead(notificationId);
            }

            if (redirectUrl != null && !redirectUrl.isEmpty() && !redirectUrl.equals("#")) {
                String finalUrl = redirectUrl.startsWith("/")
                        ? request.getContextPath() + redirectUrl
                        : request.getContextPath() + "/" + redirectUrl;
                response.sendRedirect(finalUrl);

            } else {
                response.sendRedirect(request.getContextPath() + "/admin/notifications");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/notifications?error=true");
        }
    }
}
