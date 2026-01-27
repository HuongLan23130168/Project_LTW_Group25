package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.UserDAO;
import com.example.project_ltw_25.user.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/update-profile")
public class UpdateProfileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        // --- SỬA LỖI Ở ĐÂY: Đổi "user" thành "acc" ---
        User user = (User) session.getAttribute("acc");

        if (user != null) {
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");

            UserDAO userDAO = new UserDAO();
            // Gọi hàm update (lưu ý hàm updateUserInfo cần tồn tại trong UserDAO)
            boolean success = userDAO.updateUserInfo(user.getId(), fullName, phone);

            if (success) {
                // Cập nhật lại thông tin trong session để hiển thị ngay lập tức mà không cần logout
                user.setFullName(fullName);
                user.setPhone(phone);
                session.setAttribute("acc", user); // Cập nhật lại biến "acc"

                // Redirect về trang account (dùng getContextPath để tránh lỗi đường dẫn)
                response.sendRedirect(request.getContextPath() + "/account?msg=update_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/account?msg=error");
            }
        } else {
            // Nếu mất session, quay về trang login
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
}