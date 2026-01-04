package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ForgotPasswordServlet", value = "/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        UserDAO dao = new UserDAO();

        // Giả sử bạn viết thêm hàm checkEmail trong UserDAO
        if (dao.checkEmailExists(email)) {
            // Trong thực tế: Gửi mail chứa link token ở đây.
            // Hiện tại: Chuyển hướng sang trang nhập pass mới (kèm email qua session hoặc attr)
            HttpSession session = request.getSession();
            session.setAttribute("resetEmail", email);

            request.setAttribute("successMessage", "Email hợp lệ! Hãy đặt lại mật khẩu.");
            request.getRequestDispatcher("/frontend/newPass.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Email này không tồn tại trong hệ thống!");
            request.getRequestDispatcher("/frontend/forgot.jsp").forward(request, response);
        }
    }
}