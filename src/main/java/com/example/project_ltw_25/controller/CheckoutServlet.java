package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.CartDAO;
import com.example.project_ltw_25.user.model.CartItem;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        CartDAO cartDAO = new CartDAO();
        List<CartItem> items = cartDAO.getCartByUserId(user.getId());

        if (items == null || items.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        double grandTotal = items.stream().mapToDouble(CartItem::getTotalPrice).sum();

        request.setAttribute("cartItems", items);
        request.setAttribute("grandTotal", grandTotal);

        request.getRequestDispatcher("/frontend/pay.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}