package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.AdminProductDAO;
import com.example.project_ltw_25.user.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminProductServlet", value = "/admin/products")
public class AdminProductServlet extends HttpServlet {

    private static final int PAGE_SIZE = 10; // Số sản phẩm mỗi trang

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            AdminProductDAO dao = new AdminProductDAO();

            int page = 1;
            if (request.getParameter("page") != null) {
                try {
                    page = Integer.parseInt(request.getParameter("page"));
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            int totalProducts = dao.getTotalProductCount();
            int totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);

            List<Product> list = dao.getProducts(page, PAGE_SIZE);

            request.setAttribute("products", list);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);


            String msg = request.getParameter("msg");
            if (msg != null) {
                switch (msg) {
                    case "added" -> request.setAttribute("alertMessage", "Thêm sản phẩm mới thành công!");
                    case "updated" -> request.setAttribute("alertMessage", "Cập nhật sản phẩm thành công!");
                    case "deleted" -> request.setAttribute("alertMessage", "Đã xóa sản phẩm khỏi hệ thống!");
                }
                request.setAttribute("alertType", "success");
            }

            String error = request.getParameter("error");
            if (error != null) {
                request.setAttribute("alertMessage", "Thao tác thất bại: " + error);
                request.setAttribute("alertType", "danger");
            }

            request.getRequestDispatcher("/admin/products.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("alertMessage", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("alertType", "danger");
            request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}