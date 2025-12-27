package com.example.project_ltw_25.controller;

import com.example.project_ltw_25.dao.ProductDao;
import com.example.project_ltw_25.model.Product;
import com.example.project_ltw_25.services.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ListProductController", value = "/list-product")
public class ListProductController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String priceRange = request.getParameter("priceRange");
        String[] rooms = request.getParameterValues("room"); // PHÒNG
        String[] categories = request.getParameterValues("category");         // CÂY / HOA / ĐÈN
        String sort = request.getParameter("sort");

        int page = 1;
        int size = 9;

        try {
            if (request.getParameter("page") != null)
                page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ignored) {}

        System.out.println("priceRange = " + priceRange);
        System.out.println("sort = " + sort);
        System.out.println("rooms = " + java.util.Arrays.toString(rooms));
        System.out.println("categories = " + java.util.Arrays.toString(categories));
        ProductDao dao = new ProductDao();

        List<Product> products = dao.filterProducts(
                priceRange, rooms, categories, sort, page, size
        );

        int total = dao.countProducts(priceRange, rooms, categories);
        int totalPages = (int) Math.ceil((double) total / size);

        request.setAttribute("products", products);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/frontend/living.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}