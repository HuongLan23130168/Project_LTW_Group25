package com.example.project_ltw_25.admin;

import com.example.project_ltw_25.user.dao.UserDAO;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", value = "/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        UserDAO dao = new UserDAO();

        // Bạn cần viết thêm hàm getAllUsers() trong UserDAO (xem mục 2 bên dưới)
        List<User> userList = dao.getAllUsers();

        // Gửi danh sách user sang trang JSP
        request.setAttribute("userList", userList);

        // Forward tới trang JSP nằm trong thư mục admin
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}