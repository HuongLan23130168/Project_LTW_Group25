package com.example.ttltw_project.controller.user;

import com.example.ttltw_project.dao.admin.OrderDAO;
import com.example.ttltw_project.model.admin.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ConfirmBankTransferServlet", value = "/confirm-bank-transfer")
public class ConfirmBankTransferServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderCode = request.getParameter("orderCode");

        if (orderCode == null || orderCode.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        OrderDAO orderDAO = new OrderDAO();
        boolean updated = orderDAO.userConfirmBankTransfer(orderCode);

        if (updated) {
            System.out.println("Đã xác nhận thanh toán cho đơn hàng: " + orderCode);
        } else {
            System.out.println("Không tìm thấy đơn hàng: " + orderCode);
        }

        response.sendRedirect(request.getContextPath() + "/order-completed?orderCode=" + orderCode);
    }
}