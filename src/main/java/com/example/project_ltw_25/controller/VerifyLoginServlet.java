package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.UserDAO;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "VerifyLoginServlet", value = "/verify-login")
public class VerifyLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        UserDAO dao = new UserDAO();

        // Tìm user dựa trên token còn hạn
        User user = dao.getUserByToken(token);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("acc", user);

            System.out.println("User: " + user.getFullName() + " - Role: [" + user.getRole() + "]");

            // Kiểm tra Role để Redirect
            if ("2".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }

            dao.clearToken(user.getEmail());
        } else {
            // Token sai hoặc hết hạn
            request.setAttribute("errorMessage", "Liên kết xác thực không hợp lệ hoặc đã hết hạn!");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}