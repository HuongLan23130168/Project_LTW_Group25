package com.example.demoweb1.controller;

import com.example.demoweb1.dao.CustomerDAO;
import com.example.demoweb1.dao.OrderDAO;
import com.example.demoweb1.model.Customer;
import com.example.demoweb1.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/customer-detail")
public class CustomerDetailServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/customers");
            return;
        }

        try {
            int customerId = Integer.parseInt(idParam);

            CustomerDAO customerDAO = new CustomerDAO();
            OrderDAO orderDAO = new OrderDAO();

            Customer customer = customerDAO.getCustomerById(customerId);

            if (customer == null) {
                req.setAttribute("error", "Không tìm thấy khách hàng với ID: " + customerId);
                req.getRequestDispatcher("/admin/customers.jsp").forward(req, resp);
                return;
            }

            List<Order> orderList = orderDAO.getOrdersByCustomerId(customerId);

            double totalSpent = 0;
            for (Order order : orderList) {
                totalSpent += order.getTotal_price(); // Sửa lại để gọi đúng phương thức
            }

            Order latestOrder = null;
            if (orderList != null && !orderList.isEmpty()) {
                latestOrder = orderList.get(0);
            }

            req.setAttribute("customer", customer);
            req.setAttribute("orderList", orderList);
            req.setAttribute("totalSpent", totalSpent);
            req.setAttribute("latestOrder", latestOrder);

            req.getRequestDispatcher("/admin/customerDetail.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/customers");
        }
    }
}
