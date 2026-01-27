package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.AddressDAO;
import com.example.project_ltw_25.user.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/add-address")
public class AddAddressServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("acc");

        if (user != null) {
            String address = request.getParameter("address");

            int isDefault = request.getParameter("isDefault") != null ? 1 : 0;

            AddressDAO dao = new AddressDAO();
            boolean success = dao.addAddress(user.getId(), address, isDefault);

            if(success) {
                response.sendRedirect(request.getContextPath() + "/account?msg=addr_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/account?msg=error");
            }
        } else {
            
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

}
