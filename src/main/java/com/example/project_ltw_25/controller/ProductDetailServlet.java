package com.example.project_ltw_25.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.example.project_ltw_25.model.Product;
import com.example.project_ltw_25.services.ProductService;
import java.io.IOException;
import java.util.List;

@WebServlet("/detail")
public class ProductDetailServlet extends HttpServlet {
    private final ProductService service = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Product ID");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);
            Product product = service.getById(id);

            if (product != null) {
                request.setAttribute("product", product);

                // Lấy danh sách sản phẩm tương tự
                List<Product> relatedProducts = service.getRelatedProducts(product);
                request.setAttribute("relatedProducts", relatedProducts);

                // Đảm bảo đường dẫn này khớp với thư mục chứa file jsp của bạn
                request.getRequestDispatcher("/frontend/detail.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product Not Found");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID Format");
        }
    }
}