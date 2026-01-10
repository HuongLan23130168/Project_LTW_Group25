package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.DashboardDAO;
import com.example.project_ltw_25.admin.model.Order;
import com.example.project_ltw_25.user.dao.UserDAO;
import com.example.project_ltw_25.user.model.User;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminDashboardServlet", value = "/admin/admin-dashboard")
public class AdminDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đảm bảo tiếng Việt không bị lỗi font
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        DashboardDAO dashboardDAO = new DashboardDAO();
        UserDAO userDAO = new UserDAO();

        // 1. Lấy dữ liệu thống kê (Cards)
        request.setAttribute("revenue", dashboardDAO.getTotalRevenue());
        request.setAttribute("pendingOrdersCount", dashboardDAO.countPendingOrders());
        request.setAttribute("lowStock", dashboardDAO.getLowStockCount());
        request.setAttribute("totalStock", dashboardDAO.getTotalStock());

        // 2. Lấy danh sách hiển thị (Table & List)
        request.setAttribute("recentOrders", dashboardDAO.getRecentOrders());
        request.setAttribute("bestSellers", dashboardDAO.getBestSellers());

        // Gộp phần lấy User từ Servlet thứ hai vào đây
        List<User> userList = userDAO.getAllUsers();
        request.setAttribute("userList", userList);

        // 3. Xử lý dữ liệu biểu đồ (7 ngày gần nhất)
        List<Map<String, Object>> chartData = dashboardDAO.getRevenueLast7Days();
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        for (Map<String, Object> map : chartData) {
            labels.add(map.get("date").toString());
            values.add(Double.parseDouble(map.get("daily_revenue").toString()));
        }

        // Chuyển sang JSON để Chart.js ở Client-side có thể đọc được
        request.setAttribute("jsonLabels", new Gson().toJson(labels));
        request.setAttribute("jsonValues", new Gson().toJson(values));

        // 4. Chuyển hướng đến trang giao diện duy nhất
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}