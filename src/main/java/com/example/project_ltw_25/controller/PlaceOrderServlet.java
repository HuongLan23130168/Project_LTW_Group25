package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.admin.dao.OrderDAO;
import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.admin.model.OrderItem;
import com.example.project_ltw_25.user.dao.CartDAO;
import com.example.project_ltw_25.user.model.CartItem;
import com.example.project_ltw_25.user.model.User;
import com.example.project_ltw_25.admin.services.NotificationService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PlaceOrderServlet", value = "/place-order")
public class PlaceOrderServlet extends HttpServlet {
    NotificationService notificationService = new NotificationService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            System.out.println("Đã chạy vào PlaceOrderServlet!");
            request.setCharacterEncoding("UTF-8");
            HttpSession session = request.getSession();

            // kt user login
            User user = (User) session.getAttribute("acc");
            if (user == null) {
                System.out.println("Lỗi: Không tìm thấy User trong session!");
                response.sendRedirect("login");
                return;
            }

            // lấy dữ liệu từ form
            String name = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String city = request.getParameter("city");
            String district = request.getParameter("district");
            String ward = request.getParameter("ward");
            String detail = request.getParameter("addressDetail");
            String address = detail + ", " + ward + ", " + district + ", " + city;
            String note = request.getParameter("note");

            int paymentId = Integer.parseInt(request.getParameter("paymentMethod"));
            String shippingType = request.getParameter("shippingType");
            double shippingFee = "hỏa tốc".equals(shippingType) ? 130000 : 30000;

            // kt giỏ hàng
            CartDAO cartDAO = new CartDAO();
            List<CartItem> items = cartDAO.getCartByUserId(user.getId());
            if (items == null || items.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // tính toán và tạo đơn hàng
            double total = items.stream().mapToDouble(CartItem::getTotalPrice).sum();
            OrderDAO orderDAO = new OrderDAO();
            String orderCode = orderDAO.createOrder(user.getId(), name, phone, address, note,
                    paymentId, items, total, shippingType, shippingFee);

            // tạo đơn hàng thành công
            if (orderCode != null) {
                Order order = orderDAO.getOrderByCode(orderCode);

                notificationService.notifyNewOrder(order);

                List<OrderItem> orderItems = orderDAO.getOrderItemsByOrderId(order.getId());
                double actualTotal = orderItems.stream()
                        .mapToDouble(i -> (i.getPrice() * i.getQuantity()) * (1 - i.getDiscount() / 100.0))
                        .sum();

                request.setAttribute("orderItems", orderItems);
                request.setAttribute("orderCode", orderCode);
                request.setAttribute("orderName", name);
                request.setAttribute("orderPhone", phone);
                request.setAttribute("orderEmail", email);
                request.setAttribute("orderAddress", address);
                request.setAttribute("orderDate", new java.util.Date());
                request.setAttribute("shippingType", shippingType);
                request.setAttribute("shippingFee", shippingFee);
                request.setAttribute("grandTotal", actualTotal);
                request.setAttribute("paymentMethod", (paymentId == 1 ? "COD" : "Chuyển khoản"));
                request.setAttribute("orderNote", note);

                // xóa giỏ hàng sau khi hoàn tất
                cartDAO.clearCart(user.getId());
                request.getRequestDispatcher("/frontend/completed.jsp").forward(request, response);
            } else {
                response.sendRedirect("checkout?error=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Lỗi chi tiết: " + e.getMessage());
        }
    }
}