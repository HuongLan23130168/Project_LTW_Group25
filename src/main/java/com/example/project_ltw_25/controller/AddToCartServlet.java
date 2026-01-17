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

        try {
            // Kiểm tra xem dữ liệu có bị null hoặc trống không
            if (vIdRaw != null && !vIdRaw.isEmpty() && qtyRaw != null && !qtyRaw.isEmpty()) {
                int variantId = Integer.parseInt(vIdRaw);
                int quantity = Integer.parseInt(qtyRaw);

                CartDAO dao = new CartDAO();
                String result = dao.addToCart(user.getId(), variantId, quantity);

                if ("Success".equals(result)) {
                    session.setAttribute("msg", "Đã thêm vào giỏ hàng!");
                } else {
                    session.setAttribute("error", result);
                }
            } else {
                session.setAttribute("error", "Lỗi: Không tìm thấy ID sản phẩm hoặc số lượng.");
            }
        } catch (Exception e) {
            // QUAN TRỌNG: Ghi log lỗi ra console để debug
            e.printStackTrace();
            session.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        // Quay lại trang cũ (Referer)
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}