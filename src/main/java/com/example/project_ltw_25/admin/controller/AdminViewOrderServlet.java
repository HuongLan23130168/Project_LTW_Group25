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
            // Tạm thời không dùng PaymentMethodDAO
            // PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO();

            // Bước 1: Lấy đối tượng Order cơ bản
            Order order = orderDAO.getOrderById(orderId);

            if (order == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy đơn hàng với ID: " + orderId);
                response.sendRedirect(request.getContextPath() + "/admin/orders");
                return;
            }

            // Bước 2: Lấy thông tin khách hàng (nếu có)
            User customer = customerDAO.getCustomerById(order.getUserId());
            if (customer != null) {
                order.setCustomerName(customer.getFullName());
                order.setCustomerEmail(customer.getEmail());
                order.setCustomerPhone(customer.getPhone());
            } else {
                // Nếu không có customer, dùng thông tin người nhận
                order.setCustomerName(order.getRecipientName());
                order.setCustomerEmail("Không có");
                order.setCustomerPhone(order.getRecipientPhone());
            }
            // Địa chỉ luôn là địa chỉ giao hàng
            order.setCustomerAddress(order.getShippingAddress());

            // Bước 3: Tạm thời bỏ qua việc lấy tên phương thức thanh toán
            // String paymentMethodName = paymentMethodDAO.getPaymentMethodNameById(order.getPayment_method_id());
            order.setPaymentMethod("Không xác định"); // Gán giá trị mặc định

            // Bước 4: Tạm thời không lấy danh sách sản phẩm để tránh lỗi
            // List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(orderId);
            List<OrderItem> orderItems = Collections.emptyList(); // Tạo một danh sách rỗng


            // Bước 5: Gửi tất cả dữ liệu sang JSP
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