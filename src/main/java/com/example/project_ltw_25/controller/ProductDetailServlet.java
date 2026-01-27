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
                List<Product> related = productDAO.getRelatedProducts(product.getCategory_id(), id);

                request.setAttribute("product", product);
                request.setAttribute("relatedProducts", related);

                request.getRequestDispatcher("/frontend/detail.jsp").forward(request, response);
            } else {
                System.out.println("DEBUG: Không tìm thấy sản phẩm ID = " + id);
                response.sendRedirect(contextPath + "/home?error=not_found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(contextPath + "/home?error=system_error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
