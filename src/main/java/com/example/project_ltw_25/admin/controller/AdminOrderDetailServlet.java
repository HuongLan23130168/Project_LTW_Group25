package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.OrderDAO;
import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.admin.model.OrderItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminOrderDetailServlet", value = "/admin/order-detail")
public class AdminOrderDetailServlet extends HttpServlet {

    // Khởi tạo DAO
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Lấy tham số ID từ URL
        String idParam = request.getParameter("id");

        // 2. Kiểm tra nếu ID rỗng hoặc null thì quay về trang danh sách
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }

        try {
            // 3. Parse ID sang số nguyên
            int orderId = Integer.parseInt(idParam);

            // 4. Lấy thông tin đơn hàng từ DB
            Order order = orderDAO.getOrderById(orderId);

            // 5. Nếu không tìm thấy đơn hàng, báo lỗi
            if (order == null) {
                response.sendRedirect(request.getContextPath() + "/admin/orders?error=notfound");
                return;
            }

            // 6. Lấy danh sách sản phẩm trong đơn hàng
            List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(orderId);

            // 7. Gắn dữ liệu vào request để hiển thị bên JSP
            request.setAttribute("order", order);
            request.setAttribute("orderItems", orderItems);

            // 8. Chuyển hướng đến trang hiển thị chi tiết (viewOrder.jsp)
            request.getRequestDispatcher("/admin/viewOrder.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Nếu ID không phải là số hợp lệ
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }
    }
}