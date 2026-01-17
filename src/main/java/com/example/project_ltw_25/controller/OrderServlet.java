package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.OrderDAO; // Import DAO
import com.example.project_ltw_25.admin.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/orders")
public class OrderServlet extends HttpServlet {

    // SỬA: Dùng OrderDAO thay vì OrderService
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. Lấy tham số sắp xếp
        String sortBy = req.getParameter("sortBy");
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "newest";
        }

        // 2. Gọi hàm từ DAO (Hàm này đã có sẵn trong code bạn gửi)
        List<Order> orderList = orderDAO.getAllOrders(sortBy);

        // Debug: In ra console xem có lấy được dòng nào không
        System.out.println("DEBUG: Số đơn hàng lấy được = " + (orderList != null ? orderList.size() : 0));

        // 3. Đẩy dữ liệu sang JSP
        req.setAttribute("orders", orderList);
        req.getRequestDispatcher("/admin/orders.jsp").forward(req, resp);
    }
}