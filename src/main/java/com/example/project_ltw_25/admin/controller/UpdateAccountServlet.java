package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.user.dao.UserDAO;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdateAccountServlet", value = "/admin/update-account")
public class UpdateAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        UserDAO dao = new UserDAO();
        try {
            if (phone != null && phone.length() > 20) {
                response.sendRedirect(request.getContextPath() + "/admin/account?msg=error&detail=phone_too_long");
                return;
            }
            boolean success = dao.updateAdminProfile(email, fullName, phone, address);

            if (success) {
                HttpSession session = request.getSession();
                User currentUser = (User) session.getAttribute("acc");

                if (currentUser != null) {
                    currentUser.setFullName(fullName);
                    currentUser.setPhone(phone);
                    currentUser.setAddress(address);
                    session.setAttribute("acc", currentUser);
                }
                response.sendRedirect(request.getContextPath() + "/admin/account?msg=success");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/account?msg=error");
            }
        }catch (Exception e) {
            if (e.getMessage().contains("Data too long")) {
                response.sendRedirect(request.getContextPath() + "/admin/account?msg=error&detail=data_too_long");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/account?msg=error&detail=server_error");
            }
        }
    }
}
