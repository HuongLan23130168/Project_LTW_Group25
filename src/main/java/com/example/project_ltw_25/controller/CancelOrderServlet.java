package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.admin.dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CancelOrderServlet", urlPatterns = "/cancel-order")
public class CancelOrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Lấy ID đơn hàng cần hủy từ JSP gửi sang
        String idStr = req.getParameter("orderId");

        if (idStr != null) {
            int orderId = Integer.parseInt(idStr);

            // 2. Gọi DAO để update database
            OrderDAO dao = new OrderDAO();
            dao.cancelOrder(orderId);
        }

        // 3. Sau khi hủy xong, quay lại trang đơn hàng (Tab Chờ xử lý hoặc Đã hủy)
        // Dùng sendRedirect để load lại trang account và cập nhật giao diện
        resp.sendRedirect("account?status=wait");
    }
}