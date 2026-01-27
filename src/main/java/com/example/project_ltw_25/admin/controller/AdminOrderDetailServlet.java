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
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(idParam);

            Order order = orderDAO.getOrderById(orderId);

            if (order == null) {
                response.sendRedirect(request.getContextPath() + "/admin/orders?error=notfound");
                return;
            }

            List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(orderId);

            request.setAttribute("order", order);
            request.setAttribute("orderItems", orderItems);

            request.getRequestDispatcher("/admin/viewOrder.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }
    }
}