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

        String vIdRaw = request.getParameter("variantId");
        String qtyRaw = request.getParameter("quantity");

        String redirectAction = request.getParameter("redirectAction");

        try {
            if (vIdRaw != null && !vIdRaw.isEmpty()) {
                int variantId = Integer.parseInt(vIdRaw);
                int quantity = Integer.parseInt(qtyRaw);

                CartDAO dao = new CartDAO();
                String result = dao.addToCart(user.getId(), variantId, quantity);

                if ("Success".equals(result)) {
                    if ("buy".equals(redirectAction)) {
                        response.sendRedirect(request.getContextPath() + "/cart");
                        return;
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

        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath() + "/home");
    }
}