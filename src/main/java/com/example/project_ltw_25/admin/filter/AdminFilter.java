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

        User user = (session != null) ? (User) session.getAttribute("acc") : null;

        // THÊM DÒNG NÀY ĐỂ KIỂM TRA TRẠNG THÁI
        if (user == null) {
            System.out.println("Filter: No user found in session. Redirecting to login.");
        } else {
            System.out.println("Filter: User " + user.getEmail() + " has Role: " + user.getRole());
        }

        if (user != null) {
            System.out.println("Filter Check - User: " + user.getEmail() + " | Role: " + user.getRole());
            if ("2".equals(String.valueOf(user.getRole()))) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(req.getContextPath() + "/login?error=denied");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
