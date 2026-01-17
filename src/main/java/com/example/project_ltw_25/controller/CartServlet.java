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
        // 1. Fix lỗi hiển thị font chữ tiếng Việt khi báo lỗi
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

        // Lấy tham số dưới dạng String trước
        String idRaw = request.getParameter("id");
        String qtyRaw = request.getParameter("quantity");

        try {
            // 2. CHỈ xử lý Logic khi có action và ID không bị NULL
            if (action != null && idRaw != null && !idRaw.isEmpty()) {
                int variantId = Integer.parseInt(idRaw);

                if ("delete".equals(action)) {
                    dao.removeItem(user.getId(), variantId);
                }
                else if ("update".equals(action) && qtyRaw != null && !qtyRaw.isEmpty()) {
                    int quantity = Integer.parseInt(qtyRaw);
                    String result = dao.updateQuantity(user.getId(), variantId, quantity);
                    if (!"Success".equals(result)) {
                        session.setAttribute("error", result);
                    }
                }
                // Sau khi xử lý Xong thì chuyển hướng để URL sạch (tránh lỗi F5)
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            // 3. LOGIC HIỂN THỊ (Khi action == null hoặc sau khi Redirect)
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
            request.setAttribute("totalQuantity", totalQuantity);

            // Forward ra trang JSP
            request.getRequestDispatcher("/frontend/cart.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi, in ra lỗi cụ thể để debug chứ không để trang trắng
            response.getWriter().println("Hệ thống gặp lỗi: " + e.getMessage());
        }
    }
}