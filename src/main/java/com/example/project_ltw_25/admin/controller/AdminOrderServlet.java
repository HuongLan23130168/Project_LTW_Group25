package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.OrderDAO;
import com.example.project_ltw_25.admin.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminOrderServlet", value = "/admin/orders")
public class AdminOrderServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Lấy tham số sắp xếp
        String sortBy = request.getParameter("sortBy");
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "newest";
        }

        // 2. [MỚI] Lấy tham số tìm kiếm
        String search = request.getParameter("search");

        // 3. Gọi DAO với cả 2 tham số: sắp xếp và tìm kiếm
        List<Order> orderList = orderDAO.getAllOrders(sortBy, search);

        // Debug: Kiểm tra log server
        System.out.println("DEBUG: Search='" + search + "', Sort='" + sortBy + "', Found=" + (orderList != null ? orderList.size() : 0));

        // 4. Gửi dữ liệu sang JSP
        request.setAttribute("orders", orderList);
        request.setAttribute("currentSort", sortBy); // Để JSP biết đang sort kiểu gì (giữ select box)
        request.setAttribute("search", search);      // Để JSP hiển thị lại từ khóa trong ô input

        request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}