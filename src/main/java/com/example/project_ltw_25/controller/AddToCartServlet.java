package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.CartDAO;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AddToCartServlet", value = "/add-to-cart")
public class AddToCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy giá trị dưới dạng String trước để kiểm tra
        String vIdRaw = request.getParameter("variantId");
        String qtyRaw = request.getParameter("quantity");

        String redirectAction = request.getParameter("redirectAction"); // Lấy action từ form

        try {
            if (vIdRaw != null && !vIdRaw.isEmpty()) {
                int variantId = Integer.parseInt(vIdRaw);
                int quantity = Integer.parseInt(qtyRaw);

                CartDAO dao = new CartDAO();
                String result = dao.addToCart(user.getId(), variantId, quantity);

                if ("Success".equals(result)) {
                    // Nếu khách chọn "Mua ngay", chuyển hướng thẳng tới trang giỏ hàng
                    if ("buy".equals(redirectAction)) {
                        response.sendRedirect(request.getContextPath() + "/cart");
                        return; // Ngắt hàm tại đây
                    }
                    int newTotal = dao.getTotalQuantityByUserId(user.getId());
                    session.setAttribute("totalQty", newTotal);

                    session.setAttribute("msg", "Đã thêm vào giỏ hàng!");
                } else {
                    session.setAttribute("error", result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Nếu là "add" (Thêm vào giỏ), quay lại trang cũ để hiện SweetAlert
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath() + "/home");
    }
}