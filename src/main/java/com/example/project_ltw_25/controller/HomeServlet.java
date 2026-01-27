package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.admin.dao.BannerDAO;
import com.example.project_ltw_25.admin.model.Banner;
import com.example.project_ltw_25.user.dao.ProductDAO;
import com.example.project_ltw_25.user.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeController", value = "/home")
public class HomeServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private final BannerDAO bannerDAO = new BannerDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 1. Lấy Banner đang hoạt động (Lọc từ SQL - rất tối ưu)
            List<Banner> banners = bannerDAO.getActiveBanners();

            // 2. Lấy 4 sản phẩm mới nhất dựa trên ngày tạo (created_at)
            List<Product> newProducts = productDAO.getNewestProducts(4);

            // 3. Lấy 4 sản phẩm bán chạy nhất dựa trên doanh số (totalSold)
            List<Product> hotProducts = productDAO.getBestSellerProducts(4);

            // 4. Đẩy dữ liệu sang JSP
            request.setAttribute("banners", banners);
            request.setAttribute("newProducts", newProducts); // Khớp với model Product bản mới
            request.setAttribute("hotProducts", hotProducts);

            // Thêm tiêu đề trang (Breadcrumb hoặc Title) nếu cần
            request.setAttribute("pageTitle", "Trang chủ - Cửa hàng LTW 25");

            // Chuyển hướng đến trang chủ
            request.getRequestDispatcher("/frontend/home.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý lỗi tập trung: Giúp người dùng biết hệ thống đang bảo trì thay vì lỗi 404
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Hệ thống đang gặp sự cố khi tải trang chủ. Vui lòng thử lại sau.");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Mặc định HomeController không xử lý POST, chuyển hướng về GET
        doGet(request, response);
    }
}
