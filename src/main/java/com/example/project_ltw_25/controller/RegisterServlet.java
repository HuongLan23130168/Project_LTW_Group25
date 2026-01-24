package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.UserDAO;
import com.example.project_ltw_25.user.util.EncryptionUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/frontend/login.jsp");
    }

    // Sửa lại RegisterServlet.java
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String fullName = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPass = request.getParameter("confirmPassword");

        // ĐỔI TỪ errorMessage THÀNH registerError
//      String emailRegex = "^[a-zA-Z0-9._%+-]+@(gmail|yahoo|outlook|edu)\\.com$";
        String emailRegex = "^[a-zA-Z0-9._%+-]+@([a-zA-Z0-9.-]+\\.)+(gmail|yahoo|outlook|edu|vn|com)$";

        if (email == null || !email.matches(emailRegex)) {
            request.setAttribute("registerError", "Email không hợp lệ!");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
            return;
        }

        String passRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!password.matches(passRegex)) {
            request.setAttribute("registerError", "Mật khẩu phải có ít nhất 8 ký tự, 1 chữ hoa, 1 số và 1 ký tự đặc biệt!");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPass)) {
            request.setAttribute("registerError", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
            return;
        }

        String hashedPass = EncryptionUtils.hashMD5(password);
        UserDAO dao = new UserDAO();

        // LƯU Ý: Phải truyền hashPassword vào đây
        boolean isSuccess = dao.register(fullName, email, hashedPass);
        if (isSuccess) {
            request.setAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
        } else {
            request.setAttribute("registerError", "Email đã tồn tại!");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
        }
    }
}