package com.example.project_ltw_25.controller;

import com.example.project_ltw_25.dao.UserDao;
import com.example.project_ltw_25.model.User;
import com.example.project_ltw_25.util.PasswordUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirmPassword");

        if (!password.equals(confirm)) {
            request.setAttribute("error", "Mật khẩu không khớp!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        UserDao dao = new UserDao();
        dao.register(fullname, email, password);

        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
