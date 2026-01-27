package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.AdminProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminDeleteProductServlet", value = "/admin/deleteProduct")
public class AdminDeleteProductServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            AdminProductDAO dao = new AdminProductDAO();
            dao.softDeleteProduct(id);
            response.sendRedirect(request.getContextPath() + "/admin/products?msg=deleted");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/products?error=delete_failed");
        }
    }
}