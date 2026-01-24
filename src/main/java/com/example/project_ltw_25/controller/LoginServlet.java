package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.UserDAO;
import com.example.project_ltw_25.user.model.User;
import com.example.project_ltw_25.user.services.EmailService;
import com.example.project_ltw_25.user.util.EncryptionUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

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
        String hashedPass = EncryptionUtils.hashMD5(password);

        UserDAO dao = new UserDAO();
        User user = dao.login(email, hashedPass);

        if (user != null) {
            // 1. Tạo mã duy nhất
            String token = java.util.UUID.randomUUID().toString();
            // 2. Lưu vào DB (hạn dùng 15 phút)
            dao.updateToken(email, token);
            // 3. Gửi Mail link xác thực
            new EmailService().sendMagicLink(email, token, request.getContextPath());

            request.setAttribute("successMessage", "Vui lòng kiểm tra Email để nhận link đăng nhập duy nhất!");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Email hoặc mật khẩu không chính xác!");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
        }
    }

}
