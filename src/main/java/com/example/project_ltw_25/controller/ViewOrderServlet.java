package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.CustomerDAO;
import com.example.project_ltw_25.admin.dao.OrderDAO;
import com.example.project_ltw_25.admin.dao.PaymentMethodDAO;
import com.example.project_ltw_25.admin.model.Customer;
import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.admin.model.OrderItem;
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
        if (orderIdParam == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);
            OrderDAO orderDAO = new OrderDAO();
            CustomerDAO customerDAO = new CustomerDAO();
            PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO();

            // Bước 1: Lấy đối tượng Order cơ bản
            Order order = orderDAO.getOrderById(orderId);

            if (order == null) {
                req.getSession().setAttribute("errorMessage", "Không tìm thấy đơn hàng với ID: " + orderId);
                resp.sendRedirect(req.getContextPath() + "/admin/orders");
                return;
            }

            // Bước 2: Lấy thông tin khách hàng (nếu có)
            Customer customer = customerDAO.getCustomerById(order.getUser_id());
            if (customer != null) {
                order.setCustomerName(customer.getFull_name());
                order.setCustomerEmail(customer.getEmail());
                order.setCustomerPhone(customer.getPhone());
            } else {
                // Nếu không có customer, dùng thông tin người nhận
                order.setCustomerName(order.getRecipient_name());
                order.setCustomerEmail("Không có");
                order.setCustomerPhone(order.getRecipient_phone());
            }
            // Địa chỉ luôn là địa chỉ giao hàng
            order.setCustomerAddress(order.getShipping_address());

            // Bước 3: Lấy tên phương thức thanh toán
            String paymentMethodName = paymentMethodDAO.getPaymentMethodNameById(order.getPayment_method_id());
            order.setPaymentMethod(paymentMethodName);

            // Bước 4: Lấy danh sách sản phẩm
            List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(orderId);
            
            // Tính toán lại tổng tiền (nếu cần)
            // Note: total_price từ DB đã là grandTotal, nhưng nếu muốn tính lại cho chắc:
            double calculatedTotal = 0;
            for(OrderItem item : orderItems) {
                calculatedTotal += item.getTotal();
            }
            // order.setGrandTotal(calculatedTotal + order.getShippingFee());


            // Bước 5: Gửi tất cả dữ liệu sang JSP
            req.setAttribute("order", order);
            req.setAttribute("orderItems", orderItems);

            req.getRequestDispatcher("/admin/viewOrders.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/orders");
        }
    }
}
