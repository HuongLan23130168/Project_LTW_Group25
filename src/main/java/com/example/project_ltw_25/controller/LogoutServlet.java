package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String target = "/frontend/index.jsp";

        if (session != null) {
            User user = (User) session.getAttribute("acc");
            if (user != null && "2".equals(user.getRole())) {
                target = "/login?msg=logout_success";
            }
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + target);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}