package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.CustomerDAO;
import com.example.project_ltw_25.admin.dao.OrderDAO;
import com.example.project_ltw_25.admin.dao.PaymentMethodDAO;
import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.admin.model.OrderItem;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "ViewOrderServlet", value = "/admin/viewOrder")
public class AdminViewOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);
            OrderDAO orderDAO = new OrderDAO();
            CustomerDAO customerDAO = new CustomerDAO();

            Order order = orderDAO.getOrderById(orderId);

            if (order == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy đơn hàng với ID: " + orderId);
                response.sendRedirect(request.getContextPath() + "/admin/orders");
                return;
            }

            User customer = customerDAO.getCustomerById(order.getUserId());
            if (customer != null) {
                order.setCustomerName(customer.getFullName());
                order.setCustomerEmail(customer.getEmail());
                order.setCustomerPhone(customer.getPhone());
            } else {
                order.setCustomerName(order.getRecipientName());
                order.setCustomerEmail("Không có");
                order.setCustomerPhone(order.getRecipientPhone());
            }
            order.setCustomerAddress(order.getShippingAddress());


            order.setPaymentMethod("Không xác định");


            List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(orderId);


            request.setAttribute("order", order);
            request.setAttribute("orderItems", orderItems);

            request.getRequestDispatcher("/admin/viewOrders.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}