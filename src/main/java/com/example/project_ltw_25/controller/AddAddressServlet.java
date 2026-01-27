package com.example.project_ltw_25.user.controller;

import com.example.project_ltw_25.user.dao.AddressDAO;
import com.example.project_ltw_25.user.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/add-address")
public class AddAddressServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        // --- SỬA QUAN TRỌNG: Đổi "user" thành "acc" để lấy đúng User đang đăng nhập ---
        User user = (User) session.getAttribute("acc");

        if (user != null) {
            String address = request.getParameter("address");

            // Checkbox: nếu tick thì value != null => lấy 1, ngược lại 0
            int isDefault = request.getParameter("isDefault") != null ? 1 : 0;

            AddressDAO dao = new AddressDAO();
            // Gọi hàm thêm địa chỉ vào bảng addresses
            boolean success = dao.addAddress(user.getId(), address, isDefault);

            if(success) {
                // Redirect về trang account kèm thông báo thành công
                // Dùng getContextPath() để đảm bảo đường dẫn đúng tuyệt đối
                response.sendRedirect(request.getContextPath() + "/account?msg=addr_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/account?msg=error");
            }
        } else {
            // Nếu mất session (chưa đăng nhập), chuyển hướng về trang login đúng đường dẫn
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
}