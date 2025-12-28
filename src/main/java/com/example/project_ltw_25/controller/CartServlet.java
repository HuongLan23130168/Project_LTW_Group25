package com.example.project_ltw_25.controller;

import com.example.project_ltw_25.dao.CartDAO;
import com.example.project_ltw_25.model.CartItem;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CartServlet", value = "/cart")
public class CartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        // Giả sử bạn lấy userId từ session người dùng đã đăng nhập
        Integer userId = (Integer) session.getAttribute("userId");

        // Nếu chưa đăng nhập -> trang login
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/frontend/login.jsp");
            return;
        }

        if ("add".equals(action)) {
            int variantId = Integer.parseInt(request.getParameter("variantId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            CartDAO dao = new CartDAO();

            // 1. Lưu vào Database
            int cartId = dao.getOrCreateCart(userId);
            dao.addItemToCart(cartId, variantId, quantity);

            // 2. Cập nhật lại giỏ hàng trong Session để JSP hiển thị ngay lập tức
            session.setAttribute("cart", CartDAO.getCartDetails(userId));

            // 3. Chuyển hướng -> giỏ hàng
            response.sendRedirect(request.getContextPath() + "/frontend/cart.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Kiểm tra đăng nhập qua đối tượng 'acc'
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("acc");
        if (userObj == null) {
            response.sendRedirect(request.getContextPath() + "/frontend/login.jsp");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");

        String action = request.getParameter("action");

        // 2. Nếu action == null, nghĩa là người dùng chỉ muốn XEM GIỎ HÀNG
        if (action == null) {
            session.setAttribute("cart", CartDAO.getCartDetails(userId));
            request.getRequestDispatcher("/frontend/cart.jsp").forward(request, response);
            return;
        }

        // 3. Nếu có action (update hoặc delete)
        try {
            int variantId = Integer.parseInt(request.getParameter("id"));

            if (action.equals("update")) {
                int newQty = Integer.parseInt(request.getParameter("quantity"));
                if (newQty > 0) {
                    CartDAO.updateQuantity(userId, variantId, newQty);
                }
            } else if (action.equals("delete")) {
                CartDAO.removeItem(userId, variantId);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Tránh bị dừng chương trình nếu parse lỗi
        }

        // 4. Cập nhật lại session và redirect để tránh lặp lại hành động khi F5
        session.setAttribute("cart", CartDAO.getCartDetails(userId));
        response.sendRedirect(request.getContextPath() + "/cart");
        }


}