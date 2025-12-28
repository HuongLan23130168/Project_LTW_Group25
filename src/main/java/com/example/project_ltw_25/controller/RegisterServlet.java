package com.example.project_ltw_25.controller;

import com.example.project_ltw_25.dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

import static com.example.project_ltw_25.util.EncryptionUtils.hashMD5;

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
        String emailRegex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        if (email == null || !email.matches(emailRegex)) {
            request.setAttribute("registerError", "Email không hợp lệ! Vui lòng sử dụng địa chỉ @gmail.com");
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

        String hashPassword = hashMD5(password);
        UserDAO dao = new UserDAO();

        // LƯU Ý: Phải truyền hashPassword vào đây
        boolean success = dao.register(fullName, email, hashPassword);

        if (success) {
            request.setAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
        } else {
            request.setAttribute("registerError", "Email đã tồn tại hoặc có lỗi xảy ra!");
            request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
        }
    }
}