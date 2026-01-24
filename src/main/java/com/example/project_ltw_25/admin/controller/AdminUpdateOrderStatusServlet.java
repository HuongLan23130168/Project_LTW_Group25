package com.example.project_ltw_25.admin.controller;

import com.example.project_ltw_25.admin.dao.OrderDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "UpdateOrderStatusServlet", value = "/admin/updateOrderStatus")
public class AdminUpdateOrderStatusServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Xử lý tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        try {
            // 1. Lấy dữ liệu
            String orderIdStr = request.getParameter("orderId");
            String currentStatus = request.getParameter("currentStatus");

            if (orderIdStr == null || currentStatus == null) {
                response.sendRedirect(request.getContextPath() + "/admin/orders?error=missing_info");
                return;
            }

            int orderId = Integer.parseInt(orderIdStr);
            String newStatus = "";
            String newShippingStatus = "";

            // 2. Logic "Nút Tick": Tự động nâng cấp trạng thái
            //  1: Từ "Chờ xử lý" -> Chuyển sang "Đang giao"
            if (currentStatus.equalsIgnoreCase("Chờ xử lý") || currentStatus.equalsIgnoreCase("Chờ lấy hàng")) {
                newStatus = "Đang giao";
                newShippingStatus = "Đang vận chuyển";
            }

            //  2: Từ "Đang giao" -> Chuyển sang "Chờ xác nhận"

            else if (currentStatus.contains("Đang giao") || currentStatus.contains("Vận chuyển")) {
                newStatus = "Chờ xác nhận";
                newShippingStatus = "Đã đến nơi giao nhận";


             //  3: Khách hàng bấm (Code này nằm ở Servlet của User)
            //


            } else {
                // Nếu đã hoàn thành hoặc đã hủy thì không làm gì
                response.sendRedirect(request.getContextPath() + "/admin/orders?msg=no_change");
                return;
            }

            // 3. Gọi DAO cập nhật cả 2 bảng
            boolean success = orderDAO.updateOrderStatus(orderId, newStatus, newShippingStatus);

            if (success) {
                // Nếu đang ở trang chi tiết (View Order) thì reload lại trang đó
                String referer = request.getHeader("Referer");
                if (referer != null && referer.contains("viewOrder")) {
                    response.sendRedirect(request.getContextPath() + "/admin/viewOrder?orderId=" + orderId + "&msg=success");
                } else {
                    // Nếu ở trang danh sách
                    response.sendRedirect(request.getContextPath() + "/admin/orders?msg=success");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/orders?error=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/orders?error=system");
        }
    }
}