package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.CustomerDAO;
import com.example.project_ltw_25.admin.dao.OrderDAO;
import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerDetailServlet", value = "/admin/customer-detail")
public class AdminCustomerDetailServlet extends HttpServlet {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/customers");
            return;
        }

        try {
            int customerId = Integer.parseInt(idParam);
            User customer = customerDAO.getCustomerById(customerId);

            if (customer == null) {
                // Better UX: Send back to list with an error message
                request.setAttribute("error", "Không tìm thấy khách hàng với ID: " + customerId);
                response.sendRedirect(request.getContextPath() + "/admin/customers?error=notfound");
                return;
            }

            // Fetch order history specifically for this user
            List<Order> orderList = orderDAO.getOrdersByCustomerId(customerId);

            // Calculate business metrics for the UI
            double totalSpent = orderList.stream()
                    .mapToDouble(Order::getTotalPrice)
                    .sum();

            Order latestOrder = orderList.isEmpty() ? null : orderList.get(0);

            // Set data for JSP
            request.setAttribute("customer", customer);
            request.setAttribute("orderList", orderList);
            request.setAttribute("totalSpent", totalSpent);
            request.setAttribute("latestOrder", latestOrder);

            request.getRequestDispatcher("/admin/customerDetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/customers");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}