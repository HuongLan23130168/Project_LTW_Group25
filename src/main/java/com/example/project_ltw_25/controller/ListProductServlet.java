package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.ListProductDao;
import com.example.project_ltw_25.user.model.Product;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ListProductController", value = "/list-product")
public class ListProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String priceRange = request.getParameter("priceRange");
        String[] rooms = request.getParameterValues("room");
        String[] categories = request.getParameterValues("category");
        String sort = request.getParameter("sort");

        int page = 1;
        int size = 12;

        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        String roomName = "Tất cả sản phẩm";
        if (rooms != null) {
            if (rooms.length == 1) {
                roomName = switch (rooms[0]) {
                    case "1" -> "Phòng khách";
                    case "2" -> "Phòng bếp";
                    case "3" -> "Phòng ngủ";
                    case "4" -> "Phòng làm việc";
                    case "5" -> "Ban công";
                    default -> "Sản phẩm theo phòng";
                };
            } else if (rooms.length > 1) {
                roomName = "Kết quả lọc theo nhiều phòng";
            }
        }
        request.setAttribute("roomName", roomName);

        try {
            ListProductDao dao = new ListProductDao();

            List<Product> products = dao.filterProducts(priceRange, rooms, categories, sort, page, size);

            int total = dao.countProducts(priceRange, rooms, categories);
            int totalPages = (int) Math.ceil((double) total / size);

            request.setAttribute("products", products);
            request.setAttribute("page", page);
            request.setAttribute("totalPages", totalPages);

            System.out.println("Lọc thành công: " + (products != null ? products.size() : 0) + " sản phẩm.");

            request.getRequestDispatcher("/frontend/living.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi kết nối cơ sở dữ liệu hoặc logic DAO: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}