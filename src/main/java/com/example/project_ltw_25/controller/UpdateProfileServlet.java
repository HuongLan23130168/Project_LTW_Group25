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
        User user = (User) session.getAttribute("acc");

        if (user != null) {
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");

            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.updateUserInfo(user.getId(), fullName, phone);

            if (success) {
                user.setFullName(fullName);
                user.setPhone(phone);
                session.setAttribute("acc", user); 
                response.sendRedirect(request.getContextPath() + "/account?msg=update_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/account?msg=error");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

}
