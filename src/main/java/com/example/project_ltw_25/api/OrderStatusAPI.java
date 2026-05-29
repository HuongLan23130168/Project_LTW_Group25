package com.example.ttltw_project.controller.api;

import com.example.ttltw_project.dao.admin.OrderDAO;
import com.example.ttltw_project.model.admin.Order;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/orders/*")
public class OrderStatusAPI extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final Gson gson = new Gson();

    private static final String API_KEY = "nlt_secret_2024";

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Kiểm tra API Key
        String apiKey = request.getHeader("X-API-Key");
        if (apiKey == null || !apiKey.equals(API_KEY)) {
            sendError(response, 401, "Invalid or missing API Key");
            return;
        }

        // lấy orderId từ URL
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            sendError(response, 400, "Missing order ID. Use: /api/orders/{id}");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            sendError(response, 400, "Invalid order ID format");
            return;
        }

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JsonObject body;
        try {
            body = gson.fromJson(sb.toString(), JsonObject.class);
        } catch (Exception e) {
            sendError(response, 400, "Invalid JSON format");
            return;
        }

        String newStatus = body.has("status") ? body.get("status").getAsString() : null;
        String note = body.has("note") ? body.get("note").getAsString() : "";

        if (newStatus == null || newStatus.isEmpty()) {
            sendError(response, 400, "Missing required field: status");
            return;
        }

        // Kiểm tra đơn hàng có tồn tại hay khong
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            sendError(response, 404, "Order not found with ID: " + orderId);
            return;
        }

        String oldStatus = order.getStatus();

        if (!isValidTransition(oldStatus, newStatus)) {
            sendError(response, 400, String.format(
                    "Cannot change status from '%s' to '%s'", oldStatus, newStatus));
            return;
        }

        // Cập nhật trạng thái
        String shippingStatus = mapToShippingStatus(newStatus);
        boolean success = orderDAO.updateOrderStatus(orderId, newStatus, note, shippingStatus);


        if (success) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Order status updated successfully");
            result.put("order_id", orderId);
            result.put("old_status", oldStatus);
            result.put("new_status", newStatus);
            result.put("updated_at", System.currentTimeMillis());

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(result));
        } else {
            sendError(response, 500, "Failed to update order status");
        }
    }

    // Kiểm tra chuyển trạng thái
    private boolean isValidTransition(String current, String next) {
        if (current == null || next == null) return false;

        // Luồng trạng thái
        Map<String, String[]> validTransitions = new HashMap<>();
        validTransitions.put("Chờ xác nhận thanh toán", new String[]{"Chờ xử lý", "Đã hủy"});
        validTransitions.put("Chờ xử lý", new String[]{"Đã xác nhận - Giao vận chuyển", "Đã hủy"});
        validTransitions.put("Đã xác nhận - Giao vận chuyển", new String[]{"Đã lấy hàng", "Đã hủy"});
        validTransitions.put("Đã lấy hàng", new String[]{"Đang vận chuyển", "Đã hủy"});
        validTransitions.put("Đang vận chuyển", new String[]{"Đã giao hàng - Hoàn thành", "Đã hủy"});
        validTransitions.put("Đã giao hàng - Hoàn thành", new String[]{});
        validTransitions.put("Đã hủy", new String[]{});

        String[] allowed = validTransitions.get(current);
        if (allowed == null) return false;

        for (String status : allowed) {
            if (status.equals(next)) return true;
        }
        return false;
    }

    // Map status order sang shipping status
    private String mapToShippingStatus(String orderStatus) {
        switch (orderStatus) {
            case "Đã xác nhận - Giao vận chuyển": return "Đã giao cho đơn vị vận chuyển";
            case "Đã lấy hàng": return "Đã lấy hàng";
            case "Đang vận chuyển": return "Đang vận chuyển";
            case "Đã giao hàng - Hoàn thành": return "Giao thành công";
            case "Đã hủy": return "Đã hủy";
            default: return null;
        }
    }

    //lấy thông tin đon hàng
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            String status = request.getParameter("status");
            if (status != null && !status.isEmpty()) {
                sendJsonResponse(response, orderDAO.getOrdersByStatus(status));
            } else {
                sendJsonResponse(response, orderDAO.getAllOrders("newest", null));
            }
            return;
        }

        // Lấy 1 đơn hàng cụ thể
        try {
            int orderId = Integer.parseInt(pathInfo.substring(1));
            Order order = orderDAO.getOrderById(orderId);

            if (order != null) {
                sendJsonResponse(response, order);
            } else {
                sendError(response, 404, "Order not found");
            }
        } catch (NumberFormatException e) {
            sendError(response, 400, "Invalid order ID");
        }
    }

    // Helper methods
    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(gson.toJson(data));
    }

    private void sendError(HttpServletResponse response, int statusCode, String message)
            throws IOException {
        response.setStatus(statusCode);
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", String.valueOf(System.currentTimeMillis()));
        response.getWriter().write(gson.toJson(error));
    }
}
