package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.CartDAO;
import com.example.project_ltw_25.user.model.CartItem;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CartServlet", value = "/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    // Gộp chung logic xử lý để tránh sai sót giữa GET và POST
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        CartDAO dao = new CartDAO();
        String action = request.getParameter("action");
        String idRaw = request.getParameter("id");
        String qtyRaw = request.getParameter("quantity");

        try {
            if (action != null) {
                // Nhánh Checkout: Xử lý trước vì không cần ID sản phẩm lẻ
                if ("checkout".equals(action)) {
                    List<CartItem> list = dao.getCartByUserId(user.getId());
                    if (list == null || list.isEmpty()) {
                        session.setAttribute("error", "Giỏ hàng của bạn đang trống!");
                        response.sendRedirect(request.getContextPath() + "/cart");
                        return;
                    }
                    double grandTotal = list.stream().mapToDouble(CartItem::getTotalPrice).sum();
                    request.setAttribute("cartItems", list);
                    request.setAttribute("grandTotal", grandTotal);
                    request.getRequestDispatcher("/frontend/pay.jsp").forward(request, response);
                    return;
                }

                // Các nhánh cần ID (Update/Delete)
                if (idRaw != null && !idRaw.isEmpty()) {
                    int variantId = Integer.parseInt(idRaw);
                    if ("delete".equals(action)) {
                        dao.removeItem(user.getId(), variantId);
                    } else if ("update".equals(action) && qtyRaw != null) {
                        int quantity = Integer.parseInt(qtyRaw);
                        dao.updateQuantity(user.getId(), variantId, quantity);
                    }
                    response.sendRedirect(request.getContextPath() + "/cart");
                    return;
                }
            }

            // MẶC ĐỊNH: Hiển thị giỏ hàng
            List<CartItem> list = dao.getCartByUserId(user.getId());
            double grandTotal = 0;
            int totalQuantity = 0;
            if (list != null) {
                for (CartItem item : list) {
                    grandTotal += item.getTotalPrice();
                    totalQuantity += item.getQuantity();
                }
            }
            request.setAttribute("cartItems", list);
            request.setAttribute("grandTotal", grandTotal);
            session.setAttribute("totalQty", totalQuantity);
            request.getRequestDispatcher("/frontend/cart.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Lỗi hệ thống: " + e.getMessage());
        }
    }
}