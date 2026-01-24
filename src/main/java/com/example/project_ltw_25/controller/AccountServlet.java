package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.admin.dao.OrderDAO;
import com.example.project_ltw_25.user.dao.UserDAO;
import com.example.project_ltw_25.user.model.User;
import com.example.project_ltw_25.user.model.UserOrder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AccountServlet", urlPatterns = "/account")
public class AccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User authUser = (User) session.getAttribute("acc");

        // 1. Kiểm tra đăng nhập
        if (authUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // 2. Lấy thông tin User chi tiết (bao gồm địa chỉ mới nhất)
        UserDAO userDAO = new UserDAO();
        User currentUser = userDAO.getUserWithAddress(authUser.getEmail());
        req.setAttribute("user", currentUser);

        // 3. Lấy toàn bộ danh sách đơn hàng của User
        OrderDAO orderDAO = new OrderDAO();
        List<UserOrder> allOrders = orderDAO.getMyOrders(authUser.getId());

        // 4. Xử lý bộ lọc (Filter) theo Tabs
        String statusParam = req.getParameter("status");
        List<UserOrder> filteredOrders = new ArrayList<>();

        // Mặc định là trang profile, nhưng nếu có statusParam thì chuyển sang tab orders
        String activePage = "profile";

        if (statusParam == null || statusParam.equals("all")) {
            // Tab "Tất cả"
            filteredOrders = allOrders;
            if (statusParam != null) activePage = "orders";
        } else {
            // Các tab con: Chờ xử lý, Đang giao, Đã giao...
            activePage = "orders";

            for (UserOrder order : allOrders) {
                // Chẩn hóa chuỗi trạng thái về chữ thường để so sánh chính xác
                String dbStatus = order.getStatus() != null ? order.getStatus().toLowerCase().trim() : "";
                boolean isMatch = false;

                switch (statusParam) {
                    case "wait":
                        // Bao gồm: Chờ xử lý, Chờ lấy hàng, Chờ xác nhận
                        if (dbStatus.contains("chờ")) {
                            isMatch = true;
                        }
                        break;

                    case "shipping":
                        // [ĐÃ SỬA LỖI Ở ĐÂY]
                        // Chỉ lấy trạng thái có chữ "đang" (Đang giao) hoặc "vận chuyển".
                        // Tuyệt đối KHÔNG dùng .contains("giao") vì nó sẽ dính cả "Đã giao".
                        if (dbStatus.contains("đang") || dbStatus.contains("vận chuyển")) {
                            isMatch = true;
                        }
                        break;

                    case "done":
                        // Bao gồm: Đã giao, Hoàn thành, Giao hàng thành công
                        if (dbStatus.contains("đã giao") || dbStatus.contains("hoàn thành") || dbStatus.contains("thành công")) {
                            isMatch = true;
                        }
                        break;

                    case "cancel":
                        // Bao gồm: Đã hủy, Hủy đơn
                        if (dbStatus.contains("hủy")) {
                            isMatch = true;
                        }
                        break;
                }

                if (isMatch) {
                    filteredOrders.add(order);
                }
            }
        }

        // 5. Gửi dữ liệu sang JSP
        req.setAttribute("orders", filteredOrders);
        req.setAttribute("activePage", activePage); // Biến này để JSP biết tab nào đang sáng

        req.getRequestDispatcher("/frontend/account.jsp").forward(req, resp);
    }
}