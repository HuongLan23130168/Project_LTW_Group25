package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.admin.dao.OrderDAO;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Đường dẫn này khớp với action trong form ở file JSP
@WebServlet(name = "ConfirmReceiptServlet", urlPatterns = "/user/confirmReceipt")
public class ConfirmReceiptServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Kiểm tra đăng nhập (Bảo mật: Phải đăng nhập mới được xác nhận)
        HttpSession session = req.getSession();
        User authUser = (User) session.getAttribute("acc");

        if (authUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // 2. Lấy ID đơn hàng từ JSP gửi lên
            String orderIdStr = req.getParameter("orderId");

            if (orderIdStr == null || orderIdStr.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/account?status=shipping&error=missing_id");
                return;
            }

            int orderId = Integer.parseInt(orderIdStr);

            // 3. Gọi DAO để cập nhật trạng thái trong Database
            OrderDAO orderDAO = new OrderDAO();

            // Cập nhật trạng thái đơn hàng thành "Đã giao"
            // Cập nhật trạng thái vận chuyển thành "Giao hàng thành công"
            boolean isUpdated = orderDAO.updateOrderStatus(orderId, "Đã giao", "Giao hàng thành công");

            if (isUpdated) {
                // 4. Thành công -> Chuyển hướng sang tab "Đã giao"
                resp.sendRedirect(req.getContextPath() + "/account?status=done");
            } else {
                // Thất bại (Lỗi DB) -> Quay lại tab đang giao kèm thông báo lỗi
                resp.sendRedirect(req.getContextPath() + "/account?status=shipping&error=update_failed");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/account?status=shipping&error=invalid_id");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/account?status=shipping&error=system_error");
        }
    }
}