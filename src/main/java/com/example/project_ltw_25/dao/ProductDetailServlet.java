package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.model.Product;
import com.example.project_ltw_25.user.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailServlet", value = "/detail-product")
public class ProductDetailServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);
            Product product = productService.getProduct(id);

            if (product != null) {
                List<Product> relatedProducts = productService.getRelatedProducts(product.getCategory_id(), id);

                // --- DEBUGGING ---
                System.out.println("[DEBUG] Product ID: " + product.getId());
                System.out.println("[DEBUG] Discount Percent: " + product.getDiscountPercent());
                System.out.println("[DEBUG] Related Products Count: " + (relatedProducts != null ? relatedProducts.size() : 0));
                // --- END DEBUGGING ---

                request.setAttribute("product", product);
                request.setAttribute("relatedProducts", relatedProducts);

                request.getRequestDispatcher("/frontend/detail.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi server: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Có thể xử lý logic thêm vào giỏ hàng ở đây nếu cần
    }
}
