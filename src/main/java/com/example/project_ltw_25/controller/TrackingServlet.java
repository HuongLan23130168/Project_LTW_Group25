package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.admin.dao.OrderDAO;
import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.admin.model.OrderItem;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PayServlet", value = "/tracking")
public class TrackingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderCode = request.getParameter("orderCode");

        if (orderCode != null && !orderCode.trim().isEmpty()) {
            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.getOrderByCode(orderCode.trim());

            if (order != null) {
                List<OrderItem> items = orderDAO.getOrderItemsByOrderId(order.getId());

                request.setAttribute("order", order);
                request.setAttribute("orderItems", items);
            } else {
                request.setAttribute("error", "Không tìm thấy đơn hàng: " + orderCode);
            }
        }
        request.getRequestDispatcher("/frontend/tracking.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
