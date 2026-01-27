package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.OrderDAO; // Import DAO
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/updateOrderStatus")
public class UpdateOrderStatusServlet extends HttpServlet {

   
    private final OrderDAO orderDAO = new OrderDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");

            boolean success = orderDAO.updateOrderStatus(orderId, status);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/orders?msg=success");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/orders?error=failed");
            }
          
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Mã đơn hàng không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }
    }

}
