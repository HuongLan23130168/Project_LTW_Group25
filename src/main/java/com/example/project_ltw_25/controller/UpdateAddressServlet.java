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

@WebServlet("/update-address")
public class UpdateAddressServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String address = req.getParameter("address");
            int isDefault = req.getParameter("isDefault") != null ? 1 : 0;

            AddressDAO dao = new AddressDAO();
            dao.updateAddress(id, user.getId(), address, isDefault);

            resp.sendRedirect(req.getContextPath() + "/account?msg=update_addr_success");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/account?msg=error");
        }
    }
}