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
            List<Banner> banners = bannerDAO.getActiveBanners();

            List<Product> newProducts = productDAO.getNewestProducts(4);

            List<Product> hotProducts = productDAO.getBestSellerProducts(4);

            request.setAttribute("banners", banners);
            request.setAttribute("newProducts", newProducts);
            request.setAttribute("hotProducts", hotProducts);

            request.setAttribute("pageTitle", "Trang chủ - Cửa hàng LTW 25");

            request.getRequestDispatcher("/frontend/home.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Hệ thống đang gặp sự cố khi tải trang chủ. Vui lòng thử lại sau.");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

