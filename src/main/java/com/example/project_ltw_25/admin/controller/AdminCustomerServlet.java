package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.CustomerDAO;
import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminCustomerServlet", value = "/admin/customers")
public class AdminCustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sortBy = request.getParameter("sortBy");
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "newest";
        }

        String search = request.getParameter("search");

        try {
            CustomerDAO dao = new CustomerDAO();
            List<User> customerList = dao.getAllCustomers(sortBy, search);

            // Gửi dữ liệu sang JSP
            request.setAttribute("customers", customerList);
            request.setAttribute("currentSort", sortBy);
            request.setAttribute("search", search);


            request.getRequestDispatcher("/admin/customers.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console server để debug
            response.getWriter().println("Lỗi Server: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}