package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.model.Product;
import com.example.project_ltw_25.user.dao.ProductDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailServlet", value = "/detail-product")
public class ProductDetailServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        String contextPath = request.getContextPath();

        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendRedirect(contextPath + "/home");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);
            Product product = productDAO.getById(id);

            // Kiểm tra sản phẩm có tồn tại không
            if (product != null) {
                // Lấy sản phẩm liên quan
                List<Product> related = productDAO.getRelatedProducts(product.getCategory_id(), id);

                request.setAttribute("product", product);
                request.setAttribute("relatedProducts", related);

                // Gửi sang trang chi tiết
                request.getRequestDispatcher("/frontend/detail.jsp").forward(request, response);
            } else {
                // Không tìm thấy ID sản phẩm trong DB
                System.out.println("DEBUG: Không tìm thấy sản phẩm ID = " + id);
                response.sendRedirect(contextPath + "/home?error=not_found");
            }
        } catch (Exception e) {
            // In lỗi ra console của Server (Tomcat) để biết chính xác lỗi gì
            e.printStackTrace();
            response.sendRedirect(contextPath + "/home?error=system_error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}