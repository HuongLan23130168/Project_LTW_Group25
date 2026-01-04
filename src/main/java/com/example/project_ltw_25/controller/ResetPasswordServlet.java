package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

import static com.example.project_ltw_25.user.util.EncryptionUtils.hashMD5;

@WebServlet(name = "ResetPasswordServlet", value = "/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");
        String email = (String) request.getSession().getAttribute("resetEmail");

        if (newPass != null && newPass.equals(confirmPass)) {
            UserDAO dao = new UserDAO();
            // Truyền newPass thô vào, hàm updatePassword trong DAO sẽ tự băm
            String hashPassword = hashMD5(newPass);
            boolean success = dao.updatePassword(email, hashPassword);

            if (success) {
                request.getSession().removeAttribute("resetEmail");
                request.setAttribute("successMessage", "Đổi mật khẩu thành công! Hãy đăng nhập.");
                request.getRequestDispatcher("/frontend/login.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Cập nhật thất bại, vui lòng thử lại.");
                request.getRequestDispatcher("/frontend/newPass.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("/frontend/newPass.jsp").forward(request, response);
        }
    }
}