package com.example.demoweb1.controller;

import com.example.demoweb1.dao.CustomerDAO;
import com.example.demoweb1.model.Customer;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Lấy tham số sắp xếp từ URL
        String sortBy = req.getParameter("sortBy");
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "newest"; // Đặt giá trị mặc định là "mới nhất"
        }

        // Gọi DAO để lấy danh sách khách hàng từ database
        CustomerDAO dao = new CustomerDAO();
        List<Customer> customerList = dao.getAllCustomers(sortBy);

        // Đặt danh sách khách hàng vào request để gửi cho JSP
        req.setAttribute("customers", customerList);

        // Chuyển hướng đến trang JSP để hiển thị
        req.getRequestDispatcher("/admin/customers.jsp").forward(req, resp);
    }
}
    