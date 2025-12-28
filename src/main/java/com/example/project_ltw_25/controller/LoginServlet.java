package com.example.project_ltw_25.controller;

import com.example.project_ltw_25.dao.UserDAO;
import com.example.project_ltw_25.model.User;
import com.example.project_ltw_25.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

import static com.example.project_ltw_25.util.EncryptionUtils.hashMD5;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String hashPassword = hashMD5(password);
        UserDAO dao = new UserDAO();
        User user = dao.login(email, hashPassword);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("acc", user);
            session.setAttribute("userId", user.getId());
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            // SAI: response.sendRedirect(request.getContextPath() + "/home");

            // ĐÚNG: Dùng Forward để mang theo "errorMessage" về lại trang login
            request.setAttribute("errorMessage", "Email hoặc mật khẩu không chính xác!");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
        }
    }

}
