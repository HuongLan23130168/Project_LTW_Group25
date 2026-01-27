package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.CartDAO;
import com.example.project_ltw_25.user.dao.UserDAO;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "VerifyLoginServlet", value = "/verify-login")
public class VerifyLoginServlet extends HttpServlet {


    private static final boolean DEV_MODE = true;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        UserDAO dao = new UserDAO();

        User user = dao.getUserByToken(token);

        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("acc", user);

            String role = String.valueOf(user.getRole());
            System.out.println("DEBUG: User Role is -> " + role);

            CartDAO cartDao = new CartDAO();
            int total = cartDao.getTotalQuantityByUserId(user.getId());
            session.setAttribute("totalQty", total);

            if ("2".equals(user.getRole())) {

                System.out.println("Đăng nhập thành công với quyền ADMIN. Đang chuyển hướng...");
                response.sendRedirect(request.getContextPath() + "/admin/admin-dashboard");
            } else {
                // set xóa token
                // dao.clearToken(user.getEmail());
                System.out.println("Đăng nhập thành công với quyền USER.");
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } else {
            request.setAttribute("errorMessage", "Liên kết xác thực không hợp lệ hoặc đã hết hạn!");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}