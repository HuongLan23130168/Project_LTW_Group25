package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.OrderDAO;
import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.admin.model.OrderItem;
import com.example.project_ltw_25.admin.model.OrderStatusHistory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/viewOrder")
public class ViewOrderServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String orderIdParam = req.getParameter("orderId");
        if (orderIdParam != null) {
            int orderId = Integer.parseInt(orderIdParam);
            OrderDAO dao = new OrderDAO();
            Order order = dao.getOrderById(orderId);

            if (order == null) {
                req.getSession().setAttribute("errorMessage", "Không tìm thấy đơn hàng với ID: " + orderId);
                resp.sendRedirect(req.getContextPath() + "/admin/orders");
                return;
            }

            List<OrderItem> orderItems = dao.getOrderItemsByOrderId(orderId);
            List<OrderStatusHistory> statusHistory = dao.getStatusHistoryByOrderId(orderId);

            req.setAttribute("orderId", order.getOrder_code());
            req.setAttribute("orderStatus", order.getStatus());
            req.setAttribute("orderDate", order.getOrder_date());
            req.setAttribute("customerName", order.getCustomerName());
            req.setAttribute("customerEmail", order.getCustomerEmail());
            req.setAttribute("customerPhone", order.getCustomerPhone());
            req.setAttribute("customerAddress", order.getCustomerAddress());
            req.setAttribute("paymentMethod", order.getPaymentMethod());
            req.setAttribute("orderItems", orderItems);
            req.setAttribute("shippingFee", order.getShippingFee());
            req.setAttribute("grandTotal", order.getGrandTotal());
            req.setAttribute("statusHistory", statusHistory);

            req.getRequestDispatcher("/admin/viewOrders.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/orders");
        }
    }
}
