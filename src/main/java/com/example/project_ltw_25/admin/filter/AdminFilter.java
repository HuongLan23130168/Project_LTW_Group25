package com.example.project_ltw_25.admin.filter;

import com.example.project_ltw_25.user.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // Lấy đối tượng user từ session
        User user = (session != null) ? (User) session.getAttribute("acc") : null;

        // KIỂM TRA QUYỀN
        // Nếu đã đăng nhập VÀ có role là "2" (Admin)
        if (user != null && "2".equals(user.getRole())) {
            // Cho phép đi tiếp vào trang admin
            chain.doFilter(request, response);
        } else {
            // Nếu không phải admin, đẩy về trang login bên ngoài thư mục admin
            // CỰC KỲ QUAN TRỌNG: Trang đích của redirect KHÔNG ĐƯỢC nằm trong vùng bị Filter chặn (/admin/*)
            res.sendRedirect(req.getContextPath() + "/login?error=denied");
        }
    }

    @Override
    public void destroy() {
    }
}
