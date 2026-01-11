package com.example.project_ltw_25.controller;

import com.example.project_ltw_25.dao.ProductDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.example.project_ltw_25.model.Product;
import com.example.project_ltw_25.services.ProductService;
import java.io.IOException;
import java.util.List;

@WebServlet("/detail")
public class ProductDetailServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO(); // Dùng trực tiếp DAO hoặc qua Service tùy cấu trúc bạn

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        if (idRaw == null) {
            response.sendRedirect("home");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);
            Product product = productDAO.getById(id); // Hàm này bạn đã có (load product + variants + images)

            if (product != null) {
                // Lấy sản phẩm tương tự cùng Category
                List<Product> relatedProducts = productDAO.getRelatedProducts(product.getCategory_id(), id);

                request.setAttribute("product", product);
                request.setAttribute("relatedProducts", relatedProducts);

                request.getRequestDispatcher("/frontend/detail.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        }
    }
}